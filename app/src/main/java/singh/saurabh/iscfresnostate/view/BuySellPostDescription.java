package singh.saurabh.iscfresnostate.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.model.ParseKeys;

public class BuySellPostDescription extends ActionBarActivity {

    private static String TAG = BuySellPostDescription.class.getSimpleName();
    private Context mContext = this;
    private String mObjectId;
    private ParseObject postObject;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog mProgressDialog;
    private String[] mTagsArray;

    private LinearLayout.LayoutParams layoutParams;
    private ImageView mImageView;
    private String[] mImageUrlArray;

    @InjectView(R.id.sell_title)
    TextView mTitleTextView;
    @InjectView(R.id.sell_price)
    TextView mPriceTextView;
    @InjectView(R.id.sell_location)
    TextView mLocationTextView;
    @InjectView(R.id.sell_by)
    TextView mSellByTextView;
    @InjectView(R.id.sell_date)
    TextView mSellDateTextView;
    @InjectView(R.id.sell_tags)
    TextView mTagsTextView;
    @InjectView(R.id.sell_description)
    TextView mDescriptionTextView;
    @InjectView(R.id.images_LinearLayout)
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_post_description);
        ButterKnife.inject(this);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams = new LinearLayout.LayoutParams(width, height);

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mObjectId = extras.getString(ParseKeys.OBJECTID);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.BUY_SELL_CLASS);
        query.getInBackground(mObjectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    postObject = parseObject;
                }
            }
        });
        if (mCustomNetworkErrorHandler.isNetworkAvailable())
            loadPost();
        else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
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

    private void loadPost() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.BUY_SELL_CLASS);
        query.whereEqualTo(ParseKeys.OBJECTID, mObjectId);
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

                    restoreActionBar(parseObject.get(ParseKeys.BUY_SELL_TITLE).toString().toUpperCase());

                    JSONArray imagesArray = parseObject.getJSONArray(ParseKeys.BUY_SELL_FILE);
                    if (imagesArray != null) {
                        mImageUrlArray = new String[imagesArray.length()];
                        for (int i = 0; i < imagesArray.length(); i++) {
                            JSONObject jsonObject = null;
                            String mImageUrl = "";
                            try {
                                jsonObject = imagesArray.getJSONObject(i);
                                mImageUrl = jsonObject.get("url").toString();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            mImageUrlArray[i] = mImageUrl;
                            mImageView = new ImageView(mContext);
                            mImageView.setId(i);
                            mImageView.setClickable(true);
                            mImageView.setLayoutParams(layoutParams);
                            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            mImageView.setAdjustViewBounds(true);
                            mImageView.setPadding(0, 10, 10, 10);
                            Picasso.with(mContext).load(mImageUrl).into(mImageView);
                            mLinearLayout.addView(mImageView);
                            mImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, ItemImageView.class);
                                    intent.putExtra("imageUrl", mImageUrlArray[v.getId()]);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    mTitleTextView.setText(parseObject.get(ParseKeys.BUY_SELL_TITLE).toString());
                    mPriceTextView.setText(parseObject.get(ParseKeys.BUY_SELL_PRICE).toString());
                    mLocationTextView.setText(parseObject.get(ParseKeys.BUY_SELL_LOCATION).toString());
                    mSellByTextView.setText(parseObject.get(ParseKeys.BUY_SELL_FIRST_NAME).toString());
                    mSellDateTextView.setText(posted_on);
                    mTagsTextView.setText("TAGS: " + parseObject.get(ParseKeys.BUY_SELL_TAGS).toString());
                    mDescriptionTextView.setText(parseObject.get(ParseKeys.BUY_SELL_CONTENT).toString());

                    JSONArray tagsArray = parseObject.getJSONArray(ParseKeys.BUY_SELL_TAGS);
                    mTagsArray = new String[tagsArray.length()];
                    for (int i = 0; i < tagsArray.length(); i++) {
                        try {
                            mTagsArray[i] = tagsArray.getString(i);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.post_not_available_text));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy_sell_post_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_seller) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseKeys.BUY_SELL_CLASS);
            query.getInBackground(mObjectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        postObject = parseObject;
                        ParseUser user = postObject.getParseUser("user");
                        user.fetchInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    Intent i = new Intent(Intent.ACTION_SENDTO);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Query regarding your item for sale");
                                    i.putExtra(Intent.EXTRA_TEXT, "");
                                    i.setData(Uri.parse("mailto:" + parseObject.getString("email")));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(Intent.createChooser(i, "Send mail..."));
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            });

            return true;
        }
        if (id == R.id.action_delete) {
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
                            if (postObject.getParseObject("user").getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) == 0) {
                                postObject.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        dialog.dismiss();
                                        if (e == null) {
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
}
