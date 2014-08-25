package dashsample.swager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import dashsample.swager.R;
import dashsample.swager.fragments.BookPagerFragment;

/**
 * Created by Eric-Local on 8/23/2014.
 */
public class BookDetailActivity extends Activity {
    public static final String TAG = "BookDetailActivity";
    BookPagerFragment mPagerFragment;

    public static Intent newIntent(Context context) {
        return newIntent(context, -1);
    }

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(BookPagerFragment.ARG_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        if (savedInstanceState == null) {
            int position = -1;
            if (getIntent().hasExtra(BookPagerFragment.ARG_POSITION)) {
                position = getIntent().getIntExtra(BookPagerFragment.ARG_POSITION, -1);
            }
            mPagerFragment = BookPagerFragment.newInstance(position);
            getFragmentManager().beginTransaction().add(R.id.detail_container, mPagerFragment).commit();
        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
