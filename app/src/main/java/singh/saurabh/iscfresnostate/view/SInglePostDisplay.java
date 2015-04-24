package singh.saurabh.iscfresnostate.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class SinglePostDisplay extends ActionBarActivity {

    private static String TAG = SinglePostDisplay.class.getSimpleName();
    private Context mContext = this;
    private String objectId;
    private ParseObject postObject;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog mProgressDialog;
    public ActionMode mActionMode;
    private ArrayAdapter<HashMap<String, String>> mReplyListArrayAdapter;

    // Channels for notification system
    private String postChannel;
    private String replyChannel;

    // variables for delete
    private int mDeleteReplyCounter = 0;
    private Boolean mZeroPostToDelete = true;
    private Boolean[] mArrayForCheckMarks;
    private ArrayList<HashMap<String, String>> mReplyList;
    private List<ParseObject> mArrOfReplyObjectsToDelete;

    // Parse Column Names
    private String POST_TITLE = "postTitle";
    private String POST_CONTENT = "postContent";
    private String POST_FIRST_NAME = "firstName";
    private String POST_TAGS = "postTags";
    private View mReplyListHeaderView;
    private View mReplyListFooterView;

    @InjectView(R.id.singlePost_title_textView)
    TextView mTitle;
    @InjectView(R.id.singlePost_firstName_textView)
    TextView mFirstName;
    @InjectView(R.id.singlePost_date_textView)
    TextView mDate;
    @InjectView(R.id.singlePost_tag_textView)
    TextView mTag;
    @InjectView(R.id.list_for_replies)
    ListView mListView;
    private TextView mContent;
    private EditText mReplyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_display);
        ButterKnife.inject(this);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mReplyListHeaderView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reply_list_header, null, false);
        mReplyListFooterView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reply_list_footer, null, false);

        mContent = (TextView) mReplyListHeaderView.findViewById(R.id.singlePost_content_textView);
        mReplyEditText = (EditText) mReplyListFooterView.findViewById(R.id.editText_reply);

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            objectId = extras.getString("objectId");
        }
        postChannel = "Post_" + objectId;
        replyChannel = "Reply_" + objectId;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    postObject = parseObject;
                }
            }
        });

        loadPost();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void restoreActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    public void loadPost() {
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    Date createdAt = parseObject.getCreatedAt();
                    String posted_on = createdAt.toString();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy", Locale.US);
                    Date d1 = null;
                    try {
                        d1 = sdf1.parse(posted_on);
                    } catch (java.text.ParseException ee) {
                        ee.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy   h:mm a", Locale.US);
                    posted_on = sdf.format(d1);

                    mTitle.setText(parseObject.get(POST_TITLE).toString());
                    mFirstName.setText(parseObject.get(POST_FIRST_NAME).toString());
                    mDate.setText(posted_on);
                    mTag.setText("TAGS: " + parseObject.get(POST_TAGS).toString());
                    mContent.setText(parseObject.get(POST_CONTENT).toString());

                    restoreActionBar(mTitle.getText().toString().toUpperCase());
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.post_not_available_text));
                }
            }
        });
        populateReplyList();
    }

    public void AddReply(View v) {
        mReplyEditText.setError(null);
        if (mReplyEditText.length() == 0) {
            mReplyEditText.setError(getString(R.string.error_field_required));
            View focusView = mReplyEditText;
            focusView.requestFocus();
        } else {

            final ParseUser currentUser = ParseUser.getCurrentUser();
            String message = mReplyEditText.getText().toString();
            final String firstName = currentUser.get("firstName").toString();
            final ParseObject replyPostObject = new ParseObject("Replies");
            replyPostObject.put("firstName", currentUser.get("firstName").toString());
            replyPostObject.put("replyMessage", message);
            replyPostObject.put("replyUser", currentUser);
            replyPostObject.put("parent", objectId);
            replyPostObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(mContext, "Reply successfully added", Toast.LENGTH_SHORT).show();
                        if (postObject.getParseObject("user").getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) != 0) {
                            ParseInstallation pi = ParseInstallation.getCurrentInstallation();
                            pi.put("firstName", currentUser.get("firstName").toString());
                            ParsePush.subscribeInBackground(replyChannel, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("Success", "subscribing");
                                    } else
                                        Log.d("Error", e.getMessage());
                                }
                            });
                            pi.saveEventually();
                            String piObjectId = pi.getObjectId();
                            sendNotification(postChannel, firstName);
                            sendNotificationWithQuery(replyChannel, firstName, piObjectId);
                        } else {
                            sendNotificationWithQuery(replyChannel, firstName, "0");
                        }

                        Intent i = new Intent(mContext, SinglePostDisplay.class);
                        i.putExtra("objectId", objectId);
                        finish();
                        startActivity(i);
                    } else {
                        mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), e.getMessage());
                    }
                }
            });
        }
    }

    private void sendNotification(String channel, String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channel", channel);
        map.put("firstName", name);
        map.put("objectId", objectId);
        ParseCloud.callFunctionInBackground("pushNotification", map);
    }

    private void sendNotificationWithQuery(String channel, String name, String piObjectId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channel", channel);
        map.put("firstName", name);
        map.put("piObjectId", piObjectId);
        map.put("objectId", objectId);
        ParseCloud.callFunctionInBackground("pushNotificationWithQuery", map);
    }

    private void populateReplyList() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
        // Find all posts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Replies");
        query.whereEqualTo("parent", objectId);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    mArrOfReplyObjectsToDelete = parseObjects;
                    int length = parseObjects.size();

                    if (length > 0) {
                        mReplyList = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            ParseObject obj = parseObjects.get(i);
                            String firstName = obj.get("firstName").toString();
                            String replyMessage = obj.get("replyMessage").toString();
                            ParseUser replyUserId = obj.getParseUser("replyUser");
                            Date createdAt = obj.getCreatedAt();
                            String posted_on = createdAt.toString();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy", Locale.US);
                            Date d1 = null;
                            try {
                                d1 = sdf1.parse(posted_on);
                            } catch (java.text.ParseException ee) {
                                ee.printStackTrace();
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy   h:mm a", Locale.US);
                            posted_on = sdf.format(d1);

                            HashMap<String, String> dataList = new HashMap<>();
                            dataList.put("author", firstName);
                            dataList.put("title", replyMessage);
                            dataList.put("createdAt", posted_on);
                            dataList.put("replyUser", replyUserId.getObjectId());
                            mReplyList.add(dataList);
                        }
                    } else
                        mReplyList = null;

                    if (mReplyList != null) {
                        mReplyListArrayAdapter = new ReplyListCustomAdapter(mContext, mReplyList);
                        if (mListView.getHeaderViewsCount() == 0)
                            mListView.addHeaderView(mReplyListHeaderView);
                        if (mListView.getFooterViewsCount() == 0)
                            mListView.addFooterView(mReplyListFooterView);
                        mListView.setAdapter(mReplyListArrayAdapter);
                        mListView.setOnTouchListener(new ListView.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                int action = event.getAction();
                                switch (action) {
                                    case MotionEvent.ACTION_DOWN:
                                        // Disallow ScrollView to intercept touch events.
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;

                                    case MotionEvent.ACTION_UP:
                                        // Allow ScrollView to intercept touch events.
                                        v.getParent().requestDisallowInterceptTouchEvent(false);
                                        break;
                                }
                                // Handle ListView touch events.
                                v.onTouchEvent(event);
                                return true;
                            }
                        });
                    } else {
                        ArrayList<HashMap<String, String>> temp = new ArrayList<>();
                        HashMap<String, String> dataList = new HashMap<>();
                        dataList.put("firstName", "");
                        temp.add(dataList);
                        String[] keys = {""};
                        int[] ids = {android.R.id.text1};
                        SimpleAdapter adapter = new SimpleAdapter(mContext, temp, android.R.layout.simple_list_item_1, keys, ids);
                        if (mListView.getHeaderViewsCount() == 0)
                            mListView.addHeaderView(mReplyListHeaderView);
                        if (mListView.getFooterViewsCount() == 0)
                            mListView.addFooterView(mReplyListFooterView);
                        mListView.setAdapter(adapter);
                        mListView.setFooterDividersEnabled(false);
                    }
                } else {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_post_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit_post) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        if (parseObject.getParseObject("user").getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) == 0) {
                            Intent i = new Intent(mContext, EditPost.class);
                            i.putExtra("objectId", objectId);
                            i.putExtra(POST_TITLE, mTitle.getText().toString());
                            i.putExtra(POST_CONTENT, mContent.getText().toString());
                            i.putExtra(POST_TAGS, mTag.getText().toString());
                            finish();
                            startActivity(i);
                        } else {
                            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.edit_not_authorized));
                        }
                    }
                }
            });

            return true;
        }

        if (id == R.id.action_refresh) {
            loadPost();
            return true;
        }

        if (id == R.id.action_delete_post) {
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Deleting post...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Dialog));
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.delete_title_text))
                    .setMessage(getString(R.string.delete_message_text))
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, int id) {
                            dialog.show();
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
                            query.whereEqualTo("objectId", objectId);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        if (parseObject.getParseObject("user").getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) == 0) {
                                            parseObject.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    dialog.dismiss();
                                                    if (e == null) {
                                                        ParsePush.unsubscribeInBackground(postChannel);
                                                        mMenuScreenActivity.new RefreshNewsList().execute();
                                                        finish();
                                                    } else {
                                                        mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.some_error_occurred));
                                                    }
                                                }
                                            });
                                        } else {
                                            dialog.dismiss();
                                            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.delete_not_authorized));
                                        }
                                    } else {
                                        dialog.dismiss();
                                        mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.some_error_occurred));
                                    }
                                }
                            });
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();

            return true;
        }

        if (id == R.id.action_sign_out) {
            ProgressDialog dialog;
            dialog = new ProgressDialog(mContextThemeWrapper);
            dialog.setMessage(getString(R.string.signing_out_dialog_message));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
            ParseUser.logOut();
            dialog.dismiss();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.delete_sign) {
                mDeleteReplyCounter = 0;

                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage("Deleting posts...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(true);

                if (mReplyList != null) {
                    for (int i = 0; i < mReplyList.size(); i++) {
                        if (mArrayForCheckMarks[i]) {
                            mZeroPostToDelete = false;
                            break;
                        } else
                            mZeroPostToDelete = true;
                    }

                    if (mZeroPostToDelete)
                        Toast.makeText(mContext, "Select a reply first", Toast.LENGTH_SHORT).show();
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Dialog));
                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(getString(R.string.delete_title_text))
                                .setMessage(getString(R.string.delete_message_text))
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface d, int id) {
                                        dialog.show();
                                        final List<ParseObject> tempArr = new ArrayList<>(mReplyList.size());
                                        for (int i = 0; i < mReplyList.size(); i++) {
                                            if (mArrayForCheckMarks[i]) {
                                                mZeroPostToDelete = false;
                                                mDeleteReplyCounter++;
                                                tempArr.add(mArrOfReplyObjectsToDelete.get(i));
                                            }
                                        }
                                        ParseObject.deleteAllInBackground(tempArr, new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                dialog.dismiss();
                                                if (e == null) {
                                                    if (mDeleteReplyCounter > 1)
                                                        Toast.makeText(mContext, (mDeleteReplyCounter) + " posts deleted", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(mContext, (mDeleteReplyCounter) + " post deleted", Toast.LENGTH_SHORT).show();
                                                    mActionMode.finish();
                                                } else {
                                                    Toast.makeText(mContext, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "No post to delete", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            if (id == R.id.mark_all) {
                mReplyListArrayAdapter = new ReplyListCustomAdapter(mContext, mReplyList);

                if (item.isChecked()) {
                    item.setChecked(false);
                    item.setIcon(android.R.drawable.checkbox_off_background);
                    for (int i = 0; i < mArrayForCheckMarks.length; i++)
                        mArrayForCheckMarks[i] = false;
                    mListView.setAdapter(mReplyListArrayAdapter);
                } else {
                    item.setChecked(true);
                    item.setIcon(android.R.drawable.checkbox_on_background);
                    for (int i = 0; i < mArrayForCheckMarks.length; i++)
                        mArrayForCheckMarks[i] = true;
                    mListView.setAdapter(mReplyListArrayAdapter);
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
            mActionMode = null;
            loadPost();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(R.string.title_for_delete);
            return false;
        }
    }

    public class ReplyListCustomAdapter extends ArrayAdapter<HashMap<String, String>> {

        private Context mContext;
        private ArrayList<HashMap<String, String>> mList;
        private int j = 0;

        public ReplyListCustomAdapter(Context context, ArrayList<HashMap<String, String>> postList) {
            super(context, R.layout.single_postlist_item, postList);
            mContext = context;
            mList = postList;
            mArrayForCheckMarks = new Boolean[mList.size()];
            for (int i = 0; i < mArrayForCheckMarks.length; i++)
                mArrayForCheckMarks[i] = false;
        }


        private class ViewHolder {
            protected View postTagContainer;
            protected TextView firstName, title, published_date, postTags;
            protected CheckBox checkbox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                // brand new view
                convertView = LayoutInflater.from(mContext).inflate(R.layout.single_postlist_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title_single_list_item);
                holder.firstName = (TextView) convertView.findViewById(R.id.author_name_single_list_item);
                holder.published_date = (TextView) convertView.findViewById(R.id.date_single_list_item);
                holder.postTags = (TextView) convertView.findViewById(R.id.tags_single_list_item);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_single_list_item);
                holder.postTagContainer = convertView.findViewById(R.id.tags_container);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> listItem = mList.get(position);

            // item clicked for replies
            if (listItem.get("replyUser") != null) {
                if (listItem.get("replyUser").equals(ParseUser.getCurrentUser().getObjectId())) {
                    holder.checkbox.setVisibility(View.VISIBLE);
                    holder.checkbox.setChecked(mArrayForCheckMarks[position]);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mActionMode == null)
                                mActionMode = startActionMode(new ActionBarCallBack());

                            if (!mArrayForCheckMarks[position])
                                mArrayForCheckMarks[position] = true;
                            else
                                mArrayForCheckMarks[position] = false;
                            holder.checkbox.setChecked(mArrayForCheckMarks[position]);

                            for (int i = 0; i < mList.size(); i++) {
                                if (mArrayForCheckMarks[i]) {
                                    j = 1;
                                    break;
                                } else {
                                    j = 0;
                                }
                            }
                            if (j == 0) {
                                if (mActionMode != null) {
                                    mActionMode.finish();
                                    mActionMode = null;
                                }
                            }
                        }
                    });
                } else
                    holder.checkbox.setVisibility(View.INVISIBLE);
            }

            holder.firstName.setText(listItem.get("author"));
            holder.title.setText(listItem.get("title"));
            holder.published_date.setText(listItem.get("createdAt"));

            return convertView;
        }
    }
}
