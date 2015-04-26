package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomAdapters.JobPostAdapter;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.view.JobDescription;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class JobPost {

    private static String TAG = DiscussionForum.class.getSimpleName();
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;

    public JobPost (Activity activity) {
        this.mActivity = activity;
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(mActivity);
        ContextThemeWrapper contextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(contextThemeWrapper);
        mProgressDialog.setMessage(mActivity.getString(R.string.loading_post_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
    }

    public void startJobPostTask() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            mProgressDialog.show();
            // Find all posts
            ParseQuery<ParseObject> query = ParseQuery.getQuery("JobPost");
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        updateJobPostList(parseObjects);
                    } else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
        }
    }

    /*
* Function to update list with post returned by query
* @param: list of parseObjects returned by search query
*/
    public void updateJobPostList (final List<ParseObject> parseObjects) {
        final int length = parseObjects.size();

        final ArrayList<HashMap<String, String>> postList;

        if (length > 0)
            postList = new ArrayList<>();
        else
            postList = null;
        ParseObject obj = null;
        for (int i = 0; i < length; i++) {
            obj = parseObjects.get(i);
            String firstName = obj.get(ParseKeys.JOBPOST_FIRST_NAME).toString();
            String location = obj.get(ParseKeys.JOBPOST_LOCATION).toString();
            String title = obj.get(ParseKeys.JOBPOST_TITLE).toString();
            String tags = "TAGS: ";
            tags = tags.concat(obj.get(ParseKeys.JOBPOST_TAGS).toString());

            Date createdAt = obj.getCreatedAt();
            String posted_on = createdAt.toString();
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy", Locale.US);
            Date d1 = null;
            try {
                d1 = sdf1.parse(posted_on);
            } catch (ParseException ee) {
                ee.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            posted_on = sdf.format(d1);
            HashMap<String, String> dataList = new HashMap<>();
            dataList.put("title", title);
            dataList.put("location", location);
            dataList.put("author", "Posted By: "+firstName);
            dataList.put("createdAt", "on: "+posted_on);
            dataList.put("tags", tags);

            if (postList != null) {
                postList.add(dataList);
            }
        }
        ListView lv = (ListView) mActivity.findViewById(android.R.id.list);
        TextView empty_text = (TextView) mActivity.findViewById(android.R.id.empty);

        if (postList != null) {
            ArrayAdapter<HashMap<String, String>> jobPostListAdapter = new JobPostAdapter(mActivity, postList);
            lv.setAdapter(jobPostListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String objectId = parseObjects.get(position).getObjectId();
                    Intent i = new Intent(mActivity, JobDescription.class);
                    i.putExtra(ParseKeys.OBJECTID, objectId);
                    mActivity.startActivity(i);
                }
            });

        } else {
            empty_text.setVisibility(View.VISIBLE);
            lv.setEmptyView(empty_text);
        }
    }

    public void searchJobPostTask(final String query, final Boolean showToast) {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {

            List<ParseQuery<ParseObject>> queries = new ArrayList<>();

            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(ParseKeys.JOBPOST_CLASS);
            query1.whereMatches(ParseKeys.JOBPOST_TITLE, query, "im");

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery(ParseKeys.JOBPOST_CLASS);
            query2.whereMatches(ParseKeys.JOBPOST_FIRST_NAME, query, "im");

            ParseQuery<ParseObject> query3 = ParseQuery.getQuery(ParseKeys.JOBPOST_CLASS);
            query3.whereEqualTo(ParseKeys.JOBPOST_TAGS, query);

            queries.add(query1);
            queries.add(query2);
            queries.add(query3);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
            mainQuery.orderByDescending("createdAt");
            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    if (parseObjects.size() > 0) {
                        if (e == null) {
                            updateJobPostList(parseObjects);
                        } else {
                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        updateJobPostList(parseObjects);
                        if (showToast)
                            Toast.makeText(mActivity, "No job post found!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
    }

}
