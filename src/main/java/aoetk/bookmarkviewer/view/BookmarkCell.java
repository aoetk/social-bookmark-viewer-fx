package aoetk.bookmarkviewer.view;

import aoetk.bookmarkviewer.model.BookmarkEntry;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ブックマークを表示するセル.
 */
public class BookmarkCell extends ListCell<BookmarkEntry> {

    private VBox cellContainer;

    private Text txtTitle;

    private Text txtComment;

    private HBox tagContainer;

    private boolean bound = false;

    /**
     * コンストラクタ.
     * 初期化時に表示するNodeのインスタンスを生成しておく.
     */
    public BookmarkCell() {
        initComponent();
        initStyle();
    }

    private void initStyle() {
        txtTitle.setFont(new Font("System Bold", 16.0));
    }

    private void initComponent() {
        cellContainer = new VBox(5);
        cellContainer.getStylesheets().add("/styles/Styles.css");
        txtTitle = new Text();
        VBox.setVgrow(txtTitle, Priority.NEVER);
        txtComment = new Text();
        VBox.setVgrow(txtComment, Priority.ALWAYS);
        tagContainer = new HBox();
        VBox.setVgrow(tagContainer, Priority.NEVER);
        cellContainer.getChildren().addAll(txtTitle, txtComment, tagContainer);
    }

    @Override
    protected void updateItem(BookmarkEntry bookmarkEntry, boolean empty) {
        super.updateItem(bookmarkEntry, empty);
        if (!bound) {
            txtTitle.wrappingWidthProperty().bind(getListView().widthProperty().subtract(25));
            txtComment.wrappingWidthProperty().bind(getListView().widthProperty().subtract(25));
            bound = true;
        }
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            txtTitle.setText(bookmarkEntry.titleProperty().get());
            txtComment.setText(bookmarkEntry.commentProperty().get());
            tagContainer.getChildren().clear();
            List<Node> tagLinks = bookmarkEntry.getTags().stream()
                    .map(tagName -> createTagLink(tagName)).collect(Collectors.toList());
            tagContainer.getChildren().addAll(tagLinks);
            setGraphic(cellContainer);
        }
    }

    private Node createTagLink(String tagName) {
        Hyperlink hyperlink = new Hyperlink(tagName);
        return hyperlink;
    }

}
