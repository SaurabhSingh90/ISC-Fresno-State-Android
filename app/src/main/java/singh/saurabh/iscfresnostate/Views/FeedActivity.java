package singh.saurabh.iscfresnostate.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;
import java.util.List;

import singh.saurabh.iscfresnostate.Adapters.FeedAdapter;
import singh.saurabh.iscfresnostate.Constants.Konst;
import singh.saurabh.iscfresnostate.FeedModel;
import singh.saurabh.iscfresnostate.R;

public class FeedActivity extends AppCompatActivity {

    public enum FeedItemType {
        Event,
        Status,
        Link,
        Photo,
        None
    }

//            FeedItemType feedItemType= FeedItemType.None;
//            switch (type) {
//                case "event":
//                    feedItemType = FeedItemType.Event;
//                    break;
//                case "status":
//                    feedItemType = FeedItemType.Status;
//                    break;
//                case "link":
//                    feedItemType = FeedItemType.Link;
//                    break;
//                case "photo":
//                    feedItemType = FeedItemType.Photo;
//                    break;
//            }
//            return feedItemType;

    private FeedModel mFeedModel;
//    private List<FeedModel.FeedItem> mFeedItems;

    private RecyclerView mFeedRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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


        getGraphResponse();
    }

    private void getGraphResponse() {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                Konst.getFacebookGroupFeedEndpoint(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            mFeedModel = LoganSquare.parse(
                                    response.getRawResponse(),
                                    FeedModel.class
                            );

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        if (mFeedModel != null) {
//                            mFeedItems = mFeedModel.getFeedItems();

                            // specify an adapter
                            mAdapter = new FeedAdapter(mFeedModel.getFeedItems());
                            mFeedRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString(
                Konst.getFacebookEndpointDateFormatKey(),
                Konst.getFacebookEndpointDateFormatValue());
        parameters.putString(
                Konst.getFacebookEndpointFieldsKey(),
                Konst.getFacebookEndpointFieldsValue());
        request.setParameters(parameters);
        request.executeAsync();
    }
}
