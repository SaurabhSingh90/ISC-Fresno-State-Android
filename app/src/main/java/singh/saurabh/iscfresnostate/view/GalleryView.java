package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class GalleryView extends ActionBarActivity {

    private Activity mActivity = this;
    private String mUrl;
    private String mTitle;
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;

    @InjectView(R.id.gallery_webView)
    WebView mGalleryWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        this.setProgressBarVisibility(true);
        ButterKnife.inject(this);

        mGalleryWebView.getSettings().setBuiltInZoomControls(true);
        mGalleryWebView.getSettings().setDisplayZoomControls(false);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUrl = extras.getString("form_link");
            mTitle = extras.getString("form_title");
        }
        restoreActionBar(mTitle);
        loadGalleryUrl();
    }

    public void loadGalleryUrl() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            mGalleryWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    mActivity.setProgress(progress * 100);
                    if(progress == 100) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }
            });
            mGalleryWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_loading_data));
                }
            });
            mGalleryWebView.loadUrl(mUrl);
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    public void restoreActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new RefreshBackGroundTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RefreshBackGroundTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContextThemeWrapper);
            mDialog.setMessage(getString(R.string.loading_text));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();
            loadGalleryUrl();
        }
    }
}
