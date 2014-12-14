package aoetk.bookmarkviewer.conf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Screen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * アプリケーションのコンテキスト.
 */
public class ApplicationContext {

    /** 設定ファイル名. */
    public static final String CONFIG_FILE_NAME = "app.properties";

    private static final int WINDOWS_DPI = 96;

    private static ApplicationContext ourInstance = new ApplicationContext();

    private double scaleFactor = -1;

    private DoubleProperty stageX = new SimpleDoubleProperty(this, "stageX", 0.0);

    private DoubleProperty stageY = new SimpleDoubleProperty(this, "stageY", 00.0);

    private BookmarkViewSettings bookmarkViewSettings = new BookmarkViewSettings();

    private Properties appProperties = new Properties();

    /**
     * インスタンスを取得する.
     *
     * @return このクラスのインスタンス
     */
    public static ApplicationContext getInstance() {
        return ourInstance;
    }

    private ApplicationContext() {
        loadConf();
    }

    private void loadConf() {
        assert appProperties != null;
        Path confFilePath = Paths.get(CONFIG_FILE_NAME);
        if (Files.exists(confFilePath)) {
            try (InputStream inputStream = Files.newInputStream(confFilePath)) {
                appProperties.load(inputStream);
                restoreSettings();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void restoreSettings() {
        assert appProperties != null;
        stageX.set(Double.parseDouble(appProperties.getProperty(stageX.getName())));
        stageY.set(Double.parseDouble(appProperties.getProperty(stageY.getName())));
        bookmarkViewSettings.baseWidthProperty().set(Double.parseDouble(
                appProperties.getProperty(bookmarkViewSettings.baseWidthProperty().getName())));
        bookmarkViewSettings.baseHeightProperty().set(Double.parseDouble(
                appProperties.getProperty(bookmarkViewSettings.baseHeightProperty().getName())));
        bookmarkViewSettings.leftDividerPosProperty().set(Double.parseDouble(
                appProperties.getProperty(bookmarkViewSettings.leftDividerPosProperty().getName())));
        bookmarkViewSettings.rightDividerPosProperty().set(Double.parseDouble(
                appProperties.getProperty(bookmarkViewSettings.rightDividerPosProperty().getName())));
    }

    /**
     * 設定類をセーブする.
     */
    public void saveConf() {
        assert appProperties != null;
        Path confFilePath = Paths.get(CONFIG_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(confFilePath, CREATE, TRUNCATE_EXISTING, WRITE)) {
            appProperties.setProperty(stageX.getName(), String.valueOf(stageX.get()));
            appProperties.setProperty(stageY.getName(), String.valueOf(stageY.get()));
            appProperties.setProperty(bookmarkViewSettings.baseWidthProperty().getName(),
                    String.valueOf(bookmarkViewSettings.baseWidthProperty().get()));
            appProperties.setProperty(bookmarkViewSettings.baseHeightProperty().getName(),
                    String.valueOf(bookmarkViewSettings.baseHeightProperty().get()));
            appProperties.setProperty(bookmarkViewSettings.leftDividerPosProperty().getName(),
                    String.valueOf(bookmarkViewSettings.leftDividerPosProperty().get()));
            appProperties.setProperty(bookmarkViewSettings.rightDividerPosProperty().getName(),
                    String.valueOf(bookmarkViewSettings.rightDividerPosProperty().get()));
            appProperties.store(writer, "Settings for Social Bookmark Viewer FX");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
