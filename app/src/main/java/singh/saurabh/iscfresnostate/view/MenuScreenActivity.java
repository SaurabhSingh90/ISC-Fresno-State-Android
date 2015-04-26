package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.model.DiscussionForum;
import singh.saurabh.iscfresnostate.model.Forms;
import singh.saurabh.iscfresnostate.model.Gallery;
import singh.saurabh.iscfresnostate.model.JobPost;
import singh.saurabh.iscfresnostate.model.News;
import singh.saurabh.iscfresnostate.model.ParseKeys;


public class MenuScreenActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks  {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public PlaceholderFragment mPlaceHolderFragment = new PlaceholderFragment();

    private static String TAG = MenuScreenActivity.class.getSimpleName();
    private Context mContext = this;
    private ActionMode mActionMode;
    private ParseInstallation pi = null;
    private ParseUser currentUser = null;

    private static int SECTION_NUMBER = 0;

    private static ContextThemeWrapper mContextThemeWrapper;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    // Class objects for fragments
    private static DiscussionForum mDiscussionForum = null;
    private static News mNews = null;
    private static JobPost mJobPost = null;
    private static Forms mForms = null;
    private static Gallery mGallery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        currentUser = ParseUser.getCurrentUser();
        pi = ParseInstallation.getCurrentInstallation();
        pi.put("firstName", currentUser.get("firstName").toString());

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Initializing fragment class objects
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser() == null) {
            Intent i = new Intent(mContext, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
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
                .replace(R.id.container, mPlaceHolderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1).toUpperCase();
                break;
            case 2:
                mTitle = getString(R.string.title_section2).toUpperCase();
                break;
            case 3:
                mTitle = getString(R.string.title_section3).toUpperCase();
                break;
            case 4:
                mTitle = getString(R.string.title_section4).toUpperCase();
                break;
            case 5:
                mTitle = getString(R.string.title_section5).toUpperCase();
                break;
            case 6:
                mTitle = getString(R.string.title_section6).toUpperCase();
                break;
            case 7:
                mTitle = getString(R.string.title_section7).toUpperCase();
                break;
            case 8:
                mTitle = getString(R.string.title_section8).toUpperCase();
                break;
            case 9:
                mTitle = getString(R.string.title_section9).toUpperCase();
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
                getMenuInflater().inflate(R.menu.menu_fragment_1, menu);
                searchViewInitializing(menu);
            } else if (SECTION_NUMBER == 2) {
                getMenuInflater().inflate(R.menu.menu_fragment_2, menu);
            } else if (SECTION_NUMBER == 3) {
                getMenuInflater().inflate(R.menu.menu_fragment_3, menu);
            } else if (SECTION_NUMBER == 4) {
                getMenuInflater().inflate(R.menu.menu_fragment_4, menu);
                searchViewInitializing(menu);
                MenuItem menuItem = menu.getItem(1);
                JSONArray channels = pi.getJSONArray("channels");
                for (int i = 0; i < channels.length(); i++) {
                    try {
                        if (channels.getString(i).equals(ParseKeys.JOB_POSTING_CHANNEL)) {
                            menuItem.setIcon(R.mipmap.ic_action_important_marked);
                            menuItem.setChecked(true);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (SECTION_NUMBER == 5) {
                getMenuInflater().inflate(R.menu.menu_fragment_5, menu);
            } else if (SECTION_NUMBER == 6) {
                getMenuInflater().inflate(R.menu.menu_fragment_6, menu);
            } else if (SECTION_NUMBER == 7) {
                getMenuInflater().inflate(R.menu.menu_fragment_7, menu);
            } else if (SECTION_NUMBER == 8) {
                getMenuInflater().inflate(R.menu.menu_fragment_8, menu);
            } else if (SECTION_NUMBER == 9) {
                getMenuInflater().inflate(R.menu.menu_fragment_9, menu);
            }

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void searchViewInitializing(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Title/Name/Tag");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                if (SECTION_NUMBER == 1)
                    mDiscussionForum.searchPostTask(query, true);
                else if (SECTION_NUMBER == 4)
                    mJobPost.searchJobPostTask(query, true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    if (SECTION_NUMBER == 1)
                        mDiscussionForum.searchPostTask(newText, false);
                    else if (SECTION_NUMBER == 4) {
                        mJobPost.searchJobPostTask(newText, false);
                    }
                    return true;
                } else {
                    if (newText.length() == 0) {
                        if (SECTION_NUMBER == 1)
                            mDiscussionForum.startLoadCommentsTask();
                        else if (SECTION_NUMBER == 4)
                            mJobPost.startJobPostTask();
                        return true;
                    }
                    return false;
                }
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (SECTION_NUMBER == 1)
                    mDiscussionForum.startLoadCommentsTask();
                else if (SECTION_NUMBER == 4)
                    mJobPost.startJobPostTask();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_refresh) {
            new RefreshNewsList().execute();
            return true;
        }

        if (id == R.id.action_delete_post) {
            mActionMode = startSupportActionMode(mDiscussionForum.ActionBarCallBack());
            mDiscussionForum.deleteTask = true;
            mDiscussionForum.deletePostTask();
            return true;
        }

        if (id == R.id.action_notify) {

            if (item.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContextThemeWrapper);
                builder.setIcon(android.R.drawable.ic_menu_info_details)
                        .setTitle(getString(R.string.unsubscribe_alert_title))
                        .setMessage(getString(singh.saurabh.iscfresnostate.R.string.unsubscribe_alert_message))
                        .setPositiveButton(getString(R.string.notify_me_unsubscribe_button_text), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                item.setChecked(false);
                                item.setIcon(R.mipmap.ic_action_important_unmarked);
                                ParsePush.unsubscribeInBackground(ParseKeys.JOB_POSTING_CHANNEL);
                                pi.saveEventually();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContextThemeWrapper);
                builder.setIcon(android.R.drawable.ic_menu_info_details)
                        .setTitle(getString(R.string.notify_me_alert_title))
                        .setMessage(getString(R.string.notify_me_alert_message))
                        .setPositiveButton(getString(R.string.notify_me_subscribe_button_text), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                item.setChecked(true);
                                item.setIcon(R.mipmap.ic_action_important_marked);
                                ParsePush.subscribeInBackground(ParseKeys.JOB_POSTING_CHANNEL);
                                pi.saveEventually();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
            return true;
        }

        if (id == R.id.action_sign_out) {
            ProgressDialog dialog;
            dialog = new ProgressDialog(mContextThemeWrapper);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @SuppressWarnings("deprecation")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (i) {
                case 1:
                    // Discussion Forum
                    SECTION_NUMBER = 1;
                    rootView = inflater.inflate(R.layout.fragment_1_discussion_forum, container, false);
                    mDiscussionForum.startLoadCommentsTask();

                    if (!mDiscussionForum.deleteTask) {
                        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.post_list_swipeRefreshLayout);
                        mSwipeRefreshLayout.setColorScheme(
                                R.color.swipe_color_1, R.color.swipe_color_2,
                                R.color.swipe_color_3, R.color.swipe_color_4);
                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                Log.d(TAG, "post refreshing");
                                mSwipeRefreshLayout.setRefreshing(true);
                                new RefreshNewsList().execute();
                            }
                        });
                    }
                    break;

                case 2:
                    // Top News
                    SECTION_NUMBER = 2;
                    rootView = inflater.inflate(R.layout.fragment_2_news, container, false);
                    mNews.startLoadNewsTask();
                    if (!mDiscussionForum.deleteTask) {
                        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.news_list_swipeRefreshLayout);
                        mSwipeRefreshLayout.setColorScheme(
                                R.color.swipe_color_1, R.color.swipe_color_2,
                                R.color.swipe_color_3, R.color.swipe_color_4);
                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                Log.d(TAG, "news refreshing");
                                mSwipeRefreshLayout.setRefreshing(true);
                                new RefreshNewsList().execute();
                            }
                        });
                    }
                    break;

                case 3:
                    // Buy/Sell
                    SECTION_NUMBER = 3;
                    rootView = inflater.inflate(R.layout.fragment_3_buy_sell, container, false);
                    break;

                case 4:
                    // Job Posting
                    SECTION_NUMBER = 4;
                    rootView = inflater.inflate(R.layout.fragment_4_job_postings, container, false);
                    mJobPost = new JobPost(getActivity());
                    mJobPost.startJobPostTask();
                    if (!mDiscussionForum.deleteTask) {
                        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.job_posting_swipeRefreshLayout);
                        mSwipeRefreshLayout.setColorScheme(
                                R.color.swipe_color_1, R.color.swipe_color_2,
                                R.color.swipe_color_3, R.color.swipe_color_4);
                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                Log.d(TAG, "post refreshing");
                                mSwipeRefreshLayout.setRefreshing(true);
                                new RefreshNewsList().execute();
                            }
                        });
                    }
                    break;

                case 5:
                    // Gallery
                    SECTION_NUMBER = 5;
                    rootView = inflater.inflate(R.layout.fragment_5_gallery, container, false);
                    mGallery = new Gallery(getActivity(), rootView);
                    mGallery.startGalleryFragment();
                    break;

                case 6:
                    // Things you need
                    SECTION_NUMBER = 6;
                    rootView = inflater.inflate(R.layout.fragment_6_essentials, container, false);
                    break;

                case 7:
                    // Forms
                    SECTION_NUMBER = 7;
                    rootView = inflater.inflate(R.layout.fragment_7_forms, container, false);
                    mForms = new Forms(getActivity(), rootView);
                    mForms.startFormsFragment();
                    break;

                case 8:
                    // About us
                    SECTION_NUMBER = 8;
                    String mCommitteeUrl = "http://www.iscfresnostate.com/committee/";
                    final ProgressDialog dialog = new ProgressDialog(mContextThemeWrapper);
                    dialog.setMessage(getString(R.string.loading_text));
                    dialog.setIndeterminate(false);
                    dialog.setCancelable(false);
                    rootView = inflater.inflate(R.layout.fragment_8_committee, container, false);
                    WebView mFormWebView = (WebView) rootView.findViewById(R.id.committee_webView);
                    if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
                        dialog.show();
                        mFormWebView.setWebChromeClient(new WebChromeClient() {
                            public void onProgressChanged(WebView view, int progress) {
                                setProgress(progress * 100);
                                if(progress == 100) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                }
                            }
                        });
                        mFormWebView.setWebViewClient(new WebViewClient() {
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_loading_data));
                            }
                        });
                        mFormWebView.loadUrl(mCommitteeUrl);
                    } else
                        mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
                    break;

                case 9:
                    // FAQs
                    SECTION_NUMBER = 9;
                    rootView = inflater.inflate(R.layout.fragment_9_faq, container, false);
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

    public class RefreshNewsList extends AsyncTask<Void, Void, Void> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected Void doInBackground(Void... params) {
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() && !mDiscussionForum.deleteTask) {
                try {
                    Thread.sleep(TASK_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (SECTION_NUMBER == 1 && !mDiscussionForum.deleteTask) {
                mDiscussionForum.startLoadCommentsTask();
            } else if (SECTION_NUMBER == 2 && !mDiscussionForum.deleteTask) {
                mNews.startLoadNewsTask();
            } else if (SECTION_NUMBER == 4 && !mDiscussionForum.deleteTask) {
                mJobPost.startJobPostTask();
            }
        }
    }

    public void addNewPost(View v) {
        Intent i = new Intent(mContext, AddNewPost.class);
        startActivity(i);
    }

    public void addNewJobPost(View v) {
        Intent i = new Intent(mContext, AddNewJobPost.class);
        startActivity(i);
    }
}
