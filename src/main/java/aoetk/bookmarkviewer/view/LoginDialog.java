package aoetk.bookmarkviewer.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * ログインダイアログコンポーネント.
 */
public class LoginDialog extends BorderPane implements Initializable {

    @FXML
    Label alertText;

    @FXML
    TextField userField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    private EventHandler<ActionEvent> onAction;

    private boolean init = false;

    /**
     * コンストラクタ.
     */
    public LoginDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginDialogView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load FXML.", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.disableProperty().bind(
                userField.textProperty().isEqualTo("").or(
                        passwordField.textProperty().isEqualTo("")));
        loginButton.setOnAction(actionEvent -> {
            alertText.setVisible(false);
            if (onAction != null) {
                onAction.handle(actionEvent);
            }
        });
    }

    /**
     * ダイアログのボタンアクションのイベントハンドラを設定する.
     *
     * @param eventHandler ダイアログのボタンアクションのイベントハンドラ
     */
    public void setOnAction(EventHandler<ActionEvent> eventHandler) {
        this.onAction = eventHandler;
    }

    /**
     * ダイアログに入力されたユーザー名を取得する.
     *
     * @return ダイアログに入力されたユーザー名
     */
    public String getUser() {
        return userField.getText();
    }

    /**
     * ダイアログに入力されたパスワードを取得する.
     *
     * @return ダイアログに入力されたパスワード
     */
    public String getPassword() {
        return passwordField.getText();
    }

    /**
     * 警告メッセージを表示する.
     */
    public void showAlertText() {
        alertText.setVisible(true);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (!init) {
            userField.requestFocus();
            init = true;
        }
    }

}
