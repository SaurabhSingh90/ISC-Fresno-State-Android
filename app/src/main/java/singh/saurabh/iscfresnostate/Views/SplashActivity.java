package singh.saurabh.iscfresnostate.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import singh.saurabh.iscfresnostate.Helpers.Util;
import singh.saurabh.iscfresnostate.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Util.isUserLoggedIn(this)) {
            Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show();
            // Go to main page
            startActivity(new Intent(this, FeedActivity.class));
        } else {
            // Go to Login page
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
