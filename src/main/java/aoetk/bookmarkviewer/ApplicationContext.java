package aoetk.bookmarkviewer;

import javafx.stage.Screen;

/**
 * アプリケーションのコンテキスト.
 */
public class ApplicationContext {

    private static final int WINDOWS_DPI = 96;

    private static ApplicationContext ourInstance = new ApplicationContext();

    private double scaleFactor = -1;

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

}
