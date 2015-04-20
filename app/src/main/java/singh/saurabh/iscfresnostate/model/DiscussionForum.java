package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomAdapter;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class DiscussionForum {

    private static String TAG = DiscussionForum.class.getSimpleName();
    private Activity mActivity;
    private ProgressDialog dialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private Boolean flag;

    public DiscussionForum(Activity activity) {
        this.mActivity = activity;
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(mActivity);

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Loading posts...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
    }

    public void startLoadCommentsTask() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            dialog.show();
            flag = false;
            // Find all posts
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.orderByAscending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    dialog.dismiss();
                    if (e == null) {
                        fillPostList(parseObjects, flag);
                    } else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.d(TAG, "No network");
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
        }
    }

    /*
* Function to update list with post returned by query
* @param: list of parseObjects returned by search query
*/
    public void fillPostList(final List<ParseObject> parseObjects, boolean flag_for_checkbox) {
        int length = parseObjects.size();

        final ArrayList<HashMap<String, String>> postList;

        if (length > 0)
            postList = new ArrayList<>();
        else
            postList = null;

        for (int i = length-1; i >= 0; i--) {
            ParseObject obj = parseObjects.get(i);
            String firstName = obj.get("firstName").toString();
            String title = obj.get("postTitle").toString();

            JSONArray tagsJSONArray = obj.getJSONArray("postTags");
            String[] tags = new String[tagsJSONArray.length()];
            for (int j = 0; j < tagsJSONArray.length(); j++) {
                try {
                    tags[j] = tagsJSONArray.getString(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Date createdAt = obj.getCreatedAt();
            String posted_on = createdAt.toString();
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy", Locale.US);
            Date d1 = null;
            try {
                d1 = sdf1.parse(posted_on);
            } catch (ParseException ee) {
                ee.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy   h:mm a", Locale.US);
            posted_on = sdf.format(d1);
            HashMap<String, String> dataList = new HashMap<>();
            dataList.put("author", firstName);
            dataList.put("title", title);
            dataList.put("createdAt", posted_on);

            String str = "TAGS: ";

            if (tags.length > 1) {
                for (String tag: tags) {
                    str = str + tag + " || ";
                }
            } else
                str = str + tags[0];

            dataList.put("tags", str);

            if (postList != null) {
                postList.add(dataList);
            }
        }
        ListView lv = (ListView) mActivity.findViewById(R.id.postList);
        TextView empty_text = (TextView) mActivity.findViewById(R.id.empty_text_for_postList);

        if (postList != null) {
            empty_text.setVisibility(View.INVISIBLE);
            ArrayAdapter<HashMap<String, String>> adapter = new CustomAdapter(mActivity, postList);
            lv.setAdapter(adapter);
//            mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);                 // for multiple choice mode
//            mListView.setSelector(R.drawable.bg_gradient);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    if ((postList.size() - 1) - position >= 0) {
//                        ParseObject obj = parseObjects.get((postList.size() - 1) - position);
//                        String objectId = obj.getObjectId();
//                        Intent i = new Intent(mActivity, SinglePostDisplay.class);
//                        i.putExtra("objectId", objectId);
//                        mActivity.startActivity(i);
//                    }
//                }
//            });
        } else {
            empty_text.setVisibility(View.VISIBLE);
            lv.setEmptyView(empty_text);
        }
    }

    public void searchPostTask(final String query) {
        dialog.show();
        flag = false;

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Post");
        query1.whereMatches("postTitle", query, "im");

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Post");
        query2.whereMatches("firstName", query, "im");

//        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Post");
//        query3.whereMatches("postTags", query, "im");

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
//        queries.add(query3);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending("createdAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                dialog.dismiss();
                if (parseObjects.size() > 0) {
                    if (e == null) {
                        fillPostList(parseObjects, flag);
                    } else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    fillPostList(parseObjects, flag);
                    Toast.makeText(mActivity, "No results found!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
