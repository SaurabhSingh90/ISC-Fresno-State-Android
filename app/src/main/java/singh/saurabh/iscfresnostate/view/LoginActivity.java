package singh.saurabh.iscfresnostate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.controller.CustomNetworkErrorHandler;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = this;
    public ContextThemeWrapper mContextThemeWrapper;
    private View reset_password_dialog_layout;
    public ParseUser mCurrentUser = ParseUser.getCurrentUser();
    private CustomNetworkErrorHandler mCustomNetworkErrorHandler;
    private ProgressDialog dialog;

    @InjectView(R.id.login_email)
    AutoCompleteTextView mEmailView;
    @InjectView(R.id.login_password)
    EditText mPasswordView;
    @InjectView(R.id.sign_in_button)
    Button mEmailSignInButton;
    @InjectView(R.id.sign_up_textView)
    TextView mSignUpTextView;
    @InjectView(R.id.forgot_password_textView)
    TextView mForgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mCustomNetworkErrorHandler = new CustomNetworkErrorHandler(this);
        mContextThemeWrapper = mCustomNetworkErrorHandler.mContextThemeWrapper;

        dialog = new ProgressDialog(mContextThemeWrapper);
        dialog.setMessage(getString(R.string.signing_in_text));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.action_login || id == EditorInfo.IME_NULL) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mEmailSignInButton.getWindowToken(), 0);
                attemptLogin();
            }
        });

        mSignUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SignUpActivity.class);
                startActivity(i);
            }
        });

        mForgotPasswordTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseUser mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser != null && mCurrentUser.getBoolean("emailVerified")) {
            Intent i = new Intent(mContext, MenuScreenActivity.class);
            startActivity(i);
        } else {
            mEmailView.setText("");
            mPasswordView.setText("");
        }
    }

    private void showResetPasswordDialog() {
        reset_password_dialog_layout = View.inflate(mContext, R.layout.activity_forgot_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContextThemeWrapper);
        builder.setPositiveButton(getString(R.string.send_verification_link).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setView(reset_password_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText email_for_verification_link = ((EditText) reset_password_dialog_layout.findViewById(R.id.forgot_password_editText));
                email_for_verification_link.setError(null);
                final String email = email_for_verification_link.getText().toString().trim();

                final ProgressDialog sending_mail_dialog = new ProgressDialog(mContext);
                sending_mail_dialog.setMessage(getString(R.string.sending_mail_text));
                sending_mail_dialog.setIndeterminate(true);
                sending_mail_dialog.setCanceledOnTouchOutside(false);
                sending_mail_dialog.show();

                ParseQuery<ParseUser> queryToGetEmailAddress = ParseUser.getQuery();
                queryToGetEmailAddress.whereEqualTo("username", email);
                queryToGetEmailAddress.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        sending_mail_dialog.dismiss();
                        if (e == null) {
                            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        dialog.dismiss();
                                        Toast.makeText(mContext, R.string.password_reset_link_sent, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else
                            email_for_verification_link.setError(getString(R.string.error_email_does_not_exist));
                    }
                });
            }
        });
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mCustomNetworkErrorHandler.isNetworkAvailable()) {
            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);

            // Store values at the time of the login attempt.
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            email = email.trim();
            password = password.trim();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid email address.
            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email_login));
                focusView = mEmailView;
                cancel = true;
            }
            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mEmailView.setError(getString(R.string.error_field_required));
                if (focusView == null)
                    focusView = mEmailView;
                cancel = true;
            } else if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_password_too_short_signIn));
                if (focusView == null)
                    focusView = mPasswordView;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                login_process(email, password);
            }
        } else
            mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.error_oops), getString(R.string.check_network));
    }

    private void login_process(String email, String password) {
        dialog.show();
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                dialog.dismiss();
                if (parseUser != null) {
                    if (parseUser.getBoolean("emailVerified")) {
                        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
                        pi.put("user", ParseUser.getCurrentUser());
                        pi.put("firstName", ParseUser.getCurrentUser().getString("firstName"));
                        pi.saveEventually();
                        Intent i = new Intent(mContext, MenuScreenActivity.class);
                        startActivity(i);
                    } else {
                        mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.email_not_verified_title), getString(R.string.email_not_verified_message));
                    }
                } else {
                    mCustomNetworkErrorHandler.errorDialogDisplay(getString(R.string.invalid_credentials_title), getString(R.string.invalid_credentials_message));
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

