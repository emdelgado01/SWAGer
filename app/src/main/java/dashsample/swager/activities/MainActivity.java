package dashsample.swager.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import dashsample.swager.R;
import dashsample.swager.fragments.BaseNavFragment;
import dashsample.swager.fragments.NavigationDrawerFragment;



public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, BaseNavFragment.Listener {
    public static final String TAG = "MainActivity";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private BaseNavFragment mNavFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onNavigationDrawerItemSelected(BaseNavFragment baseNavFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        mNavFragment = (BaseNavFragment) fragmentManager.findFragmentById(R.id.container);

        if (mNavFragment != null)
            mNavFragment.safeDestroy();

        //hideMap();
        restoreActionBar();
        mNavFragment = baseNavFragment;

        fragmentManager.beginTransaction()
                /*.setCustomAnimations(R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        0,
                        0)*/
                .replace(R.id.container, baseNavFragment)
                .commit();
        mTitle = getString(baseNavFragment.getTitle());
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_books);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void toggleDrawerIndication(boolean showIndicator) {
        mNavigationDrawerFragment.getDrawerToggle().setDrawerIndicatorEnabled(showIndicator);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mNavFragment == null)
            super.onActivityResult(requestCode, resultCode, data);
        else
            mNavFragment.onActivityResult(requestCode, resultCode, data);
    }
}
