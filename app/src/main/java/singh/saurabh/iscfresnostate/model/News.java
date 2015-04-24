package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

    private Activity mActivity;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;

    // Variables for News Class
    private List<Message> messages;
    private ArrayList<HashMap<String, String>> mNewsList = new ArrayList<>();
    private MessageList mMessageObject = new MessageList();

    //Tags for TextViews of listView
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_DESCRIPTION = "description";
    private static final String NEWS_DATE = "createdAt";

    // Progress Dialog
    private ProgressDialog ppDialog;

    public News(Activity activity) {
        this.mActivity = activity;
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(mActivity);
    }

    public void startLoadNewsTask() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable())
            new LoadNews().execute();
        else
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
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

        SimpleAdapter news_adapter = new SimpleAdapter(mActivity, mNewsList, R.layout.single_newslist_item, keys, ids);

        if (mNewsList != null) {
            ListView newsList = (ListView) mActivity.findViewById(R.id.news_listView);
            newsList.setAdapter(news_adapter);

            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(messages.get(position).getLink().toString()));
                    mActivity.startActivity(viewMessage);
                }
            });
        }
        else {
            new CustomNetworkErrorHandler(mActivity)
                    .errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.error_loading_data));
        }
    }

    public class LoadNews extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            ppDialog = new ProgressDialog(mActivity);
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
                new CustomNetworkErrorHandler(mActivity)
                        .errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.error_loading_data));
            }

            super.onPostExecute(result);
        }
    }
}