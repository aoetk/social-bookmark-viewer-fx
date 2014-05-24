package aoetk.bookmarkviewer.view;

import aoetk.bookmarkviewer.model.BookmarkEntry;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
    }

    private void initComponent() {
        cellContainer = new VBox(5);
        cellContainer.getStylesheets().add("/styles/Styles.css");
        txtTitle = new Text();
        txtTitle.getStyleClass().add("bookmark-title");
        VBox.setVgrow(txtTitle, Priority.NEVER);
        txtComment = new Text();
        txtComment.getStyleClass().add("bookmark-comment");
        VBox.setVgrow(txtComment, Priority.ALWAYS);
        tagContainer = new HBox();
        VBox.setVgrow(tagContainer, Priority.NEVER);
        cellContainer.getChildren().addAll(txtTitle, txtComment, tagContainer);
    }

    @Override
    protected void updateItem(BookmarkEntry bookmarkEntry, boolean empty) {
        super.updateItem(bookmarkEntry, empty);
        if (!bound) {
            txtTitle.wrappingWidthProperty().bind(getListView().widthProperty().subtract(35));
            txtComment.wrappingWidthProperty().bind(getListView().widthProperty().subtract(35));
            bound = true;
        }
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            cellContainer.getChildren().clear();
            txtTitle.setText(bookmarkEntry.titleProperty().get());
            txtComment.setText(bookmarkEntry.commentProperty().get());
            tagContainer.getChildren().clear();
            List<Node> tagLinks = bookmarkEntry.getTags().stream()
                    .map(this::createTagLink).collect(Collectors.toList());
            tagContainer.getChildren().addAll(tagLinks);
            if (txtComment.getText().isEmpty()) {
                cellContainer.getChildren().addAll(txtTitle, tagContainer);
            } else {
                cellContainer.getChildren().addAll(txtTitle, txtComment, tagContainer);
            }
            setGraphic(cellContainer);
        }
    }

    private Node createTagLink(String tagName) {
        return new Hyperlink(tagName);
    }

}
