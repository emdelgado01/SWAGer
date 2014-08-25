package dashsample.swager.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import dashsample.swager.activities.BookFieldsActivity;
import dashsample.swager.models.books.Book;
import dashsample.swager.tasks.BooksTask;

/**
 * Created by Eric-Local on 8/22/2014.
 */
public abstract class BaseBookTaskFragment extends Fragment implements BooksTask.Callbacks {
    protected BooksTask mBooksTask;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBooksTask = (BooksTask) getFragmentManager().findFragmentByTag(BooksTask.TAG);
        if (mBooksTask == null) {
            mBooksTask = BooksTask.newInstance(this);
            getFragmentManager().beginTransaction().add(mBooksTask, BooksTask.TAG).commit();
        } else {
            mBooksTask.setCallbacks(this);
        }

    }

    @Override
    public void onBookCreated(Book book) {

    }

    @Override
    public void onBooksLoaded(List<Book> books) {

    }

    @Override
    public void onBooksError(String message) {

    }

    @Override
    public void onCreateError(String message) {

    }

    @Override
    public void onBookEdited(Book book) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BookFieldsActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Book book = data.getParcelableExtra(BookFieldsFragment.ARG_BOOK);
                if (TextUtils.isEmpty(book.getId())) {
                    mBooksTask.createBook(book);
                } else {
                    mBooksTask.editBook(book);
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
