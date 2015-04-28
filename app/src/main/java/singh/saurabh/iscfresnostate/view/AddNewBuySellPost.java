package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;
import singh.saurabh.iscfresnostate.controller.FileHelper;
import singh.saurabh.iscfresnostate.model.ParseKeys;

public class AddNewBuySellPost extends ActionBarActivity {

    private static String TAG = AddNewBuySellPost.class.getSimpleName();
    private Activity mContext = this;
    private String objectId;
    private static ParseUser mCurrentUser = ParseUser.getCurrentUser();
    private static String firstName = mCurrentUser.getString("firstName");
    private View focusView = null;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog mProgressDialog;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();

    @InjectView(R.id.buy_sell_title_editText)
    EditText mTitleEditText;
    @InjectView(R.id.buy_sell_price_editText)
    EditText mPriceEditText;
    @InjectView(R.id.buy_sell_location_editText)
    EditText mLocationEditText;
    @InjectView(R.id.buy_sell_tags_editText)
    EditText mTagEditText;
    @InjectView(R.id.buy_sell_description_editText)
    EditText mDescriptionEditText;
    @InjectView(R.id.submit_buy_sell_post)
    Button mSubmitPost;
    @InjectView(R.id.description_container)
    LinearLayout mLinearLayout;
    @InjectView(R.id.buy_sell_horizontalScrollView)
    HorizontalScrollView mHorizontalScrollView;

    // Request code for image capturing
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int PICK_PHOTO_REQUEST = 1;

    private Uri[] mMediaUri = new Uri[6];
    private String[] mFileName = new String[6];
    private ParseFile[] file = new ParseFile[6];
    private byte[] mFileBytes;
    private static int mImageIdIndex = 0;
    private LinearLayout.LayoutParams layoutParams;
    private Bitmap mBitmap = null;
    private ImageView mImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_buy_sell_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams = new LinearLayout.LayoutParams(width, height);

