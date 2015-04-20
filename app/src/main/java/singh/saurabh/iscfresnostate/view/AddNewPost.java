package singh.saurabh.iscfresnostate.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddNewPost extends Activity {

    private static String TAG = AddNewPost.class.getSimpleName();
    private Context mContext = this;
    public ContextThemeWrapper mContextThemeWrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog);

    @InjectView(R.id.title_editText)
    EditText mTitleEditext;
    @InjectView(R.id.postContent_editText)
    EditText mPostContentEditText;
    @InjectView(R.id.post_tags_editText)
    EditText mPostTagsEditText;
    @InjectView(R.id.submit_post_button)
    Button mSubmitPostButton;
    @InjectView(R.id.add_new_post_container)
    View mAddPostContainer;
    @InjectView(R.id.addNewPost_progressBar)
    View mProgressView;

    private static ParseUser mCurrentUser = new LoginActivity().mCurrentUser;
    private static String firstName = mCurrentUser.getString("firstName");
    private View focusView = null;
    private String postChannel = "Post_";

    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);

        mPostTagsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_add_post || id == EditorInfo.IME_NULL) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mPostTagsEditText.getWindowToken(), 0);
                    startSubmitPostTask();
                    return true;
                }
                return false;
            }
        });

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
        mTitleEditext.setError(null);
        mPostContentEditText.setError(null);
        mPostTagsEditText.setError(null);
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            if (mTitleEditext.length() == 0) {
                mTitleEditext.setError(getString(R.string.error_field_required));
                focusView = mTitleEditext;
            } else if (mPostContentEditText.length() == 0) {
                mPostContentEditText.setError(getString(R.string.error_field_required));
                focusView = mPostContentEditText;
            } else if (mPostTagsEditText.length() == 0) {
                mPostTagsEditText.setError(getString(R.string.error_post_tag_required));
                focusView = mPostTagsEditText;
            } else {
                submitPost();
            }
            if (mTitleEditext.length() == 0 || mPostContentEditText.length() == 0 || mPostTagsEditText.length() == 0)
                focusView.requestFocus();
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    private void submitPost() {
        showProgress(true);
        String titleString = mTitleEditext.getText().toString().trim();
        String messageString = mPostContentEditText.getText().toString().trim();
        String tagsString = mPostTagsEditText.getText().toString().trim();

        // Make a new post
        final ParseObject post = new ParseObject("Post");
        post.put("postTitle", titleString);
        post.put("postContent", messageString);
        String[] tagsArray = tagsString.split(",");
        for (String tag : tagsArray) {
            tag = tag.toLowerCase().trim();
            post.add("postTags", tag);
        }
        post.put("user", mCurrentUser);
        post.put("firstName", firstName);
        post.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                showProgress(false);
                if ( e == null ) {
                    postChannel = postChannel.concat(post.getObjectId());
                    ParsePush.subscribeInBackground(postChannel);
                    finish();
                    Toast.makeText(mContext, "Post added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_post, menu);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddPostContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddPostContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddPostContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddPostContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
