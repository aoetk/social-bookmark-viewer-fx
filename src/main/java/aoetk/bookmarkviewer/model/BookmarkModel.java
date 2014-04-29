package aoetk.bookmarkviewer.model;

import aoetk.bookmarkviewer.service.BookmarkServiceClient;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ブックマーク全体を表すモデル.
 */
public class BookmarkModel {

    private final BookmarkServiceClient serviceClient;

    private final SortedSet<String> tags = new TreeSet<>();

    private final List<BookmarkEntry> entries = new ArrayList<>();

    private final ObservableMap<String, Set<BookmarkEntry>> entryMap = FXCollections.observableHashMap();

    private final ObservableList<String> selectedTags = FXCollections.observableArrayList();

    private final ObservableList<BookmarkEntry> selectedEntries = FXCollections.observableArrayList();

    public SortedSet<String> getTags() {
        return tags;
    }

    public List<BookmarkEntry> getEntries() {
        return entries;
    }

    public ObservableMap<String, Set<BookmarkEntry>> getEntryMap() {
        return entryMap;
    }

    public ObservableList<String> getSelectedTags() {
        return selectedTags;
    }

    public ObservableList<BookmarkEntry> getSelectedEntries() {
        return selectedEntries;
    }

    /**
     * コンストラクタ.
     *
     * @param serviceClient Webサービスクライアント
     */
    public BookmarkModel(BookmarkServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * ブックマークエントリの読み込み数を取得するプロパティ.
     * 進捗状況表示に利用する.
     *
     * @return ブックマークエントリの読み込み数
     */
    public ReadOnlyIntegerProperty loadedCountProperty() {
        return serviceClient.loadedCountProperty();
    }

    /**
     * ブックマークを読み込む.
     *
     * @throws IOException Webサービスへの接続に失敗
     */
    public void loadFeed() throws IOException {
        try {
            final List<BookmarkEntry> loadedEntries = serviceClient.getEntries();
            entries.addAll(loadedEntries);
            selectedEntries.addAll(loadedEntries);
            extractTags(loadedEntries);
            createIndex(loadedEntries);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * タグの選択を絞り込む.
     * キーワードが空文字の場合は全要素を選択する.
     *
     * @param keyword 絞り込みキーワード (部分一致)
     */
    public void selectTagsByKeyword(Optional<String> keyword) {
        selectedTags.clear();
        List<String> matchedTags = keyword.filter(word -> !word.isEmpty())
                .map(word -> {
                    String pattern = ".*" + word.toLowerCase() + ".*";
                    return tags.stream().filter(tag -> tag.toLowerCase().matches(pattern))
                            .collect(Collectors.toList());
                })
                .orElseGet(() -> new ArrayList<>(tags));
        selectedTags.addAll(matchedTags);
    }

    /**
     * 選択されたタグの配列でエントリを絞り込む.
     * 配列が空の場合は絞り込まない.
     *
     * @param tags タグの配列
     */
    public void selectedEntriesByTags(Optional<List<String>> tags) {
        selectedEntries.clear();
        List<BookmarkEntry> matchedEntries = tags.filter(items -> !items.isEmpty())
                .map(items -> entryMap.keySet().stream().filter(items::contains)
                        .flatMap(key -> entryMap.get(key).stream())
                        .distinct()
                        // 降順ソートする
                        // 作成時刻の差がintの範囲を超えるとおかしくなるけど、まあそれはそれで
                        .sorted((o1, o2) -> (int) (o2.getCreatedAt() - o1.getCreatedAt()))
                        .collect(Collectors.toList()))
                .orElseGet(() -> entries);
        selectedEntries.addAll(matchedEntries);
    }

    private void createIndex(List<BookmarkEntry> loadedEntries) {
        Map<String, Set<BookmarkEntry>> entryByTag = loadedEntries.stream()
                .flatMap((entry) -> entry.getTags().stream().map(tag -> new Pair<>(tag, entry)))
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toSet())));
        entryMap.putAll(entryByTag);
    }

    private void extractTags(List<BookmarkEntry> loadedEntries) {
        Set<String> loadedTags = loadedEntries.stream().flatMap((entry) -> entry.getTags().stream())
                .collect(Collectors.toSet());
        tags.addAll(loadedTags);
        selectedTags.addAll(tags);
    }

}
