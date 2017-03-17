package singh.saurabh.iscfresnostate.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import singh.saurabh.iscfresnostate.Fragments.BoardFragment;
import singh.saurabh.iscfresnostate.Fragments.GroupFeedFragment;
import singh.saurabh.iscfresnostate.Fragments.NewsFragment;
import singh.saurabh.iscfresnostate.Fragments.NotificationFragment;
import singh.saurabh.iscfresnostate.Fragments.StoreFragment;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection;

import static singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection.CUSTOM_TAB_PACKAGE_NAME;
import static singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection.customTabsServiceConnection;

public class TabActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ChromeCustomTabsServiceConnection.Init();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        selectFragment(navigation.getMenu().getItem(0));
    }

    @Override
    protected void onStart() {
        super.onStart();

        CustomTabsClient.bindCustomTabsService(
                this,
                CUSTOM_TAB_PACKAGE_NAME,
                customTabsServiceConnection
        );
    }

    private void selectFragment(MenuItem item) {

        Fragment frag = null;
        switch (item.getItemId()) {
            case R.id.navigation_board:
                frag = BoardFragment.newInstance();
                break;
            case R.id.navigation_feed:
                frag = GroupFeedFragment.newInstance();
                break;
            case R.id.navigation_news:
                frag = NewsFragment.newInstance();
                break;
            case R.id.navigation_notifications:
                frag = NotificationFragment.newInstance();
                break;
            case R.id.navigation_store:
                frag = StoreFragment.newInstance();
                break;
        }

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, frag, frag.getTag());
            ft.commit();
        }
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(customTabsServiceConnection);
    }
}
