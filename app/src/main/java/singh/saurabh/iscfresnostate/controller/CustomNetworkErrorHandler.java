package singh.saurabh.iscfresnostate.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ContextThemeWrapper;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class CustomNetworkErrorHandler extends AlertDialog {

    private Context mContext;
    public ContextThemeWrapper mContextThemeWrapper;

    public CustomNetworkErrorHandler(Context context) {
        super(context);
        mContext = context;
        mContextThemeWrapper = new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Dialog);
    }

    public void errorDialogDisplay(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContextThemeWrapper);
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkinfo != null && networkinfo.isConnected())
            isAvailable = true;

        return isAvailable;
    }
}
