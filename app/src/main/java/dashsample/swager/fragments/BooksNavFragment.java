package dashsample.swager.fragments;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import dashsample.swager.R;
import dashsample.swager.activities.BookDetailActivity;

/**
 * Created by Eric-Local on 8/21/2014.
 */
public class BooksNavFragment extends BaseNavFragment implements BooksListFragment.Callbacks {
    public static final String TAG = "BooksNavFragment";
    private static final String ARG_DETAIL_LOADED = "ARG_DETAIL_LOADED";
    private boolean mTwoPane=false;
    private BooksListFragment mListFragment;
    private boolean mDetailLoaded = false;

    public static BooksNavFragment newInstance() {
        return new BooksNavFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_nav_books, container, false);
        //mTwoPane = layout.findViewById(R.id.two_pane_detail) != null;
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_DETAIL_LOADED, mDetailLoaded);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {

        } else {
            mDetailLoaded = savedInstanceState.getBoolean(ARG_DETAIL_LOADED);
        }
        mListFragment = (BooksListFragment) getFragmentManager().findFragmentByTag(BooksListFragment.TAG);
        if (mListFragment == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            mListFragment = BooksListFragment.newInstance(this);
            fragmentTransaction.add(R.id.books_list_container, mListFragment, BooksListFragment.TAG);
            fragmentTransaction.commit();
        } else {
            mListFragment.setCallbacks(this);
        }
        if (!mTwoPane && mDetailLoaded)
            mListener.toggleDrawerIndication(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                if (!mTwoPane && mDetailLoaded) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.books_list_container, mListFragment);
                    fragmentTransaction.commit();
                    mListener.toggleDrawerIndication(true);
                    handledBackPressed();
                    return true;
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void safeDestroy() {
        //remove child fragments
    }

    private boolean handledBackPressed() {
        if (mTwoPane) {

        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            Fragment fragment = fragmentManager.findFragmentByTag(BookDetailFragment.TAG);
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
            }

            fragment = fragmentManager.findFragmentByTag(BookPagerFragment.TAG);
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
                fragmentManager.popBackStack();
            }
            fragmentTransaction.commit();
            mListener.toggleDrawerIndication(true);
            return true;

        }
        return false;
    }

    @Override
    public int getTitle() {
        return R.string.title_books;
    }

    @Override
    public void onBookSelected(int position) {
        if (mTwoPane) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.commit();

        } else {
            Intent intent = BookDetailActivity.newIntent(getActivity(), position);
            ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_out_left);
            startActivity(intent, activityOptions.toBundle());
        }
        //mDetailLoaded = true;
    }

    @Override
    public void onBooksDeleted(SparseArray<String> books) {

    }


}
