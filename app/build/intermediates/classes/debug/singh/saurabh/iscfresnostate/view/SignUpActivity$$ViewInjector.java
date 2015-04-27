// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SignUpActivity$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.SignUpActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427469, "field 'mFirstName'");
    target.mFirstName = finder.castView(view, 2131427469, "field 'mFirstName'");
    view = finder.findRequiredView(source, 2131427470, "field 'mLastName'");
    target.mLastName = finder.castView(view, 2131427470, "field 'mLastName'");
    view = finder.findRequiredView(source, 2131427471, "field 'mEmail'");
    target.mEmail = finder.castView(view, 2131427471, "field 'mEmail'");
    view = finder.findRequiredView(source, 2131427472, "field 'mPassword'");
    target.mPassword = finder.castView(view, 2131427472, "field 'mPassword'");
    view = finder.findRequiredView(source, 2131427473, "field 'mConfirmPassword'");
    target.mConfirmPassword = finder.castView(view, 2131427473, "field 'mConfirmPassword'");
    view = finder.findRequiredView(source, 2131427475, "field 'mSignUpButton'");
    target.mSignUpButton = finder.castView(view, 2131427475, "field 'mSignUpButton'");
  }

  @Override public void reset(T target) {
    target.mFirstName = null;
    target.mLastName = null;
    target.mEmail = null;
    target.mPassword = null;
    target.mConfirmPassword = null;
    target.mSignUpButton = null;
  }
}
