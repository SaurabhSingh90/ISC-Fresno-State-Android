package singh.saurabh.iscfresnostate.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;
import java.util.List;

import singh.saurabh.iscfresnostate.Adapters.FeedAdapter;
import singh.saurabh.iscfresnostate.Constants.Konst;
import singh.saurabh.iscfresnostate.FeedModel;
import singh.saurabh.iscfresnostate.Helpers.RecyclerItemClickListener;
import singh.saurabh.iscfresnostate.R;

import static singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection.mCustomTabsIntent;
import static singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection.mCustomTabsSession;

/**
 * Created by saurabhsingh on 3/15/17.
 */

public class GroupFeedFragment extends Fragment {

    private int mIndexOfFeedEndpoint;
    private boolean lastPage;
    private boolean isLoading;
    private FeedModel mFeedModel;

    private RecyclerView mFeedRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<FeedModel.FeedItem> mFeedItems;
    private String nextPageUrl;

    public GroupFeedFragment() {
        // Required empty public constructor
    }

    public static GroupFeedFragment newInstance() {

        Bundle args = new Bundle();

        GroupFeedFragment fragment = new GroupFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFeedRecyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mFeedRecyclerView.setHasFixedSize(true);     // in our content it does change the layout size

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mFeedRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new FeedAdapter(this);
        mFeedRecyclerView.setAdapter(mAdapter);
        mFeedRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this.getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        itemClick(mFeedItems.get(position));
                    }
                })
        );


        nextPageUrl = Konst.getFacebookGroupFeedEndpoint()
                +"?"
                +Konst.getFacebookEndpointFieldsKey()
                +Konst.getFacebookEndpointFieldsValue()
                +"&"
                +Konst.getFacebookEndpointDateFormatKey()
                +Konst.getFacebookEndpointDateFormatValue();

        mFeedItems = new ArrayList<>();
        loadFeedPage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void itemClick(FeedModel.FeedItem item) {
        String url = item.getLink();

        if (url == null || url.length() == 0) {
            String[] idParts = item.getId().split("_");
            url = "https://www.facebook.com/groups/" + idParts[0] + "?view=permalink&id=" + idParts[1];
        }

        Log.d("TAG", "opening url: " + url);

        mCustomTabsIntent.launchUrl(this.getActivity(), Uri.parse(url));
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
            mFeedItems.addAll(mFeedModel.getFeedItems());
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
