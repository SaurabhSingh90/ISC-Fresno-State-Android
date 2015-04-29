package singh.saurabh.iscfresnostate.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class ItemImageView extends ActionBarActivity {

    private String url;
    private ProgressDialog mProgressDialog;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private static ContextThemeWrapper mContextThemeWrapper;

    @InjectView(R.id.item_image_webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_image_view);
        ButterKnife.inject(this);

//        getActionBar().hide();

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        mProgressDialog = new ProgressDialog(mContextThemeWrapper);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.get("imageUrl").toString();
        }
        loadGalleryUrl();
    }

    public void loadGalleryUrl() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    setProgress(progress * 100);
                    if(progress == 100) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.error_loading_data));
                }
            });
            mWebView.loadUrl(url);
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_item_image_view, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
