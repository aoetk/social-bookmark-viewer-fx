package aoetk.bookmarkviewer.service;

import aoetk.bookmarkviewer.model.BookmarkEntry;
import javafx.beans.property.ReadOnlyIntegerProperty;

import javax.ws.rs.core.Cookie;
import java.util.List;

/**
 * ブックマークWebサービスにアクセスするクライアントのインターフェース.
 */
public interface BookmarkServiceClient {

    /**
     * ブックマークエントリを取得する.
     * このメソッドは取得が完了するまでスレッドをブロックする.
     *
     * @return ブックマークエントリ
     */
    public List<BookmarkEntry> getEntries();

    /**
     * ブックマークエントリの読み込み数を取得するプロパティ.
     * 進捗状況表示に利用する.
     *
     * @return ブックマークエントリの読み込み数
     */
    public ReadOnlyIntegerProperty loadedCountProperty();

    /**
     * ブックマークWebサービスのログインに必要なCookieを取得する.
     *
     * @return ブックマークWebサービスのログインに必要なCookie
     */
    public List<Cookie> getLoginCookies();

}
