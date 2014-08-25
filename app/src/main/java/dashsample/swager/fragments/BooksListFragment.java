package dashsample.swager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.List;

import dashsample.swager.R;
import dashsample.swager.activities.BookFieldsActivity;
import dashsample.swager.common.UI;
import dashsample.swager.models.books.Book;

/**
 * Created by Eric-Local on 8/20/2014.
 */
public class BooksListFragment extends BaseBookTaskFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "BooksListFragment";
    private static final String ARG_REFRESHING = "ARG_REFRESHING";

    private GridView mBooksView;
    private View mEmpty;
    private View mProgress;
    private Callbacks mCallbacks;
    private SwipeRefreshLayout mSwipeRefresh;
    private BooksAdapter mAdapter;
    private SparseArray<Book> mActionSelection;
    private int mTotalSelected = 0;
    private boolean mRefreshing = false;
    private ShareActionProvider mShareActionProvider;

    public interface Callbacks {
        public void onBookSelected(int position);

        public void onBooksDeleted(SparseArray<String> books);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public static BooksListFragment newInstance(Callbacks callbacks) {
        BooksListFragment fragment = new BooksListFragment();
        fragment.mCallbacks = callbacks;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_books_list, container, false);
        mBooksView = (GridView) layout.findViewById(R.id.books_view);
        mProgress = layout.findViewById(R.id.list_progress);
        mEmpty = layout.findViewById(android.R.id.empty);
        mSwipeRefresh = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        mBooksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallbacks.onBookSelected(position);
            }
        });
        mBooksView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mBooksView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    if (mActionSelection == null)
                        mActionSelection = new SparseArray<Book>();
                    Book book = (Book) mBooksView.getItemAtPosition(position);
                    mActionSelection.put(position, book);
                    mTotalSelected += 1;
                } else {
                    mActionSelection.remove(position);
                    mTotalSelected -= 1;
                }
                mode.setTitle(mTotalSelected + "");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.books_actions, menu);
                mActionSelection = null;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete_books:
                        mBooksTask.deleteBooks(mActionSelection);
                        mode.finish();
                        return true;
                    case R.id.action_checkout_books: {
                        CheckoutDialogFragment.newInstance(new CheckoutDialogFragment.Callbacks() {
                            @Override
                            public void onCreateClick(String name) {
                                mBooksTask.checkoutBooks(name, mActionSelection);
                                mode.finish();
                            }
                        }).show(getFragmentManager(), CheckoutDialogFragment.TAG);

                        break;
                    }
                    case R.id.action_share_books:
                        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
                        mShareActionProvider.setShareIntent(UI.getDefaultShareIntent(getActivity(), mActionSelection));

                        break;
                    default:
                        return false;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mTotalSelected = 0;

            }
        });

        if (savedInstanceState == null) {

        } else {
            mRefreshing = savedInstanceState.getBoolean(ARG_REFRESHING);
        }
        mSwipeRefresh.setRefreshing(mRefreshing);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_REFRESHING, mRefreshing);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new BooksAdapter(getActivity());
        mBooksView.setAdapter(mAdapter);
        mBooksTask.loadBooks(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.books, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_book: {
                startActivityForResult(BookFieldsActivity.newIntent(getActivity()), BookFieldsActivity.REQUEST_CODE, UI.getDetailActivityOptions(getActivity()));
                return true;
            }
            case R.id.action_seed: {
                manualRefresh();
                return true;
            }
            case R.id.action_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.confirm_delete_all)
                        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mBooksTask.deleteAllBooks();
                            }
                        })
                        .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void manualRefresh() {
        mSwipeRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBooksTask.loadBooks(true);
    }

    @Override
    public void onRefresh() {
        mRefreshing = true;
        mBooksTask.loadBooks(false);
    }

    @Override
    public void onBooksLoaded(List<Book> books) {
        mRefreshing = false;
        mSwipeRefresh.setRefreshing(false);
        mAdapter.setData(books);
        UI.crossFade(true, mBooksView, mProgress, View.GONE, getActivity());
        mBooksView.setEmptyView(mEmpty);
    }

    @Override
    public void onBooksError(String message) {
        mAdapter.setData(null);
        UI.crossFade(true, mBooksView, mProgress, View.GONE, getActivity());
        mBooksView.setEmptyView(mEmpty);
    }

    @Override
    public void onCreateError(String message) {

    }

    @Override
    public void onBookCreated(Book book) {
        mAdapter.add(book);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookEdited(Book book) {
        mBooksTask.loadBooks(true);
    }

    @Override
    public void onBookDeleted() {
        mBooksTask.loadBooks(true);
    }

    public class BooksAdapter extends ArrayAdapter<Book> {
        private final LayoutInflater mInflater;


        public BooksAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<Book> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.book_list_item, parent, false);
                viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.text1), (TextView) convertView.findViewById(R.id.text2), (ImageButton) convertView.findViewById(R.id.overflow), (ImageButton) convertView.findViewById(R.id.multi_toggle));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Book book = getItem(position);
            viewHolder.mTitle.setText(book.getTitle());
            viewHolder.mDescription.setText(book.getAuthor());
            viewHolder.mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getContext(), viewHolder.mMenu);
                    popup.getMenuInflater().inflate(R.menu.book, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit_book:
                                    startActivityForResult(BookFieldsActivity.newIntent(getActivity(), book), BookFieldsActivity.REQUEST_CODE, UI.getDetailActivityOptions(getContext()));
                                    break;
                                case R.id.action_delete_book: {
                                    mBooksTask.deleteBook(book);
                                    break;
                                }
                                case R.id.action_share_book: {
                                    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
                                    mShareActionProvider.setShareIntent(UI.getDefaultShareIntent(getContext(), book));
                                    break;
                                }
                                case R.id.action_checkout_book: {
                                    CheckoutDialogFragment.newInstance(new CheckoutDialogFragment.Callbacks() {
                                        @Override
                                        public void onCreateClick(String name) {
                                            mBooksTask.checkoutBook(name, book);
                                        }
                                    }).show(getFragmentManager(), CheckoutDialogFragment.TAG);
                                    break;
                                }

                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            viewHolder.mMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = mBooksView.isItemChecked(position);
                    mBooksView.setItemChecked(position, !isChecked);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private final TextView mTitle;
            private final TextView mDescription;
            private final ImageButton mMenu;
            private final ImageButton mMulti;

            public ViewHolder(TextView title, TextView description, ImageButton menu, ImageButton multi) {
                this.mTitle = title;
                this.mDescription = description;
                this.mMenu = menu;
                this.mMulti = multi;
            }
        }

    }

   /* @Override
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
    }*/
}
