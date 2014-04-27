package aoetk.bookmarkviewer;

import javafx.stage.Screen;

/**
 * アプリケーションのコンテキスト.
 */
public class ApplicationContext {

    private static final int WINDOWS_DPI = 96;

    private static final int MAC_DPI = 72;

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
                double screenDpi = screen.getDpi();
                String osName = System.getProperty("os.name");
                scaleFactor = osName.startsWith("Mac OS X") ? screenDpi / MAC_DPI : screenDpi / WINDOWS_DPI;
            }
        }
        return scaleFactor;
    }

}
