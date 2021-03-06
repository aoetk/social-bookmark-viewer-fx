package aoetk.bookmarkviewer;

import aoetk.bookmarkviewer.conf.ApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The application class.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BookmarkView.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Social Bookmark Viewer FX");
        stage.setScene(scene);

        final ApplicationContext applicationContext = ApplicationContext.getInstance();
        stage.setX(applicationContext.stageXProperty().get());
        stage.setY(applicationContext.stageYProperty().get());
        applicationContext.stageXProperty().bind(stage.xProperty());
        applicationContext.stageYProperty().bind(stage.yProperty());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                applicationContext.saveConf();
            }
        });
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
