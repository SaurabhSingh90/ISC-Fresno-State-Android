package singh.saurabh.iscfresnostate.Helpers;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saurabhsingh on 3/7/17.
 */

public class Util {

    public static boolean isUserLoggedIn(Activity activity) {
//        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
//        return preferences.contains(getKey(activity, R.string.login_key)) || AccessToken.getCurrentAccessToken() != null;

        // We get the current instance of logged in user
        // if it's null that means user is not logged in
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

//    public static void setLoginPrefrence(Activity activity) {
//        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(getKey(activity, R.string.login_key), true);
//        editor.apply();
//    }

    public static String getDate(Context context, String createdTime) {
//        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
//        try {
//            Date date = dateFormat.parse(createdTime);
//            return date.toString();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        SimpleDateFormat formatter = new SimpleDateFormat("MM dd HH:mm", Locale.getDefault());
        try {
            return formatter.format(formatter.parse(createdTime));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return createdTime;

    }
}
