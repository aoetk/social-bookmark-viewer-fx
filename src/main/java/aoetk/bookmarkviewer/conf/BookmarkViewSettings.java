package aoetk.bookmarkviewer.conf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * ブックマークビューの状態を保持する.
 */
public class BookmarkViewSettings {

    BookmarkViewSettings() {
    }

    private DoubleProperty baseWidth = new SimpleDoubleProperty(this, "baseWidth", 1200.0);

    private DoubleProperty baseHeight = new SimpleDoubleProperty(this, "baseHeight", 700.0);

    private DoubleProperty leftDividerPos = new SimpleDoubleProperty(this, "leftDividerPos", 0.14);

    private DoubleProperty rightDividerPos = new SimpleDoubleProperty(this, "rightDividerPos", 0.35);

    private static final BookmarkViewSettings INSTANCE = new BookmarkViewSettings();

    /**
     * ビューのベース部分の幅.
     *
     * @return ビューのベース部分の幅
     */
    public DoubleProperty baseWidthProperty() {
        return baseWidth;
    }

    /**
     * ビューのベース部分の高さ.
     *
     * @return ビューのベース部分の高さ.
     */
    public DoubleProperty baseHeightProperty() {
        return baseHeight;
    }

    /**
     * 左のディバイダのポジション.
     *
     * @return 左のディバイダのポジション.
     */
    public DoubleProperty leftDividerPosProperty() {
        return leftDividerPos;
    }

    /**
     * 右のディバイダのポジション.
     *
     * @return 右のディバイダのポジション
     */
    public DoubleProperty rightDividerPosProperty() {
        return rightDividerPos;
    }

    /**
     * インスタンスを取得する.
     *
     * @return このクラスのインスタンス
     */
    public static BookmarkViewSettings getInstance() {
        return INSTANCE;
    }

}
