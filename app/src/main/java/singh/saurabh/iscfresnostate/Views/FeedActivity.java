package singh.saurabh.iscfresnostate.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;
import java.util.List;

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

    private FeedModel feedModel;
    private List<FeedModel.FeedItem> feedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedItems = new ArrayList<>();

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
                            feedModel = LoganSquare.parse(response.getRawResponse(), FeedModel.class);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        if (feedModel != null) {
                            feedItems = feedModel.getFeedItems();
//                            FeedModel.FeedItem item = feedItems.get(0);
//                            String name = item.getFromUser().getName();
//                            String y = name;
                            // update adapter

                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "from,message,story,link,picture,name,description,type,object_id,created_time,updated_time,is_hidden,subscribed,is_expired");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
