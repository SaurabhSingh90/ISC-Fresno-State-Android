// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class GalleryView$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.GalleryView> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427461, "field 'mGalleryWebView'");
    target.mGalleryWebView = finder.castView(view, 2131427461, "field 'mGalleryWebView'");
  }

  @Override public void reset(T target) {
    target.mGalleryWebView = null;
  }
}
