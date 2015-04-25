package singh.saurabh.iscfresnostate.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class EditPost extends ActionBarActivity {

    private Context mContext = this;
    private String objectId;
    private String mTitle, mMessage, mTag;
    private View focusView = null;
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;

    // Parse Column Names
    private String POST_TITLE = "postTitle";
    private String POST_CONTENT = "postContent";
    private String POST_TAGS = "postTags";

    @InjectView(R.id.edit_Post_Title) TextView mTitleEditText;
    @InjectView(R.id.edit_message) TextView mContentEditText;
    @InjectView(R.id.edit_tag) TextView mTagEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.editing_post_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            objectId = extras.getString("objectId");
            mTitle = extras.getString(POST_TITLE);
            mMessage = extras.getString(POST_CONTENT);
            mTag = extras.getString(POST_TAGS);
        }

        mTitleEditText.setText(mTitle);
        mContentEditText.setText(mMessage);
        mTagEditText.setText(mTag);
    }

    public void SubmitEditPost(View v) {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            // Resetting errors for both editText
            mTitleEditText.setError(null);
            mContentEditText.setError(null);
            mTagEditText.setError(null);

            if (mTitleEditText.length() == 0) {
                mTitleEditText.setError(getString(R.string.error_field_required));
                focusView = mTitleEditText;
            }
            if (mContentEditText.length() == 0) {
                mContentEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mContentEditText;
            }
            if (mTagEditText.length() == 0) {
                mTagEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mTagEditText;
            }

            if (mTitleEditText.length() == 0 || mContentEditText.length() == 0 || mTagEditText.length() == 0)
                focusView.requestFocus();
            else
                postEditedComment();
        } else {
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
        }
    }

    private void postEditedComment() {
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    parseObject.put(POST_TITLE, mTitleEditText.getText().toString());
                    parseObject.put(POST_CONTENT, mContentEditText.getText().toString());
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(mContext, getString(R.string.post_updated_text), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(mContext, PostDescription.class);
                                i.putExtra("objectId", objectId);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data));
                }
            }
        });
    }
}
