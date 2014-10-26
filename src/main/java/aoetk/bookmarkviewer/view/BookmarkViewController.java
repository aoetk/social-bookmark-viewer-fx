package aoetk.bookmarkviewer.view;

import aoetk.bookmarkviewer.ApplicationContext;
import aoetk.bookmarkviewer.model.BookmarkEntry;
import aoetk.bookmarkviewer.model.BookmarkModel;
import aoetk.bookmarkviewer.service.DiigoServiceClient;
import aoetk.bookmarkviewer.service.LoadingBookmarkService;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ブックマークビューのコントローラ.
 */
public class BookmarkViewController implements Initializable {

    private static final String DIIGOLET_URL = "https://www.diigo.com/javascripts/webtoolbar/diigolet_b_h_b.js";

    private static final String EVERNOTE_CLIPPER_SCRIPT =
            "javascript:(function(){EN_CLIP_HOST='http://www.evernote.com';" +
                    "try{var x=document.createElement('SCRIPT');x.type='text/javascript';" +
                    "x.src=EN_CLIP_HOST+'/public/bookmarkClipper.js?'+(new Date().getTime()/100000);" +
                    "document.getElementsByTagName('head')[0].appendChild(x);}" +
                    "catch(e){location.href=EN_CLIP_HOST+'/clip.action?url=" +
                    "'+encodeURIComponent(location.href)+'&title='+encodeURIComponent(document.title);}})();";

    @FXML
    TextField pageSearchBox;

    @FXML
    Label progressLabel;

    @FXML
    ProgressIndicator webIndicator;

    @FXML
    Button stopButton;

    @FXML
    Button backButton;

    @FXML
    Button forwardButton;

    @FXML
    TextField locationBar;

    @FXML
    WebView webView;

    @FXML
    StackPane basePane;

    @FXML
    TextField searchBox;

    @FXML
    ListView<String> tagListView;

    @FXML
    ListView<BookmarkEntry> bookmarkListView;

    @FXML
    Region vailRegion;

    @FXML
    VBox indicatorBox;

    private BookmarkModel bookmarkModel;

    private LoadingBookmarkService loadingBookmarkService;

    private WebEngine webEngine;

    private WebHistory history;

    private Worker<Void> loadWorker;

    private LoginDialog dialog;

