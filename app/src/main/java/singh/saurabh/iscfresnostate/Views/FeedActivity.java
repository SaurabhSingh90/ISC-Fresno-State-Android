package singh.saurabh.iscfresnostate.Views;

import android.content.ComponentName;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import singh.saurabh.iscfresnostate.Adapters.FeedAdapter;
import singh.saurabh.iscfresnostate.Constants.Konst;
import singh.saurabh.iscfresnostate.FeedModel;
import singh.saurabh.iscfresnostate.R;

public class FeedActivity extends AppCompatActivity {

    private int mIndexOfFeedEndpoint;
    private boolean lastPage;
    private boolean isLoading;
    private FeedModel mFeedModel;

    private RecyclerView mFeedRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String nextPageUrl;

    // Package name for the Chrome channel the client wants to connect to. This
    // depends on the channel name.
    // Stable = com.android.chrome
    // Beta = com.chrome.beta
    // Dev = com.chrome.dev
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";  // Change when in stable

    CustomTabsClient mCustomTabsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // for warming up custom chrome tab
        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mCustomTabsClient = client;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, connection);
        Log.d("TAG", "ok: " + ok);

        mFeedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mFeedRecyclerView.setHasFixedSize(true);     // in our content it does change the layout size

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mFeedRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FeedAdapter(this, new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FeedModel.FeedItem item) {
                itemClick(item);
            }
        });
        mFeedRecyclerView.setAdapter(mAdapter);

        nextPageUrl = Konst.getFacebookGroupFeedEndpoint()
                +"?"
                +Konst.getFacebookEndpointFieldsKey()
                +Konst.getFacebookEndpointFieldsValue()
                +"&"
                +Konst.getFacebookEndpointDateFormatKey()
                +Konst.getFacebookEndpointDateFormatValue();

        loadFeedPage();
    }

    void itemClick(FeedModel.FeedItem item) {
        String url = item.getLink();

        if (url == null || url.length() == 0) {
            String[] idParts = item.getId().split("_");
            url = "https://www.facebook.com/groups/" + idParts[0] + "?view=permalink&id=" + idParts[1];
        }

        Log.d("TAG", "url: " + url);

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //mContext.startActivity(browserIntent);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        //builder.setToolbarColor(colorInt);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    public void loadFeedPage() {

        if (lastPage || isLoading) return;

        isLoading = true;

        GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                nextPageUrl,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        isLoading = false;

                        if (response == null || response.getRawResponse().length() == 0) return;

                        try {
                            mFeedModel = LoganSquare.parse(
                                    response.getRawResponse(),
                                    FeedModel.class
                            );

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        updateAdapter();
                    }
                }).executeAsync();
    }

    private void updateAdapter() {
        if (mFeedModel != null) {

            // Update adapter
            mAdapter.updateFeedList(mFeedModel.getFeedItems());
            if (mFeedModel.getPager() != null) {
                nextPageUrl = mFeedModel.getPager().getNextPageUrl();
                mIndexOfFeedEndpoint = nextPageUrl.indexOf(Konst.getFacebookGroupFeedEndpoint());
                nextPageUrl = nextPageUrl.substring(mIndexOfFeedEndpoint);
            } else {
                lastPage = true;
            }
        }
    }
}
