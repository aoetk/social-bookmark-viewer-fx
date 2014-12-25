package aoetk.bookmarkviewer.model;

import aoetk.bookmarkviewer.service.BookmarkServiceClient;
import javafx.collections.ObservableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link aoetk.bookmarkviewer.model.BookmarkModel}のテスト.
 */
public class BookmarkModelTest {

    private BookmarkModel sut;

    @Before
    public void setUp() throws Exception {
        sut = new BookmarkModel(createServiceClientMock());
        sut.loadFeed();
    }

    @Test
    public void testLoadFeed_extract_tags() throws Exception {
        assertThat(sut.getEntries().size(), is(3));
        assertThat(sut.getSelectedEntries().size(), is(3));
        assertThat(sut.getTags().size(), is(5));
        assertThat(sut.getSelectedTags().size(), is(5));
    }

    @Test
    public void testLoadFeed_index() throws Exception {
        ObservableMap<String, Set<BookmarkEntry>> result = sut.getEntryMap();
        assertThat(result.size(), is(5));
        assertThat(result.get("foo").size(), is(2));
        assertThat(result.get("bar").size(), is(2));
        assertThat(result.get("baz").size(), is(1));
    }

    @Test
    public void testSelectTagsByKeyword() throws Exception {
        sut.selectTagsByKeyword(Optional.of("ba"));
        assertThat(sut.getSelectedTags().size(), is(2));
    }

    @Test
    public void testSelectTagsByKeyword_empty_word() throws Exception {
        sut.selectTagsByKeyword(Optional.of(""));
        assertThat(sut.getSelectedTags().size(), is(5));
    }

    @Test
    public void testSelectedEntriesByTags() throws Exception {
        sut.selectedEntriesByTags(Optional.of(Arrays.asList("foo", "bar")));
        assertThat(sut.getSelectedEntries().size(), is(1));
    }

    @Test
    public void testSelectedEntriesByTags_empty_tags() throws Exception {
        sut.selectedEntriesByTags(Optional.of(Collections.emptyList()));
        assertThat(sut.getSelectedEntries().size(), is(3));
    }

    private BookmarkServiceClient createServiceClientMock() {
        BookmarkServiceClient client = mock(BookmarkServiceClient.class);
        when(client.getEntries()).thenReturn(createEntries());
        return client;
    }

    private List<BookmarkEntry> createEntries() {
        SortedSet<String> tags0 = new TreeSet<>();
        tags0.addAll(Arrays.asList("foo", "bar"));
        BookmarkEntry entry0 = new BookmarkEntry("title0", "url0", "cooment0", tags0, 0L);
        SortedSet<String> tags1 = new TreeSet<>();
        tags1.addAll(Arrays.asList("foo", "hoge", "fuga"));
        BookmarkEntry entry1 = new BookmarkEntry("title1", "url1", "comment1", tags1, 1L);
        SortedSet<String> tags2 = new TreeSet<>();
        tags2.addAll(Arrays.asList("bar", "baz"));
        BookmarkEntry entry2 = new BookmarkEntry("title2", "url2", "comment2", tags2, 2L);
        return Arrays.asList(entry0, entry1, entry2);
    }

}
