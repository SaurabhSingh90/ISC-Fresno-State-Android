package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomAdapters.DiscussionPostAdapter;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.view.PostDescription;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class DiscussionForum {

    private static String TAG = DiscussionForum.class.getSimpleName();
    private Activity mActivity;
    private static Context mContext;
    private PostDescription mPostDescription = new PostDescription();
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private ArrayAdapter<HashMap<String, String>> postList_adapter = null;
    private ArrayAdapter<HashMap<String, String>> deleteList_adapter = null;

    // Flag to check visibility of checkBox
    private Boolean flag;

    // Variables for delete post task
    private ArrayList<HashMap<String, String>> mPostListForDelete = null;
    private List<ParseObject> mArrOfPostObjectsToDelete;
    private ActionMode mActionMode;
    private int mDeletePostCounter = 0;
    private Boolean mZeroPostToDelete = true;
    public Boolean deleteTask = false;

    public DiscussionForum(Activity activity) {
        this.mActivity = activity;
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(mActivity);
        ContextThemeWrapper contextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(contextThemeWrapper);
        mProgressDialog.setMessage(mActivity.getString(R.string.loading_post_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
    }

    public void startLoadCommentsTask() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            mProgressDialog.show();
            flag = false;
            // Find all posts
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.POST_CLASS);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    mProgressDialog.dismiss();
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
        final int length = parseObjects.size();

        final ArrayList<HashMap<String, String>> postList;

        if (length > 0)
            postList = new ArrayList<>();
        else
            postList = null;
        ParseObject obj = null;
        for (int i = 0; i < length; i++) {
            obj = parseObjects.get(i);
            String firstName = obj.get(ParseKeys.POST_FIRST_NAME).toString();
            String title = obj.get(ParseKeys.POST_TITLE).toString();
            String tags = "TAGS: ";
            tags = tags.concat(obj.get(ParseKeys.POST_TAGS).toString());

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
            dataList.put("tags", tags);

            if (postList != null) {
                postList.add(dataList);
            }
        }
        ListView lv = (ListView) mActivity.findViewById(android.R.id.list);
        TextView empty_text = (TextView) mActivity.findViewById(android.R.id.empty);

        if (postList != null) {
            postList_adapter = new DiscussionPostAdapter(mActivity, postList, flag_for_checkbox);
            lv.setAdapter(postList_adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String objectId = parseObjects.get(position).getObjectId();
                    Intent i = new Intent(mActivity, PostDescription.class);
                    i.putExtra(ParseKeys.OBJECTID, objectId);
                    mActivity.startActivity(i);
                }
            });

        } else {
            empty_text.setVisibility(View.VISIBLE);
            lv.setEmptyView(empty_text);
        }
    }

    public void searchPostTask(final String query, final Boolean showToast) {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            flag = false;

            List<ParseQuery<ParseObject>> queries = new ArrayList<>();

            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(ParseKeys.POST_CLASS);
            query1.whereMatches("postTitle", query, "im");

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery(ParseKeys.POST_CLASS);
            query2.whereMatches("firstName", query, "im");

            ParseQuery<ParseObject> query3 = ParseQuery.getQuery(ParseKeys.POST_CLASS);
            query3.whereEqualTo("postTags", query);

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
                            fillPostList(parseObjects, flag);
                        } else {
                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        fillPostList(parseObjects, flag);
                        if (showToast)
                            Toast.makeText(mActivity, "No match found!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
    }

    public void deletePostTask() {
        deleteTask = true;
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            mProgressDialog.show();
            flag = true;
            // Find post(s) of current user only for deletion
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.POST_CLASS);
            query.whereEqualTo("user", currentUser);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    mProgressDialog.dismiss();
                    if (parseObjects.size() > 0) {
                        if (e == null) {
                            mArrOfPostObjectsToDelete = parseObjects;
                            updateList_for_delete(parseObjects, flag);
                        } else {
                            Toast.makeText(mActivity, "No post(s) to delete", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mActivity, "No post(s) to delete", Toast.LENGTH_LONG).show();
                    }
                }
            });


        } else {
            Log.d(TAG, "No network");
            mCustomNetworkErrorHandler.errorDialogDisplay(mActivity.getString(R.string.error_oops), mActivity.getString(R.string.check_network));
        }
    }

    public void updateList_for_delete(List<ParseObject> parseObjects, boolean flag_for_checkbox) {
        int length = parseObjects.size();

        if (length > 0)
            mPostListForDelete = new ArrayList<>();
        else
            mPostListForDelete = null;

        for (int i = 0; i < length; i++) {
            ParseObject obj = parseObjects.get(i);
            String firstName = obj.get(ParseKeys.POST_FIRST_NAME).toString();
            String title = obj.get(ParseKeys.POST_TITLE).toString();
            String tags = "TAGS: ";
            tags = tags.concat(obj.get(ParseKeys.POST_TAGS).toString());

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
            dataList.put("tags", tags);
            mPostListForDelete.add(dataList);
        }

        ListView lv = (ListView) mActivity.findViewById(android.R.id.list);
        if (mPostListForDelete != null) {
            deleteList_adapter = new DiscussionPostAdapter(mActivity, mPostListForDelete, flag_for_checkbox);
            lv.setAdapter(deleteList_adapter);
        } else {
            TextView empty_text = (TextView) mActivity.findViewById(android.R.id.empty);
            lv.setEmptyView(empty_text);
        }
    }

    public ActionMode.Callback ActionBarCallBack() {
        return new ActionBarCallBack();
    }


    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.delete_sign) {
                mDeletePostCounter = 0;

                final ProgressDialog dialog = new ProgressDialog(mActivity);
                dialog.setMessage("Deleting posts...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(true);

                if (mPostListForDelete != null) {
                    for(int i = 0; i < mPostListForDelete.size(); i++) {
                        if (DiscussionPostAdapter.mArrayForCheckMarks[i]) {
                            mZeroPostToDelete = false;
                            break;
                        } else
                            mZeroPostToDelete = true;
                    }

                    if (mZeroPostToDelete)
                        Toast.makeText(mActivity, "Select a post first", Toast.LENGTH_SHORT).show();
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mActivity,android.R.style.Theme_Holo_Dialog));
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(mActivity.getString(R.string.delete_title_text))
                                .setMessage(mActivity.getString(R.string.delete_message_text))
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface d, int id) {
                                        dialog.show();
                                        final List<ParseObject> tempArr = new ArrayList<>(mPostListForDelete.size());
                                        for (int i = 0; i < mPostListForDelete.size(); i++) {
                                            if (DiscussionPostAdapter.mArrayForCheckMarks[i]) {
                                                mZeroPostToDelete = false;
                                                mDeletePostCounter++;
                                                tempArr.add(mArrOfPostObjectsToDelete.get(i));
                                            }
                                        }
                                        ParseObject.deleteAllInBackground(tempArr, new DeleteCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                dialog.dismiss();
                                                if (e == null) {
                                                    if (mDeletePostCounter > 1)
                                                        Toast.makeText(mActivity, (mDeletePostCounter) + " posts deleted", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(mActivity, (mDeletePostCounter) + " post deleted", Toast.LENGTH_SHORT).show();
                                                    mActionMode.finish();
                                                } else {
                                                    Toast.makeText(mActivity, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).show();
                    }
                } else {
                    Toast.makeText(mActivity, "No post to delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            if (id == R.id.mark_all) {
                ListView lv = (ListView)mActivity.findViewById(android.R.id.list);
                deleteList_adapter = new DiscussionPostAdapter(mActivity, mPostListForDelete, true);

                if (item.isChecked()) {
                    item.setChecked(false);
                    item.setIcon(android.R.drawable.checkbox_off_background);
                    DiscussionPostAdapter.unMarkAll();
                    lv.setAdapter(deleteList_adapter);
                } else {
                    item.setChecked(true);
                    item.setIcon(android.R.drawable.checkbox_on_background);
                    DiscussionPostAdapter.markAll();
                    lv.setAdapter(deleteList_adapter);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu, menu);
            
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            deleteTask = false;
            startLoadCommentsTask();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(R.string.title_for_delete);
            return false;
        }
    }
}
