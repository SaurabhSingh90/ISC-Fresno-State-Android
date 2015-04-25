// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361911, "field 'mEmailView'");
    target.mEmailView = finder.castView(view, 2131361911, "field 'mEmailView'");
    view = finder.findRequiredView(source, 2131361912, "field 'mPasswordView'");
    target.mPasswordView = finder.castView(view, 2131361912, "field 'mPasswordView'");
    view = finder.findRequiredView(source, 2131361914, "field 'mEmailSignInButton'");
    target.mEmailSignInButton = finder.castView(view, 2131361914, "field 'mEmailSignInButton'");
    view = finder.findRequiredView(source, 2131361916, "field 'mSignUpTextView'");
    target.mSignUpTextView = finder.castView(view, 2131361916, "field 'mSignUpTextView'");
    view = finder.findRequiredView(source, 2131361915, "field 'mForgotPasswordTextView'");
    target.mForgotPasswordTextView = finder.castView(view, 2131361915, "field 'mForgotPasswordTextView'");
  }

  @Override public void reset(T target) {
    target.mEmailView = null;
    target.mPasswordView = null;
    target.mEmailSignInButton = null;
    target.mSignUpTextView = null;
    target.mForgotPasswordTextView = null;
  }
}
