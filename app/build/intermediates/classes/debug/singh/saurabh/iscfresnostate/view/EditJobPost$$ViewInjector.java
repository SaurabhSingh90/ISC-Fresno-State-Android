// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class EditJobPost$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.EditJobPost> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427438, "field 'mTitleEditText'");
    target.mTitleEditText = finder.castView(view, 2131427438, "field 'mTitleEditText'");
    view = finder.findRequiredView(source, 2131427439, "field 'mLocationEditText'");
    target.mLocationEditText = finder.castView(view, 2131427439, "field 'mLocationEditText'");
    view = finder.findRequiredView(source, 2131427440, "field 'mContentEditText'");
    target.mContentEditText = finder.castView(view, 2131427440, "field 'mContentEditText'");
    view = finder.findRequiredView(source, 2131427441, "field 'mTagEditText'");
    target.mTagEditText = finder.castView(view, 2131427441, "field 'mTagEditText'");
  }

  @Override public void reset(T target) {
    target.mTitleEditText = null;
    target.mLocationEditText = null;
    target.mContentEditText = null;
    target.mTagEditText = null;
  }
}
