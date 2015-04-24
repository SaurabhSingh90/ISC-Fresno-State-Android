// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SinglePostDisplay$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.SinglePostDisplay> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361915, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131361915, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131361916, "field 'mFirstName'");
    target.mFirstName = finder.castView(view, 2131361916, "field 'mFirstName'");
    view = finder.findRequiredView(source, 2131361917, "field 'mDate'");
    target.mDate = finder.castView(view, 2131361917, "field 'mDate'");
    view = finder.findRequiredView(source, 2131361918, "field 'mTag'");
    target.mTag = finder.castView(view, 2131361918, "field 'mTag'");
    view = finder.findRequiredView(source, 2131361919, "field 'mListView'");
    target.mListView = finder.castView(view, 2131361919, "field 'mListView'");
  }

  @Override public void reset(T target) {
    target.mTitle = null;
    target.mFirstName = null;
    target.mDate = null;
    target.mTag = null;
    target.mListView = null;
  }
}
