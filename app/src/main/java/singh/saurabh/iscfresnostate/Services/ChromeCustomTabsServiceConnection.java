package singh.saurabh.iscfresnostate.Services;

import android.content.ComponentName;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

/**
 * Created by saurabhsingh on 3/15/17.
 */

public class ChromeCustomTabsServiceConnection {

    // Package name for the Chrome channel the client wants to connect to. This
    // depends on the channel name.
    // Stable = com.android.chrome
    // Beta = com.chrome.beta
    // Dev = com.chrome.dev
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    public static CustomTabsClient customTabsClient;
    public static CustomTabsSession customTabsSession;
    public static CustomTabsServiceConnection customTabsServiceConnection;
    public static CustomTabsIntent customTabsIntent;

    public static void Init() {
        // for warming up custom chrome tab
        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                customTabsClient = client;
                customTabsClient.warmup(0);
                customTabsSession = customTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                customTabsClient = null;
            }
        };

        customTabsIntent = new CustomTabsIntent
                .Builder(customTabsSession)
                .setShowTitle(true)
                //.setToolbarColor(colorInt)
                .setInstantAppsEnabled(true)
                .build();
    }
}
