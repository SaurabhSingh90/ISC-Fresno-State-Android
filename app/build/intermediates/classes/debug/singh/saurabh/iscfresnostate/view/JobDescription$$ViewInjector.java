// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class JobDescription$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.JobDescription> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361896, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131361896, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131361897, "field 'mLocation'");
    target.mLocation = finder.castView(view, 2131361897, "field 'mLocation'");
    view = finder.findRequiredView(source, 2131361898, "field 'mFirstName'");
    target.mFirstName = finder.castView(view, 2131361898, "field 'mFirstName'");
    view = finder.findRequiredView(source, 2131361899, "field 'mDate'");
    target.mDate = finder.castView(view, 2131361899, "field 'mDate'");
    view = finder.findRequiredView(source, 2131361900, "field 'mTag'");
    target.mTag = finder.castView(view, 2131361900, "field 'mTag'");
    view = finder.findRequiredView(source, 2131361903, "field 'mContent'");
    target.mContent = finder.castView(view, 2131361903, "field 'mContent'");
  }

  @Override public void reset(T target) {
    target.mTitle = null;
    target.mLocation = null;
    target.mFirstName = null;
    target.mDate = null;
    target.mTag = null;
    target.mContent = null;
  }
}
