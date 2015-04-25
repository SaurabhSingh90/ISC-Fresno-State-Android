package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.model.ParseKeys;

public class AddNewJobPost extends ActionBarActivity {

    private static String TAG = AddNewPost.class.getSimpleName();
    private Activity mContext = this;
    private static ParseUser mCurrentUser = new LoginActivity().mCurrentUser;
    private static String firstName = mCurrentUser.getString("firstName");
    private View focusView = null;
    private String postChannel = "Post_";
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog dialog;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();

    @InjectView(R.id.job_title_editText)
    EditText mTitleEditText;
    @InjectView(R.id.job_location_editText)
    EditText mLocationEditText;
    @InjectView(R.id.job_postContent_editText)
    EditText mPostContentEditText;
    @InjectView(R.id.job_post_tags_editText)
    EditText mPostTagsEditText;
    @InjectView(R.id.submit_job_post_button)
    Button mSubmitPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        dialog = new ProgressDialog(mContextThemeWrapper);
        dialog.setMessage(getString(R.string.adding_post_text));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        mSubmitPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mSubmitPostButton.getWindowToken(), 0);
                startSubmitPostTask();
            }
        });

    }

    private void startSubmitPostTask() {
        mTitleEditText.setError(null);
        mLocationEditText.setError(null);
        mPostContentEditText.setError(null);
        mPostTagsEditText.setError(null);
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            if (mTitleEditText.length() == 0) {
                mTitleEditText.setError(getString(R.string.error_field_required));
                focusView = mTitleEditText;
            }
            if (mLocationEditText.length() == 0) {
                mLocationEditText.setError(getString(R.string.error_field_required));
                focusView = mLocationEditText;
            }
            if (mPostContentEditText.length() == 0) {
                mPostContentEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mPostContentEditText;
            }
            if (mPostTagsEditText.length() == 0) {
                mPostTagsEditText.setError(getString(R.string.error_post_tag_required));
                if (focusView == null)
                    focusView = mPostTagsEditText;
            }
            if (mTitleEditText.length() == 0 || mLocationEditText.length() == 0 || mPostContentEditText.length() == 0 || mPostTagsEditText.length() == 0)
                focusView.requestFocus();
            else
                submitPost();
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    private void submitPost() {
        dialog.show();
        String titleString = mTitleEditText.getText().toString().trim();
        String locationString = mLocationEditText.getText().toString().trim();
        String contentString = mPostContentEditText.getText().toString().trim();
        String tagsString = mPostTagsEditText.getText().toString().trim();

        // Make a new post
        final ParseObject post = new ParseObject(ParseKeys.JOBPOST_CLASS);
        post.put(ParseKeys.JOBPOST_TITLE, titleString);
        post.put(ParseKeys.JOBPOST_LOCATION, locationString);
        post.put(ParseKeys.JOBPOST_CONTENT, contentString);
        String[] tagsArray = tagsString.split(",");
        for (String tag : tagsArray) {
            tag = tag.toLowerCase().trim();
            post.add(ParseKeys.JOBPOST_TAGS, tag);
        }
        post.put(ParseKeys.POST_USER, mCurrentUser);
        post.put(ParseKeys.JOBPOST_FIRST_NAME, firstName);
        post.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    postChannel = postChannel.concat(post.getObjectId());
                    ParsePush.subscribeInBackground(postChannel);
                    mMenuScreenActivity.new RefreshNewsList().execute();
                    finish();
                    Toast.makeText(mContext, getString(R.string.post_added_text), Toast.LENGTH_SHORT).show();
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_job_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}