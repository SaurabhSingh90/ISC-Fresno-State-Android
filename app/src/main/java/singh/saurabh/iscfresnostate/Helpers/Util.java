package singh.saurabh.iscfresnostate.Helpers;

import android.app.Activity;
import com.google.firebase.auth.FirebaseAuth;

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
}
