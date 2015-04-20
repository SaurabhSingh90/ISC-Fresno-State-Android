package singh.saurabh.iscfresnostate.controller;

import android.app.Application;

import com.parse.Parse;

import singh.saurabh.iscfresnostate.model.ParseKeys;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, ParseKeys.APPLICATION_KEY, ParseKeys.CLIENT_ID);
    }
}
