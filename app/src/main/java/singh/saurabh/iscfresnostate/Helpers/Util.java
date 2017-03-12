package singh.saurabh.iscfresnostate.Helpers;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String formatTimeForEvent(long pacificTime) {
        Date date = new Date(pacificTime * 1000);
        DateFormat format = new SimpleDateFormat("d MMM h:mm a", Locale.getDefault());
        return format.format(date);
    }
}
