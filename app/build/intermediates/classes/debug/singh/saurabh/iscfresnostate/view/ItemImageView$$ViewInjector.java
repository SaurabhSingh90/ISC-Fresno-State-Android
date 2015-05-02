// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ItemImageView$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.ItemImageView> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427462, "field 'mWebView'");
    target.mWebView = finder.castView(view, 2131427462, "field 'mWebView'");
  }

  @Override public void reset(T target) {
    target.mWebView = null;
  }
}
