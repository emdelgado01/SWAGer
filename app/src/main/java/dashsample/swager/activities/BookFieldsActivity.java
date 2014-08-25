package dashsample.swager.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dashsample.swager.R;
import dashsample.swager.fragments.BookFieldsFragment;
import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/23/2014.
 */
public class BookFieldsActivity extends Activity {
    public static final String TAG = "BookFieldsActivity";
    public static final int REQUEST_CODE = 100;


    BookFieldsFragment mFieldsFragment;

    public static Intent newIntent(Context context) {
        return newIntent(context, null);
    }

    public static Intent newIntent(Context context, Book book) {
        Intent intent = new Intent(context, BookFieldsActivity.class);
        intent.putExtra(BookFieldsFragment.ARG_BOOK, book);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_fields);

        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.ab_done_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                        Book book = mFieldsFragment.getBook();
                        if (book != null) {
                            Intent finishIntent = new Intent();
                            finishIntent.putExtra(BookFieldsFragment.ARG_BOOK, book);
                            setResult(RESULT_OK, finishIntent);
                            finish();
                        }
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Cancel"
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Book book = null;
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(BookFieldsFragment.ARG_BOOK)) {
                book = getIntent().getParcelableExtra(BookFieldsFragment.ARG_BOOK);
            }
        } else {
            mFieldsFragment = (BookFieldsFragment) getFragmentManager().findFragmentById(R.id.books_fields_container);
        }

        mFieldsFragment = (BookFieldsFragment) getFragmentManager().findFragmentById(R.id.books_fields_container);
        if (mFieldsFragment == null) {
            mFieldsFragment = BookFieldsFragment.newInstance(book);
            getFragmentManager().beginTransaction().add(R.id.books_fields_container, mFieldsFragment).commit();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}
