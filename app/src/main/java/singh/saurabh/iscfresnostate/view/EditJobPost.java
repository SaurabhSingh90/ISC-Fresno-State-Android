package singh.saurabh.iscfresnostate.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
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
import singh.saurabh.iscfresnostate.model.ParseKeys;

public class EditJobPost extends ActionBarActivity {

    private Context mContext = this;
    private String objectId;
    private String mTitle, mLocation, mMessage;
    private String[] mTagsArray;
    private View focusView = null;
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;

    @InjectView(R.id.editJobPost_Title)
    EditText mTitleEditText;
    @InjectView(R.id.editJobPost_Location)
    EditText mLocationEditText;
    @InjectView(R.id.editJobPost_Content)
    EditText mContentEditText;
    @InjectView(R.id.editJobPost_Tags)
    EditText mTagEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.editing_post_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            objectId = extras.getString(ParseKeys.OBJECTID);
            mTitle = extras.getString(ParseKeys.JOBPOST_TITLE);
            mLocation = extras.getString(ParseKeys.JOBPOST_LOCATION);
            mMessage = extras.getString(ParseKeys.JOBPOST_CONTENT);
            mTagsArray = extras.getStringArray(ParseKeys.JOBPOST_TAGS);
        }

        mTitleEditText.setText(mTitle);
        mLocationEditText.setText(mLocation);
        mContentEditText.setText(mMessage);
        String str = "";
        for (String string: mTagsArray)
            str = str.concat(" "+string+",");
        mTagEditText.setText(str);
    }

    public void SubmitEditJobPost(View v) {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            // Resetting errors for both editText
            mTitleEditText.setError(null);
            mLocationEditText.setError(null);
            mContentEditText.setError(null);
            mTagEditText.setError(null);

            if (mTitleEditText.length() == 0) {
                mTitleEditText.setError(getString(R.string.error_field_required));
                focusView = mTitleEditText;
            }
            if (mLocationEditText.length() == 0) {
                mLocationEditText.setError(getString(R.string.error_field_required));
                focusView = mLocationEditText;
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

            if (mTitleEditText.length() == 0 || mLocationEditText.length() == 0 || mContentEditText.length() == 0 || mTagEditText.length() == 0)
                focusView.requestFocus();
            else
                postEditedJobPost();
        } else {
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
        }
    }

    private void postEditedJobPost() {
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.JOBPOST_CLASS);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    parseObject.put(ParseKeys.JOBPOST_TITLE, mTitleEditText.getText().toString());
                    parseObject.put(ParseKeys.JOBPOST_LOCATION, mLocationEditText.getText().toString());
                    parseObject.put(ParseKeys.JOBPOST_CONTENT, mContentEditText.getText().toString());
                    String tagsString = mTagEditText.getText().toString().trim();
                    String[] tagsArray = tagsString.split(",");
                    for (String tag : tagsArray) {
                        tag = tag.toLowerCase().trim();
                        parseObject.addUnique(ParseKeys.JOBPOST_TAGS, tag);
                    }
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(mContext, getString(R.string.post_updated_text), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(mContext, JobDescription.class);
                                i.putExtra(ParseKeys.OBJECTID, objectId);
                                startActivity(i);
                                finish();
                            } else
                                mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data)+" "+e.getMessage());
                        }
                    });
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data));
                }
            }
        });
    }

}
