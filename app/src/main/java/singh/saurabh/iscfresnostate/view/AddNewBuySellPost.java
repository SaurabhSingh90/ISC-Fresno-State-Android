package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import butterknife.ButterKnife;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class AddNewBuySellPost extends ActionBarActivity {

    private static String TAG = AddNewBuySellPost.class.getSimpleName();
    private Activity mContext = this;
    private String objectId;
    private static ParseUser mCurrentUser = ParseUser.getCurrentUser();
    private static String firstName = mCurrentUser.getString("firstName");
    private View focusView = null;
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private ContextThemeWrapper mContextThemeWrapper;
    private ProgressDialog dialog;
    private MenuScreenActivity mMenuScreenActivity = new MenuScreenActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_buy_sell_post);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        dialog = new ProgressDialog(mContextThemeWrapper);
        dialog.setMessage(getString(R.string.adding_post_text));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
