package aoetk.bookmarkviewer.service;

import aoetk.bookmarkviewer.model.BookmarkModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.ws.rs.core.Cookie;
import java.text.MessageFormat;
import java.util.List;

/**
 * バックグラウンドでブックマークを読み込むクラス.
 */
public class LoadingBookmarkService extends Service<BookmarkModel> {

    private BookmarkServiceClient serviceClient;

    private List<Cookie> loginCookies;

    /**
     * ブックマークWebサービスにアクセスするクライアントを渡して初期化する.
     *
     * @param serviceClient ブックマークWebサービスにアクセスするためのクライアント
     */
    public LoadingBookmarkService(BookmarkServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    protected Task<BookmarkModel> createTask() {
        return new Task<BookmarkModel>() {
            @Override
            protected BookmarkModel call() throws Exception {
                if (loginCookies == null) {
                    loginCookies = serviceClient.getLoginCookies();
                }
                final BookmarkModel bookmarkModel = new BookmarkModel(serviceClient);
                bookmarkModel.loadedCountProperty().addListener((observableValue, oldVal, newVal) -> {
                    updateMessage(MessageFormat.format("{0} 件読み込みました。", newVal));
                });
                bookmarkModel.loadFeed();
                return bookmarkModel;
            }
        };
    }

    /**
     * ブックマークWebサービスのログインに必要なCookieを取得する.
     *
     * @return ブックマークWebサービスのログインに必要なCookie
     */
    public List<Cookie> getLoginCookies() {
        return loginCookies;
    }

}