    /**
     * 初期処理. ログインダイアログを開き, ユーザー名とパスワードを取得する.
     *
     * @param url FXMLのURL
     * @param resourceBundle リソースバンドル
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookmarkListView.setCellFactory(bookmarkEntryListView -> new BookmarkCell());
        ApplicationContext context = ApplicationContext.getInstance();
        webView.setZoom(context.getScaleFactor());
        webEngine = webView.getEngine();
        history = webEngine.getHistory();
        loadWorker = webEngine.getLoadWorker();
        dialog = new LoginDialog();
        dialog.setOnAction(actionEvent -> {
            dialog.setVisible(false);
            loadBookmark(dialog.getUser(), dialog.getPassword());
        });
        basePane.getChildren().add(dialog);
    }

    private void loadBookmark(String userName, String password) {
        loadingBookmarkService = new LoadingBookmarkService(new DiigoServiceClient(userName, password));
        loadingBookmarkService.setOnSucceeded(workerStateEvent -> {
            bookmarkModel = loadingBookmarkService.getValue();
            setListContent();
            addListeners();
            setBindings();
            loadInitialPage(); // 暫定的にDiigooのログインページを初期表示する
        });
        loadingBookmarkService.setOnFailed(workerStateEvent -> {
            loadingBookmarkService.getException().printStackTrace();
            dialog.showAlertText();
            dialog.setVisible(true);
        });
        indicatorBox.visibleProperty().bind(loadingBookmarkService.runningProperty());
        vailRegion.visibleProperty().bind(loadingBookmarkService.runningProperty());
        progressLabel.textProperty().bind(loadingBookmarkService.messageProperty());
        loadingBookmarkService.start();
    }

    private void setBindings() {
        webIndicator.visibleProperty().bind(webEngine.getLoadWorker().stateProperty().isEqualTo(Worker.State.RUNNING));
        backButton.disableProperty().bind(history.currentIndexProperty().isEqualTo(0));
        stopButton.disableProperty().bind(loadWorker.runningProperty().not());
    }

    private void addListeners() {
        // タグの検索処理
        searchBox.textProperty().addListener((observable, oldValue, newValue)
                -> bookmarkModel.selectTagsByKeyword(Optional.of(newValue)));

        // タグの選択処理
        final MultipleSelectionModel<String> tagSelectionModel = tagListView.getSelectionModel();
        tagSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        tagSelectionModel.getSelectedItems().addListener((ListChangeListener.Change<? extends String> change) -> {
            if (change != null) {
                bookmarkModel.selectedEntriesByTags(Optional.of(Collections.unmodifiableList(change.getList())));
            }
        });

        // ブックマークの選択処理
        final MultipleSelectionModel<BookmarkEntry> selectedBookmarks = bookmarkListView.getSelectionModel();
        selectedBookmarks.selectedItemProperty().addListener((property, oldEntry, newEntry) -> {
            if (newEntry != null) {
                loadWebContent(newEntry.urlProperty().get());
            }
        });

        // ブックマーク上でのタグリンククリックの監視
        bookmarkListView.addEventFilter(ActionEvent.ACTION, event -> {
           if (event.getTarget() instanceof Hyperlink) {
                handleTagLinkAction(event, tagSelectionModel);
           }
        });

        // WebView関連の表示状態管理
        history.currentIndexProperty().addListener((property, oldValue, newValue) ->
                forwardButton.setDisable(history.getCurrentIndex() + 1 == history.getEntries().size()));
        webEngine.locationProperty().addListener((observableValue, oldVal, newVal) -> locationBar.setText(newVal));
    }

    private void handleTagLinkAction(ActionEvent event, final MultipleSelectionModel<String> tagSelectionModel) {
        Hyperlink tagLink = (Hyperlink) event.getTarget();
        searchBox.setText("");
        tagSelectionModel.clearSelection();
        tagSelectionModel.select(tagLink.getText());
        tagListView.scrollTo(tagLink.getText());
    }

    private void loadWebContent(String url) {
        webEngine.load(url);
    }

    private void setListContent() {
        tagListView.setItems(bookmarkModel.getSelectedTags());
        bookmarkListView.setItems(bookmarkModel.getSelectedEntries());
    }

    /**
     * ブラウザの履歴を「戻る」.
     *
     * @param event イベント
     */
    @FXML
    void handleBackButtonAction(ActionEvent event) {
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
        }
    }

    /**
     * ブラウザの履歴を「進む」.
     *
     * @param event イベント
     */
    @FXML
    void handleForwardButtonAction(ActionEvent event) {
        if (history.getCurrentIndex() + 1 < history.getEntries().size()) {
            history.go(1);
        }
    }

    /**
     * ロケーションバーのキープレス処理. Enterで読み込み開始.
     *
     * @param event イベント
     */
    @FXML
    void handleLocationBarKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !locationBar.getText().isEmpty()) {
            loadWebContent(locationBar.getText());
        }
    }

    /**
     * ブラウザの読み込みを停止する.
     *
     * @param event イベント
     */
    @FXML
    void handleStopButtonAction(ActionEvent event) {
        loadWorker.cancel();
    }

    /**
     * Diigoletを起動する.
     *
     * @param event イベント
     */
    @FXML
    void handleDiigoButtonAction(ActionEvent event) {
        if (webEngine.getDocument() != null) {
            final Document document = webEngine.getDocument();
            final Element scriptElm = document.createElement("script");
            scriptElm.setAttribute("type", "text/javascript");
            scriptElm.setAttribute("src", DIIGOLET_URL);
            NodeList bodys = document.getElementsByTagName("body");
            if (bodys != null && bodys.getLength() > 0) {
                bodys.item(0).appendChild(scriptElm);
            }
        }
    }

    /**
     * Evernote Webクリッパーを起動する.
     *
     * @param event イベント
     */
    public void handleClipButtonAction(ActionEvent event) {
        if (webEngine.getDocument() != null) {
            webEngine.executeScript(EVERNOTE_CLIPPER_SCRIPT);
        }
    }

    private void loadInitialPage() {
        webEngine.load("https://www.diigo.com/sign-in?referInfo=https%3A%2F%2Fwww.diigo.com");
    }

}
