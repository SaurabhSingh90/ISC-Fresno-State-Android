package singh.saurabh.iscfresnostate.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

import singh.saurabh.iscfresnostate.R;

/**
 * Created by saurabhsingh on 3/7/17.
 */

public class Util {

    public static String getKey(Activity activity, int resource_id) {
        return activity.getString(resource_id);
    }

    public static boolean isUserLoggedIn(Activity activity) {
//        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
//        return preferences.contains(getKey(activity, R.string.login_key)) || AccessToken.getCurrentAccessToken() != null;
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void setLoginPrefrence(Activity activity) {
//        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(getKey(activity, R.string.login_key), true);
//        editor.apply();
    }
}
