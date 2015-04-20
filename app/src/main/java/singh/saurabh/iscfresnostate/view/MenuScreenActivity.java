package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.model.DiscussionForum;
import singh.saurabh.iscfresnostate.model.News;


public class MenuScreenActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SearchView.OnQueryTextListener  {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    // Our created menu to use
    private Menu mMenu;

    private static String TAG = MenuScreenActivity.class.getSimpleName();
    private Context mContext = this;

    private static int SECTION_NUMBER = 0;

    private static DiscussionForum mDiscussionForum = null;
    private static News mNews = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        mDiscussionForum = new DiscussionForum(this);
        mNews = new News(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MENU_TAG", "onRestart ");
        if (SECTION_NUMBER == 1) {
            mDiscussionForum.startLoadCommentsTask();
        } else if (SECTION_NUMBER == 2) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser() == null) {
            Intent i = new Intent(mContext, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Log.d("MENU_TAG", "onResume ");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
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
        ActionBar actionBar = getSupportActionBar();
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
            if (SECTION_NUMBER == 1) {
                getMenuInflater().inflate(R.menu.menu_discussion_forum, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search_discussion_forum);
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                searchView.setQueryHint("Name/Title/Tag");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // perform query here
                        mDiscussionForum.searchPostTask(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.length() > 4) {
                            mDiscussionForum.searchPostTask(newText);
                            return true;
                        } else {
                            if (newText.length() == 0) {
                                mDiscussionForum.startLoadCommentsTask();
                                return true;
                            }
                            return false;
                        }
                    }
                });

                MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mDiscussionForum.startLoadCommentsTask();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
            }

            mMenu = menu;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_sign_out) {
            ProgressDialog dialog;
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(getString(R.string.signing_out_dialog_message));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
            ParseUser.logOut();
            dialog.dismiss();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mDiscussionForum.searchPostTask(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (i) {
                case 1:
                    SECTION_NUMBER = 1;
                    rootView = inflater.inflate(R.layout.fragment_1_discussion_forum, container, false);
                    mDiscussionForum.startLoadCommentsTask();
                    break;
                case 2:
                    SECTION_NUMBER = 2;
                    rootView = inflater.inflate(R.layout.fragment_2_news, container, false);
                    mNews.startLoadNewsTask();
                    break;
                case 3:
                    break;
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MenuScreenActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void addNewPost(View v) {
        Intent i = new Intent(mContext, AddNewPost.class);
        startActivity(i);
    }
}
