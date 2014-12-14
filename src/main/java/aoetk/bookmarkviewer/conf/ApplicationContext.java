package aoetk.bookmarkviewer.conf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Screen;

/**
 * アプリケーションのコンテキスト.
 */
public class ApplicationContext {

    private static final int WINDOWS_DPI = 96;

    private static ApplicationContext ourInstance = new ApplicationContext();

    private double scaleFactor = -1;

    private DoubleProperty stageX = new SimpleDoubleProperty(this, "stageX", 0.0);

    private DoubleProperty stageY = new SimpleDoubleProperty(this, "stageY", 00.0);

    private BookmarkViewSettings bookmarkViewSettings = new BookmarkViewSettings();

    /**
     * インスタンスを取得する.
     *
     * @return このクラスのインスタンス
     */
    public static ApplicationContext getInstance() {
        return ourInstance;
    }

    private ApplicationContext() {
    }

    /**
     * 画面のスケール倍率を取得する.
     *
     * @return 画面のスケール倍率
     */
    public double getScaleFactor() {
        if (scaleFactor == -1) {
            Screen screen = Screen.getPrimary();
            if (screen != null) {
                if (System.getProperty("os.name").startsWith("Mac OS X")) {
                    scaleFactor = 1;
                } else {
                    scaleFactor = screen.getDpi() / WINDOWS_DPI;
                }
            }
        }
        return scaleFactor;
    }

    /**
     * Stageのx座標.
     *
     * @return Stageのx座標
     */
    public DoubleProperty stageXProperty() {
        return stageX;
    }

    /**
     * Stageのy座標.
     *
     * @return Stageのy座標
     */
    public DoubleProperty stageYProperty() {
        return stageY;
    }

    /**
     * ブックマークビューの設定を取得する.
     *
     * @return ブックマークビューの設定
     */
    public BookmarkViewSettings getBookmarkViewSettings() {
        return bookmarkViewSettings;
    }

}
