package dashsample.swager.models.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/23/2014.
 */

/*
In real world, a Content Provider backed by a sqlite db would be used.  In memory list is used here for simplicity
 */
public final class Session {

    private static List<Book> mBooks;
    private static Map<String, Integer> mBookIndex;

    public static List<Book> getSavedBooks() {
        return mBooks;
    }

    public static void setSavedBooks(List<Book> books) {
        mBooks = books;
        rebuildIndex();
    }

    private static void rebuildIndex() {
        mBookIndex = new HashMap<String, Integer>(mBooks.size());
        for (int x = 0; x < mBooks.size(); x++) {
            mBookIndex.put(mBooks.get(x).getId(), x);
        }
    }

    public static Book getBook(String id) {
        return mBooks.get(mBookIndex.get(id));
    }

    public static Book getBook(int position) {
        return mBooks.get(position);
    }

    public static int getBookPosition(String id) {
        return mBookIndex.get(id);
    }

    public static void addBook(Book book) {
        mBookIndex.put(book.getId(), mBooks.size());
        mBooks.add(book);
    }

    public static void updateBook(Book book) {
        mBooks.set(getBookPosition(book.getId()), book);
    }

    public static void removeBook(String id) {
        mBooks.remove(((int) mBookIndex.get(id)));
        rebuildIndex();
    }

}
