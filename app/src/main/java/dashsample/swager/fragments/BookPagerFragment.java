package dashsample.swager.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;

import java.util.List;

import dashsample.swager.R;
import dashsample.swager.activities.BookFieldsActivity;
import dashsample.swager.common.UI;
import dashsample.swager.models.books.Book;
import dashsample.swager.models.storage.Session;

/**
 * Created by Eric-Local on 8/22/2014.
 */
public class BookPagerFragment extends BaseBookTaskFragment {
    public static final String TAG = "BookPagerFragment";
    public static final String ARG_POSITION = "ARG_POSITION";

    private int mStartPosition = 0;
    private ViewPager mPager;
    private BooksPagerAdapter mAdapter;
    private ShareActionProvider mShareActionProvider;

    public static BookPagerFragment newInstance(int startPosition) {
        BookPagerFragment fragment = new BookPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, startPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_books_pager, container, false);
        mPager = (ViewPager) layout.findViewById(R.id.books_pager);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args == null || !args.containsKey(ARG_POSITION)) {
                throw new IllegalArgumentException(ARG_POSITION + " not provided to fragment");
            } else {
                mStartPosition = args.getInt(ARG_POSITION);
            }

        } else {
            mStartPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        mBooksTask.loadBooks(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, mStartPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book, menu);
        MenuItem item = menu.findItem(R.id.action_share_book);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareIntent(UI.getDefaultShareIntent(getActivity(), Session.getBook(mPager.getCurrentItem())));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onBooksLoaded(List<Book> books) {
        mAdapter = new BooksPagerAdapter(getFragmentManager(), books);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mShareActionProvider != null)
                    mShareActionProvider.setShareIntent(UI.getDefaultShareIntent(getActivity(), Session.getBook(mPager.getCurrentItem())));
            }
        });


        mPager.setCurrentItem(mStartPosition);
    }

    @Override
    public void onBookDeleted() {
        mBooksTask.loadBooks(true);
    }

    @Override
    public void onBooksError(String message) {

    }

    @Override
    public void onBookEdited(Book book) {
        ((BookDetailFragment) mAdapter.mFragments.get(mPager.getCurrentItem())).refresh(book);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Book book = Session.getBook(mPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.action_edit_book:
                startActivityForResult(BookFieldsActivity.newIntent(getActivity(), book), BookFieldsActivity.REQUEST_CODE, UI.getDetailActivityOptions(getActivity()));
                return true;
            case R.id.action_delete_book: {
                mBooksTask.deleteBook(book);
                return true;
            }
            case R.id.action_share_book: {
                mShareActionProvider = (ShareActionProvider) item.getActionProvider();
                mShareActionProvider.setShareIntent(UI.getDefaultShareIntent(getActivity(), book));
                return true;
            }
            case R.id.action_checkout_book: {
                CheckoutDialogFragment.newInstance(new CheckoutDialogFragment.Callbacks() {
                    @Override
                    public void onCreateClick(String name) {
                        mBooksTask.checkoutBook(name, book);
                    }
                }).show(getFragmentManager(), CheckoutDialogFragment.TAG);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class BooksPagerAdapter extends FragmentStatePagerAdapter {
        private List<Book> mCollection;
        SparseArray<Fragment> mFragments = new SparseArray<Fragment>();


        public BooksPagerAdapter(FragmentManager fm, List<Book> records) {
            super(fm);
            mCollection = records;
        }

        @Override
        public Fragment getItem(int i) {
            return BookDetailFragment.newInstance(mCollection.get(i));
        }

        @Override
        public int getCount() {
            return mCollection.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mFragments.remove(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragments.put(position, fragment);
            return fragment;
        }
    }
}
