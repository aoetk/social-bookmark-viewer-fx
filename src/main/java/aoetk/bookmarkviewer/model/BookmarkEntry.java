package aoetk.bookmarkviewer.model;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * ブックマークエントリ.
 */
public class BookmarkEntry {

    private final StringProperty url = new SimpleStringProperty(this, "url");

    private final StringProperty title = new SimpleStringProperty(this, "title");

    private final StringProperty comment = new SimpleStringProperty(this, "comment", "");

    private final SortedSet<String> tags = new TreeSet<>();

    private long createdAt;

    /**
     * コンストラクタ.
     *
     * @param title ブックマークタイトル (必須)
     * @param url ブックマークURL (必須)
     * @param comment ブックマークコメント
     * @param tags タグのリスト
     * @param createdAt ブックマークの作成時刻
     * @throws NullPointerException タイトルもしくはURLが{@code null}
     */
    public BookmarkEntry(String title, String url, String comment, Set<String> tags, long createdAt) {
        Objects.requireNonNull(title, "title must not be null.");
        Objects.requireNonNull(url, "url must not be null.");
        this.title.set(title);
        this.url.set(url);
        if (comment != null) {
            this.comment.set(comment);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.createdAt = createdAt;
    }

    /**
     * ブックマークURL.
     *
     * @return ブックマークURL
     */
    public StringProperty urlProperty() {
        return url;
    }

    /**
     * ブックマークタイトル.
     *
     * @return ブックマークタイトル
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * ブックマークコメント.
     *
     * @return ブックマークコメント
     */
    public StringProperty commentProperty() {
        return comment;
    }

    /**
     * タグの一覧を取得する.
     *
     * @return タグの一覧
     */
    public SortedSet<String> getTags() {
        return tags;
    }

    /**
     * ブックマークの作成時刻を取得する.
     *
     * @return ブックマークの作成時刻 
     */
    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookmarkEntry that = (BookmarkEntry) o;

        if (!comment.get().equals(that.comment.get())) {
            return false;
        }
        if (!tags.equals(that.tags)) {
            return false;
        }
        if (!title.get().equals(that.title.get())) {
            return false;
        }
        return url.get().equals(that.url.get());
    }

    @Override
    public int hashCode() {
        int result = url.get().hashCode();
        result = 31 * result + title.get().hashCode();
        result = 31 * result + comment.get().hashCode();
        result = 31 * result + tags.hashCode();
        return result;
    }

}
