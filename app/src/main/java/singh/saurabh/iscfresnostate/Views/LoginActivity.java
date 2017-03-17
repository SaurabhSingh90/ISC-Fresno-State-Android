package singh.saurabh.iscfresnostate.Views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import singh.saurabh.iscfresnostate.R;

public class LoginActivity extends AppCompatActivity {

    private String TAG;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    RelativeLayout progressRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TAG = this.getLocalClassName();

        // Initialize Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
//                    Toast.makeText(LoginActivity.this, "Welcome: " + user.getDisplayName()
//                            , Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Initialize Facebook callback manager
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setAlpha(0);
                progressRelativeLayout.setAlpha(0.7f);
                handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        });

        progressRelativeLayout = (RelativeLayout)findViewById(R.id.login_progress_layout);
        progressRelativeLayout.setAlpha(0);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken: " + token);
        Log.d("FBAccessToken", "access token: " + AccessToken.getCurrentAccessToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete: " + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential: ex: " + task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed."
                                    , Toast.LENGTH_SHORT).show();
                        } else {

                            finish();
                            startActivity(new Intent(LoginActivity.this, TabActivity.class));
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
