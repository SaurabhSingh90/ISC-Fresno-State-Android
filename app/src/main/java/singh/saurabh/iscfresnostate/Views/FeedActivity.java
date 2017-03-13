package singh.saurabh.iscfresnostate.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private View footerView;

    private String nextPageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

//        mFeedItems = new ArrayList<>();

        mFeedRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mFeedRecyclerView.setHasFixedSize(true);     // in our content it does change the layout size

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mFeedRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FeedAdapter(this);
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
