package dashsample.swager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import dashsample.swager.R;
import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/23/2014.
 */
public class BookFieldsFragment extends Fragment {
    public static final String TAG = "BookFieldsFragment";
    public static final String ARG_BOOK = "ARG_BOOK";

    private Book mBook;
    private EditText mTitle;
    private EditText mAuthor;
    private EditText mPublisher;
    private EditText mCategories;

    public static BookFieldsFragment newInstance() {
        return newInstance(null);
    }

    public static BookFieldsFragment newInstance(Book book) {
        BookFieldsFragment fragment = new BookFieldsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_book_fields, container, false);
        mTitle = (EditText) layout.findViewById(R.id.book_title);
        mAuthor = (EditText) layout.findViewById(R.id.book_author);
        mPublisher = (EditText) layout.findViewById(R.id.book_publisher);
        mCategories = (EditText) layout.findViewById(R.id.book_categories);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_BOOK))
                mBook = args.getParcelable(ARG_BOOK);
        } else {
            mBook = savedInstanceState.getParcelable(ARG_BOOK);
        }

        if (mBook != null) {
            mTitle.setText(mBook.getTitle());
            mAuthor.setText(mBook.getAuthor());
            mPublisher.setText(mBook.getPublisher());
            mCategories.setText(mBook.getCategories());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_BOOK, mBook);
    }

    public Book getBook() {
        boolean valid = true;
        mTitle.setError(null);
        mAuthor.setError(null);
        mPublisher.setError(null);
        mCategories.setError(null);

        String title = mTitle.getText().toString();
        String author = mAuthor.getText().toString();
        String publisher = mPublisher.getText().toString();
        String categories = mCategories.getText().toString();

        if (TextUtils.isEmpty(title)) {
            valid = false;
            mTitle.setError(getString(R.string.required));
        }

        if (TextUtils.isEmpty(author)) {
            valid = false;
            mAuthor.setError(getString(R.string.required));
        }

        if (TextUtils.isEmpty(publisher)) {
            valid = false;
            mPublisher.setError(getString(R.string.required));
        }

        if (TextUtils.isEmpty(categories)) {
            valid = false;
            mCategories.setError(getString(R.string.required));
        }

        if (valid) {
            if (mBook == null) {
                return new Book(null, author, categories, null, null, publisher, title, null);

            } else {
                return new Book(mBook.getId(), author, categories, mBook.getLastCheckedOut(), mBook.getLastCheckedOutBy(), publisher, title, mBook.getUrl());
            }
        } else
            return null;
    }

}
