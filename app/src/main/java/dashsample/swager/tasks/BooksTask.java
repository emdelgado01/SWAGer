package dashsample.swager.tasks;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import dashsample.swager.R;
import dashsample.swager.common.DateTimeUtils;
import dashsample.swager.common.Global;
import dashsample.swager.models.books.Book;
import dashsample.swager.models.storage.Session;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Eric-Local on 8/21/2014.
 */
public class BooksTask extends Fragment {
    public static final String TAG = "BooksTask";
    private Callbacks mCallbacks;


    public interface Callbacks {
        public void onBooksLoaded(List<Book> books);

        public void onBookCreated(Book book);

        public void onBookEdited(Book book);

        public void onBookDeleted();

        public void onBooksError(String message);

        public void onCreateError(String message);
    }

    public static BooksTask newInstance(Callbacks callbacks) {
        BooksTask fragment = new BooksTask();
        fragment.mCallbacks = callbacks;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallbacks = null;

    }

    public Book loadBook(int position) {
        return Session.getSavedBooks().get(position);
    }

    public void loadBooks(boolean useCache) {
        if (useCache && Session.getSavedBooks() != null) {
            safeLoadedCallback(Session.getSavedBooks());
        } else {
            Book.getBooks(new Callback<List<Book>>() {
                @Override
                public void success(final List<Book> books, Response response) {
                    Session.setSavedBooks(books);
                    safeLoadedCallback(Session.getSavedBooks());

                }

                @Override
                public void failure(RetrofitError error) {
                    if (isAdded()) {
                        Global.showToast(getActivity(), true, R.string.error_books_load);
                        safeErrorCallback(null);
                    }
                }
            });
        }
    }

    public void createBook(Book book) {
        Global.showToast(getActivity(), true, R.string.creating_book);
        book.createBook(new Callback<Book>() {
            @Override
            public void success(Book book, Response response) {
                //book.setPosition(Session.getSavedBooks().size());
                Global.showToast(getActivity(), true, R.string.book_created);
                Session.addBook(book);
                safeBookCreatedCallback(book);
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    Global.showToast(getActivity(), true, R.string.error_book_create);
                    safeErrorCreateCallback(null);
                }
            }
        });
    }

    public void editBook(final Book book) {
        Global.showToast(getActivity(), true, R.string.updating_book);
        book.editBook(new Callback<Book>() {
            @Override
            public void success(Book updatedBook, Response response) {
                //int position = book.getPosition();
                Session.updateBook(updatedBook);
                Global.showToast(getActivity(), true, R.string.book_updated);
                safeBookEditedCallback(updatedBook);
            }

            @Override
            public void failure(RetrofitError error) {
                Global.showToast(getActivity(), true, R.string.error_book_edit);

            }
        });
    }

    public void checkoutBooks(final String name, final SparseArray<Book> books) {
        Global.showToast(getActivity(), true, R.string.checking_out_book);
        for (int x = 0; x < books.size(); x++) {
            checkoutBook(name, books.valueAt(x));
        }
    }

    public void checkoutBook(final String name, final Book book) {
        Global.showToast(getActivity(), true, R.string.checking_out_book);
        book.setLastCheckedOutBy(name);
        book.setLastCheckedOut(DateTimeUtils.getCurrentDateTimeInServerFormat());
        book.editBook(new Callback<Book>() {
            @Override
            public void success(Book book, Response response) {
                Global.showToast(getActivity(), true, R.string.book_checked_out);
                Session.updateBook(book);
                safeBookEditedCallback(book);
            }

            @Override
            public void failure(RetrofitError error) {
                Global.showToast(getActivity(), true, R.string.error_book_checkout);
            }
        });
    }

    public void deleteBook(final Book book) {
        Global.showToast(getActivity(), true, R.string.deleting_book);
        book.deleteBook(new Callback<Book>() {
            @Override
            public void success(Book book1, Response response) {
                Session.removeBook(book.getId());
                safeDeleteCallback();
                Global.showToast(getActivity(), true, R.string.book_deleted);
            }

            @Override
            public void failure(RetrofitError error) {
                Global.showToast(getActivity(), true, R.string.error_books_delete);
            }
        });
    }

    public void deleteBooks(final SparseArray<Book> books) {
        for (int x = 0; x < books.size(); x++) {
            deleteBook(books.valueAt(x));
        }
    }

    public void deleteAllBooks() {
        Book.deleteAllBooks(new Callback() {
            @Override
            public void success(Object o, Response response) {
                Session.setSavedBooks(new ArrayList<Book>());
                Global.showToast(getActivity(), true, R.string.book_deleted);
                safeDeleteCallback();
            }

            @Override
            public void failure(RetrofitError error) {
                Global.showToast(getActivity(), true, R.string.error_books_delete);
            }
        });
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    private void safeLoadedCallback(List<Book> books) {
        if (mCallbacks != null)
            mCallbacks.onBooksLoaded(books);
    }

    private void safeErrorCallback(String message) {
        if (mCallbacks != null)
            mCallbacks.onBooksError(message);
    }

    private void safeErrorCreateCallback(String message) {
        if (mCallbacks != null)
            mCallbacks.onCreateError(message);
    }

    private void safeDeleteCallback() {
        if (mCallbacks != null)
            mCallbacks.onBookDeleted();
    }

    private void safeBookCreatedCallback(Book book) {
        if (mCallbacks != null)
            mCallbacks.onBookCreated(book);
    }

    private void safeBookEditedCallback(Book book) {
        if (mCallbacks != null)
            mCallbacks.onBookEdited(book);
    }
}
