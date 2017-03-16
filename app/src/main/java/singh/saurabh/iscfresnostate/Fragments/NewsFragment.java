package singh.saurabh.iscfresnostate.Fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import singh.saurabh.iscfresnostate.NewsFeedParser.Message;
import singh.saurabh.iscfresnostate.NewsFeedParser.MessageList;
import singh.saurabh.iscfresnostate.R;

import static singh.saurabh.iscfresnostate.Services.ChromeCustomTabsServiceConnection.mCustomTabsIntent;

public class NewsFragment extends Fragment {

    ListView mNewsFeedListView;

    // Variables for News Class
    private List<Message> messages;
    private ArrayList<HashMap<String, String>> mNewsList = new ArrayList<>();
    private MessageList mMessageObject = new MessageList();

    //Tags for TextViews of listView
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_DESCRIPTION = "description";
    private static final String NEWS_DATE = "createdAt";

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
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
        new LoadNews().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNewsFeedListView = (ListView) view.findViewById(R.id.news_feed_listView);
    }

    /**
     * Retrieves recent news from the fresno state rss feed url.
     */
    public boolean updateNews() {
        mNewsList = mMessageObject.startFunction();
        messages = mMessageObject.mMessages;

        if (mNewsList == null)
            return false;
        else
            return true;
    }

    /**
     * Inserts the parsed news data into the news list view.
     */
    private void updateNewsList() {

        String[] keys = {
                NEWS_TITLE,
                NEWS_DATE,
                NEWS_DESCRIPTION
        };

        int[] ids = {
                R.id.title_single_newslist_item,
                R.id.date_single_newslist_item,
                R.id.description_single_newslist_item
        };

        SimpleAdapter news_adapter = new SimpleAdapter(this.getActivity(), mNewsList, R.layout.news_feed_item, keys, ids);

        if (mNewsList != null) {

            if (this.getView() == null) return;

            if (mNewsFeedListView == null) {
                mNewsFeedListView = (ListView) this.getView().findViewById(R.id.news_feed_listView);
            }
            mNewsFeedListView.setAdapter(news_adapter);

            mNewsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    itemClick(messages.get(position));
                }
            });
        }
        else {
//            new CustomNetworkErrorHandler(mActivity)
//                    .errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.error_loading_data));
        }
    }

    void itemClick(Message item) {
        String url = item.getLink().toString();

        Log.d("TAG", "opening url: " + url);

        mCustomTabsIntent.launchUrl(this.getActivity(), Uri.parse(url));
    }

    public class LoadNews extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
//            ppDialog = new ProgressDialog(mContextThemeWrapper);
//            ppDialog.setMessage("Loading News...");
//            ppDialog.setIndeterminate(false);
//            ppDialog.setCancelable(true);
//            ppDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return updateNews();
        }

        @Override
        protected void onPostExecute(Boolean result) {
//            ppDialog.dismiss();
            if (result)
                updateNewsList();
            else {
//                new CustomNetworkErrorHandler(mActivity)
//                        .errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.error_loading_data));
            }

            super.onPostExecute(result);
        }
    }
}
