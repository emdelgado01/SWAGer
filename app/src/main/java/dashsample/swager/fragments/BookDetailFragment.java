package dashsample.swager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dashsample.swager.R;
import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/21/2014.
 */
public class BookDetailFragment extends Fragment {
    public static final String TAG = "BookDetailFragment";
    public static final String ARG_BOOK = "ARG_BOOK";

    private Book mBook;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mPublisher;
    private TextView mCategories;
    private TextView mCheckedOut;


    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_book_detail, container, false);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args == null || !args.containsKey(ARG_BOOK)) {
                throw new IllegalArgumentException(ARG_BOOK + " not provided to BookDetailFragment");
            } else {
                mBook = args.getParcelable(ARG_BOOK);
            }
        } else {
            mBook = savedInstanceState.getParcelable(ARG_BOOK);
        }

        mTitle = (TextView) layout.findViewById(R.id.book_title);
        mAuthor = (TextView) layout.findViewById(R.id.book_author);
        mPublisher = (TextView) layout.findViewById(R.id.book_publisher);
        mCategories = (TextView) layout.findViewById(R.id.book_categories);
        mCheckedOut = (TextView) layout.findViewById(R.id.book_last_checked_out);
        setFields();
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_BOOK, mBook);
    }

    public void refresh(Book book) {
        mBook = book;
        setFields();
    }

    private void setFields() {
        mTitle.setText(mBook.getTitle());
        mAuthor.setText(mBook.getAuthor());
        mPublisher.setText(mBook.getPublisher());
        mCategories.setText(mBook.getCategories());
        if (!TextUtils.isEmpty(mBook.getLastCheckedOut()))
            mCheckedOut.setText(String.format("%s by %s", mBook.getLastCheckedOut(), mBook.getLastCheckedOutBy()));

    }

}