        for (int i = 0; i < 6; i++)
            file[i] = null;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.adding_post_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);

        mSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mSubmitPost.getWindowToken(), 0);
                startSubmitPostTask();
            }
        });
    }

    private void startSubmitPostTask() {
        mTitleEditText.setError(null);
        mPriceEditText.setError(null);
        mLocationEditText.setError(null);
        mTagEditText.setError(null);
        mDescriptionEditText.setError(null);

        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            if (mTitleEditText.length() == 0) {
                mTitleEditText.setError(getString(R.string.error_field_required));
                focusView = mTitleEditText;
            }
            if (mPriceEditText.length() == 0) {
                mPriceEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mPriceEditText;
            }
            if (mLocationEditText.length() == 0) {
                mLocationEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mLocationEditText;
            }
            if (mTagEditText.length() == 0) {
                mTagEditText.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mTagEditText;
            }
            if (mDescriptionEditText.length() == 0) {
                mDescriptionEditText.setError(getString(R.string.error_post_tag_required));
                if (focusView == null)
                    focusView = mDescriptionEditText;
            }
            if (mTitleEditText.length() == 0
                    || mPriceEditText.length() == 0
                    || mLocationEditText.length() == 0
                    || mTagEditText.length() == 0
                    || mDescriptionEditText.length() == 0)
                focusView.requestFocus();
            else
                submitPost();
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    private void submitPost() {
        mProgressDialog.show();
        String titleString = mTitleEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String locationString = mLocationEditText.getText().toString().trim();
        String tagsString = mTagEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();

        // Make a new post
        final ParseObject post = new ParseObject(ParseKeys.BUY_SELL_CLASS);
        post.put(ParseKeys.BUY_SELL_TITLE, titleString);
        post.put(ParseKeys.BUY_SELL_PRICE, priceString);
        post.put(ParseKeys.BUY_SELL_LOCATION, locationString);
        post.put(ParseKeys.BUY_SELL_CONTENT, descriptionString);
        String[] tagsArray = tagsString.split(",");
        for (String tag : tagsArray) {
            tag = tag.toLowerCase().trim();
            post.addUnique(ParseKeys.BUY_SELL_TAGS, tag);
        }
        post.put(ParseKeys.USER, mCurrentUser);
        post.put(ParseKeys.BUY_SELL_FIRST_NAME, firstName);

        for (int i = 0; i < 6; i++) {
            if (file[i] == null) {
                uploadData(post);
                break;
            } else {
                post.add(ParseKeys.BUY_SELL_FILE, file[i]);

            }
        }
    }

    private void uploadData(final ParseObject post) {
        post.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    objectId = post.getObjectId();
                    ParseInstallation pi = ParseInstallation.getCurrentInstallation();
                    pi.saveEventually();
//                    String piObjectId = pi.getObjectId();
//                    sendJobNotificationWithQuery(firstName, piObjectId);
                    mMenuScreenActivity.new RefreshNewsList().execute();
                    finish();
                    Toast.makeText(mContext, getString(R.string.job_posted), Toast.LENGTH_SHORT).show();
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_posting_data));
                }
            }
        });
    }

    private void sendJobNotificationWithQuery(String name, String piObjectId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channel", ParseKeys.BUY_SELL_CHANNEL);
        map.put("firstName", name);
        map.put("piObjectId", piObjectId);
        map.put("objectId", objectId);
        ParseCloud.callFunctionInBackground("jobPostPushNotification", map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_buy_sell_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContextThemeWrapper);
            builder.setItems(R.array.camera_choices, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case 0:
                            // take picture
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri[mImageIdIndex] = getOutPutMediaFileUri();
                            if (mMediaUri[mImageIdIndex] == null)
                                Toast.makeText(mContext, "Error Accessing Storage", Toast.LENGTH_SHORT).show();
                            else {
                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri[mImageIdIndex]);
                                startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                            }
                            break;
                        case 1:
                            //choose picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Uri getOutPutMediaFileUri() {
        if (isExternalStorageAvailable()) {
            // get the uri
            // 1. get ext storage dir
            File mediaStorgeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

            // 2. create out subdir
            if (!mediaStorgeDir.exists()) {
                if (!mediaStorgeDir.mkdirs()) {
                    Log.d(TAG, "Failed to create directory");
                    return null;
                }
            }

            // 3. Create a file name
            // 4. Create the file
            File mediaFile;
            Date currentDate = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss", Locale.US).format(currentDate);

            String path = mediaStorgeDir.getPath() + File.separator;
            mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
            // 5. Return the file's uri
            return Uri.fromFile(mediaFile);
        } else
            return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageView = new ImageView(mContext);
        mImageView.setId(mImageIdIndex);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setAdjustViewBounds(true);
        mImageView.setPadding(0, 10, 10, 10);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_REQUEST) {
                if (data == null) {
                    Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
                } else {
                    mMediaUri[mImageIdIndex] = data.getData();
                }
            } else {
                // add to gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri[mImageIdIndex]);
                sendBroadcast(mediaScanIntent);
            }

            Uri selectedImage = mMediaUri[mImageIdIndex];
            getContentResolver().notifyChange(selectedImage, null);
            InputStream inputStream = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                inputStream = null;
                inputStream = getContentResolver().openInputStream(selectedImage);
                mBitmap = BitmapFactory.decodeStream(inputStream, null, options);

                ByteArrayOutputStream picture_taken_stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, picture_taken_stream);
                mFileBytes = picture_taken_stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mFileName[mImageIdIndex] = FileHelper.getFileName(mContext, selectedImage, ParseKeys.TYPE_IMAGE);
            final ParseFile parseFile = new ParseFile(mFileName[mImageIdIndex], mFileBytes);

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Adding photo");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        file[mImageIdIndex] = parseFile;
                        mImageView.setImageBitmap(mBitmap);
                        mLinearLayout.addView(mImageView);
                        mHorizontalScrollView.setSmoothScrollingEnabled(true);
                        mHorizontalScrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHorizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                        }, 100L);
                        mImageIdIndex++;
                    } else {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(mContext, "Error adding photo", Toast.LENGTH_LONG).show();
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    dialog.setProgress(integer);
                    if (integer == 100)
                        dialog.dismiss();
                }
            });
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(mContext, "There was an error", Toast.LENGTH_SHORT).show();
        }
    }

}
