// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class FormView$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.FormView> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427449, "field 'mFormWebView'");
    target.mFormWebView = finder.castView(view, 2131427449, "field 'mFormWebView'");
  }

  @Override public void reset(T target) {
    target.mFormWebView = null;
  }
}
