package aoetk.bookmarkviewer.conf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * アプリケーションのコンテキスト.
 */
public class ApplicationContext {

    private static ApplicationContext ourInstance = new ApplicationContext();

    private DoubleProperty stageX = new SimpleDoubleProperty(this, "stageX", 0.0);

    private DoubleProperty stageY = new SimpleDoubleProperty(this, "stageY", 0.0);

    private BookmarkViewSettings bookmarkViewSettings = new BookmarkViewSettings();

    private Preferences preferences;

    /**
     * インスタンスを取得する.
     *
     * @return このクラスのインスタンス
     */
    public static ApplicationContext getInstance() {
        return ourInstance;
    }

    private ApplicationContext() {
        preferences = Preferences.userNodeForPackage(ApplicationContext.class);
        restoreSettings();
    }

    private void restoreSettings() {
        assert preferences != null;
        stageX.set(preferences.getDouble(stageX.getName(), stageX.get()));
        stageY.set(preferences.getDouble(stageY.getName(), stageY.get()));
        DoubleProperty baseWidth = bookmarkViewSettings.baseWidthProperty();
        DoubleProperty baseHeight = bookmarkViewSettings.baseHeightProperty();
        DoubleProperty leftDividerPos = bookmarkViewSettings.leftDividerPosProperty();
        DoubleProperty rightDividerPos = bookmarkViewSettings.rightDividerPosProperty();
        baseWidth.set(preferences.getDouble(baseWidth.getName(), baseWidth.get()));
        baseHeight.set(preferences.getDouble(baseHeight.getName(), baseHeight.get()));
        leftDividerPos.set(preferences.getDouble(leftDividerPos.getName(), leftDividerPos.get()));
        rightDividerPos.setValue(preferences.getDouble(rightDividerPos.getName(), rightDividerPos.get()));
    }

    /**
     * 設定類をセーブする.
     */
    public void saveConf() {
        assert preferences != null;
        try {
            preferences.putDouble(stageX.getName(), stageX.get());
            preferences.putDouble(stageY.getName(), stageY.get());
            DoubleProperty baseWidth = bookmarkViewSettings.baseWidthProperty();
            DoubleProperty baseHeight = bookmarkViewSettings.baseHeightProperty();
            DoubleProperty leftDividerPos = bookmarkViewSettings.leftDividerPosProperty();
            DoubleProperty rightDividerPos = bookmarkViewSettings.rightDividerPosProperty();
            preferences.putDouble(baseWidth.getName(), baseWidth.get());
            preferences.putDouble(baseHeight.getName(), baseHeight.get());
            preferences.putDouble(leftDividerPos.getName(), leftDividerPos.get());
            preferences.putDouble(rightDividerPos.getName(), rightDividerPos.get());
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
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
