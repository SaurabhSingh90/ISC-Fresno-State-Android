package singh.saurabh.iscfresnostate.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;

public class SignUpActivity extends Activity {

    private Context mContext = this;

    @InjectView(R.id.firstName_editText)
    EditText mFirstName;
    @InjectView(R.id.lastName_editText)
    EditText mLastName;
    @InjectView(R.id.sign_up_email_editText)
    EditText mEmail;
    @InjectView(R.id.sign_up_password_editText)
    EditText mPassword;
    @InjectView(R.id.confirm_sign_up_password_editText)
    EditText mConfirmPassword;
    @InjectView(R.id.sign_up_button)
    Button mSignUpButton;
    @InjectView(R.id.signup_progress)
    View mProgressView;
    @InjectView(R.id.sign_up_form)
    View mSignUpFormView;

    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);

        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_signUp || id == EditorInfo.IME_NULL) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mConfirmPassword.getWindowToken(), 0);
                    signUpUserTask();
                    return true;
                }
                return false;
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mSignUpButton.getWindowToken(), 0);
                signUpUserTask();
            }
        });
    }

    private void signUpUserTask() {

        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            // Reset errors.
            mEmail.setError(null);
            mPassword.setError(null);
            mFirstName.setError(null);
            mLastName.setError(null);
            mConfirmPassword.setError(null);

            // Store values at the time of the login attempt.
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String confirmPassword = mConfirmPassword.getText().toString().trim();
            String firstName = mFirstName.getText().toString().trim();
            String lastName = mLastName.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            // Check if first name was entered.
            if (TextUtils.isEmpty(firstName)) {
                mFirstName.setError(getString(R.string.error_field_required));
                focusView = mFirstName;
                cancel = true;
            }

            // Check if last name was entered.
            if (TextUtils.isEmpty(lastName)) {
                mLastName.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mLastName;
                cancel = true;
            }

            // Check if email is entered and if so then valid or not
            if (TextUtils.isEmpty(email)) {
                mEmail.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mEmail;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmail.setError(getString(R.string.error_invalid_email_signUp));
                if (focusView == null)
                    focusView = mEmail;
                cancel = true;
            }

            // Check if password and confirm password is entered and if so then valid or not
            if (TextUtils.isEmpty(password)) {
                mPassword.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mPassword;
                cancel = true;
            } else if (password.length() < 5) {
                mPassword.setError(getString(R.string.error_password_too_short_signUp));
                if (focusView == null)
                    focusView = mPassword;
                cancel = true;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                mConfirmPassword.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mConfirmPassword;
                cancel = true;
            } else if (!isPasswordValid(password, confirmPassword)) {
                mConfirmPassword.setError(getString(R.string.error_password_does_not_match));
                if (focusView == null)
                    focusView = mConfirmPassword;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                ParseUser newUser = new ParseUser();
                // Mandatory fields
                newUser.setUsername(email);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.isAuthenticated();
                //Extra Fields
                newUser.put("firstName", firstName);
                newUser.put("lastName", lastName);
                //Background task for sign up
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        showProgress(false);
                        if (e == null) {
                            Toast.makeText(mContext, R.string.account_activation_link_sent, Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), email + " is already registered");
                        }
                    }
                });

            }
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String confirmPassword) {
        return (password.compareTo(confirmPassword) == 0);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
