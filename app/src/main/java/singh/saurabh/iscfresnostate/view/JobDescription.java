package singh.saurabh.iscfresnostate.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.model.ParseKeys;

public class JobDescription extends ActionBarActivity {

    private static String TAG = PostDescription.class.getSimpleName();
    private Context mContext = this;
    private String objectId;
    private ParseObject postObject;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog mProgressDialog;

    @InjectView(R.id.singleJobPost_title_textView)
    TextView mTitle;
    @InjectView(R.id.singleJobPost_location_textView)
    TextView mLocation;
    @InjectView(R.id.singleJobPost_firstName_textView)
    TextView mFirstName;
    @InjectView(R.id.singleJobPost_date_textView)
    TextView mDate;
    @InjectView(R.id.singleJobPost_tag_textView)
    TextView mTag;
    @InjectView(R.id.singleJobPost_content_textView)
    TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);
        ButterKnife.inject(this);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            objectId = extras.getString(ParseKeys.OBJECTID);
        }
        loadJobPost();
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

    public void loadJobPost() {
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.JOBPOST_CLASS);
        query.whereEqualTo(ParseKeys.OBJECTID, objectId);
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
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                    posted_on = sdf.format(d1);

                    restoreActionBar(parseObject.get(ParseKeys.JOBPOST_TITLE).toString().toUpperCase().toUpperCase());

                    mTitle.setText(parseObject.get(ParseKeys.JOBPOST_TITLE).toString().toUpperCase());
                    mLocation.setText(parseObject.get(ParseKeys.JOBPOST_LOCATION).toString());
                    mFirstName.setText("Posted By: " + parseObject.get(ParseKeys.JOBPOST_FIRST_NAME).toString());
                    mDate.setText("on: " + posted_on);
                    mTag.setText("TAGS: " + parseObject.get(ParseKeys.JOBPOST_TAGS).toString());
                    mContent.setText(parseObject.get(ParseKeys.JOBPOST_CONTENT).toString());

                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.post_not_available_text));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
}
