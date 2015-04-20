package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.controller.feedParser.Message;
import singh.saurabh.iscfresnostate.controller.feedParser.MessageList;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class News {

    private Activity mContext;

    // Variables for News Class
    private List<Message> messages;
    private ArrayList<HashMap<String, String>> mNewsList = new ArrayList<>();
    private MessageList mMessageObject = new MessageList();

    //Tags for TextViews of listView
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_AUTHOR = "author";
    private static final String NEWS_DATE = "createdAt";
    private static final String NEWS_TAG = "tag";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Progress Dialog
    private ProgressDialog ppDialog;

    public News(Activity context) {
        this.mContext = context;
        mSwipeRefreshLayout = new SwipeRefreshLayout(mContext);
    }

    public void startLoadNewsTask() {
        new LoadNews().execute();
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
                NEWS_AUTHOR,
                NEWS_DATE,
                NEWS_TAG
        };

        int[] ids = {
                R.id.title_single_list_item,
                R.id.author_name_single_list_item,
                R.id.date_single_list_item,
                R.id.post_tags_textView_single_list_item
        };

        SimpleAdapter news_adapter = new SimpleAdapter(mContext, mNewsList, R.layout.single_list_item, keys, ids);

        if (mNewsList != null) {
            ListView newsList = (ListView)mContext.findViewById(R.id.news_listView);
            newsList.setAdapter(news_adapter);

            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(messages.get(position).getLink().toString()));
                    mContext.startActivity(viewMessage);
                }
            });
        }
        else {
            new CustomNetworkErrorHandler(mContext)
                    .errorDialogDisplay(mContext.getString(R.string.error_oops), mContext.getString(R.string.error_loading_data));
        }
    }

    public class LoadNews extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            ppDialog = new ProgressDialog(mContext);
            ppDialog.setMessage("Loading News...");
            ppDialog.setIndeterminate(false);
            ppDialog.setCancelable(true);
            ppDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return updateNews();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ppDialog.dismiss();
            if (result)
                updateNewsList();
            else {
                new CustomNetworkErrorHandler(mContext)
                        .errorDialogDisplay(mContext.getString(R.string.error_oops), mContext.getString(R.string.error_loading_data));
            }

            super.onPostExecute(result);
        }
    }
}