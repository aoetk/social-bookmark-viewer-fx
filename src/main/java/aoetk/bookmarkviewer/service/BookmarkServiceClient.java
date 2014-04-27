package aoetk.bookmarkviewer.service;

import aoetk.bookmarkviewer.model.BookmarkEntry;

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

}
