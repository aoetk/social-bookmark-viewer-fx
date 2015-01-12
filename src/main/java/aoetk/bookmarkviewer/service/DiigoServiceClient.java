package aoetk.bookmarkviewer.service;

import aoetk.bookmarkviewer.model.BookmarkEntry;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.codehaus.jackson.annotate.JsonProperty;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ブックマークWebサービスとしてDiigoを使うクライアント.
 */
public class DiigoServiceClient extends WebServiceClientBase implements BookmarkServiceClient {

    /** DiigoのWeb APIのエンドポイントURL. */
    public static final String DIIGO_API_URL = "https://secure.diigo.com/api/v2/bookmarks";

    /** DiigoのWebページにログインするためのURL. */
    public static final String DIIGO_LOGIN_URL = "https://www.diigo.com/sign-in";

    private String userName;

    private String password;

    private ReadOnlyIntegerWrapper loadedCount = new ReadOnlyIntegerWrapper();

    /**
     * コンストラクタ.
     *
     * @param userName Diigoのユーザー名
     * @param password Diigoのパスワード
     */
    public DiigoServiceClient(String userName, String password) {
        super();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userName, password);
        client.register(feature).register(FilterForDiigo.class);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public List<BookmarkEntry> getEntries() {
        List<BookmarkEntry> result = new ArrayList<>();
        try {
            loadEntry(0, result);
        } catch (ResponseProcessingException | WebApplicationException e) {
            // TODO あとでちゃんとした例外クラスを作る
            throw new IllegalStateException(e);
        }

        return result;
    }

    @Override
    public ReadOnlyIntegerProperty loadedCountProperty() {
        return loadedCount.getReadOnlyProperty();
    }

    /**
     * DiigoのログインURLに対してPOSTを行い, Cookieを取得する.
     *
     * @return ログインに必要なCookie
     */
    @Override
    public List<Cookie> getLoginCookies() {
        MultivaluedMap<String, String> postData = new MultivaluedHashMap<>();
        postData.putSingle("username", userName);
        postData.putSingle("password", password);
        postData.putSingle("referInfo", "https://www.diigo.com");
        Response response = client.target(DIIGO_LOGIN_URL)
                .request(MediaType.TEXT_HTML_TYPE)
                .post(Entity.form(postData));
        Map<String, NewCookie> responseCookies = response.getCookies();
        return responseCookies.entrySet().stream()
                .filter(item -> "CHKIO".equals(item.getKey()) || "diigoandlogincookie".equals(item.getKey()))
                .map(item -> new Cookie(item.getKey(), item.getValue().getValue()))
                .collect(Collectors.toList());
    }

    private void loadEntry(int startIndex, List<BookmarkEntry> result) {
        List<BookmarkDto> bookmarks = client.target(DIIGO_API_URL)
                .queryParam("key", getApiKey()).queryParam("filter", "all").queryParam("user", userName)
                .queryParam("count", 100).queryParam("start", startIndex)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<BookmarkDto>>() {
                });
        for (BookmarkDto bookmark : bookmarks) {
            Set<String> tags = new HashSet<>();
            Collections.addAll(tags, bookmark.getTags().split(","));
            result.add(new BookmarkEntry(bookmark.getTitle(), bookmark.getUrl(), bookmark.getDesc(), tags,
                    bookmark.getCreatedAtAsLong()));
        }
        loadedCount.set(result.size());
        if (bookmarks.size() > 0) {
            loadEntry(startIndex + 100, result);
        }
    }

    private String getApiKey() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/conf/key.properties"));
            return properties.getProperty("diigo.api.key");
        } catch (IOException e) {
            throw new IllegalStateException("key.properties was not found.", e);
        }
    }

    /**
     * Webサービスから取得したブックマーク情報を格納するDTO.
     */
    public static class BookmarkDto {
        private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss Z");

        private String title;
        private String url;
        private String user;
        private String desc;
        private String tags;
        private String shared;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
        private List<String> comments;
        private List<Map<String, Object>> annotations;
        private String readlater;

        /**
         * コンストラクタ.
         */
        public BookmarkDto() {
        }

        /**
         * コンストラクタ.
         *
         * @param title タイトル
         * @param url URL
         * @param user ユーザー
         * @param desc ブックマークの説明
         * @param tags タグ (カンマ区切り)
         * @param shared 共有の有無
         * @param createdAt 作成日時
         * @param updatedAt 更新日時
         * @param comments コメントのリスト
         * @param annotations 注釈のリスト
         * @param readlater 後で読むフラグ
         */
        public BookmarkDto(String title, String url, String user, String desc, String tags, String shared,
                    String createdAt, String updatedAt, List<String> comments, List<Map<String, Object>> annotations,
                String readlater) {
            this.title = title;
            this.url = url;
            this.user = user;
            this.desc = desc;
            this.tags = tags;
            this.shared = shared;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.comments = comments;
            this.annotations = annotations;
            this.readlater = readlater;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getShared() {
            return shared;
        }

        public void setShared(String shared) {
            this.shared = shared;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<String> getComments() {
            return comments;
        }

        public void setComments(List<String> comments) {
            this.comments = comments;
        }

        public List<Map<String, Object>> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(List<Map<String, Object>> annotations) {
            this.annotations = annotations;
        }

        public String getReadlater() {
            return readlater;
        }

        public void setReadlater(String readlater) {
            this.readlater = readlater;
        }

        public long getCreatedAtAsLong() {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(createdAt, DATE_PATTERN);
            return zonedDateTime.toInstant().getEpochSecond();
        }

        public long getUpdatedAtAsLong() {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(updatedAt, DATE_PATTERN);
            return zonedDateTime.toInstant().getEpochSecond();
        }
    }

}
