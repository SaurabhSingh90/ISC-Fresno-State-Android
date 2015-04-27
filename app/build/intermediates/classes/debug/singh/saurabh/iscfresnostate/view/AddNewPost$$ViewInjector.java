// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AddNewPost$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.AddNewPost> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427420, "field 'mTitleEditext'");
    target.mTitleEditext = finder.castView(view, 2131427420, "field 'mTitleEditext'");
    view = finder.findRequiredView(source, 2131427421, "field 'mPostContentEditText'");
    target.mPostContentEditText = finder.castView(view, 2131427421, "field 'mPostContentEditText'");
    view = finder.findRequiredView(source, 2131427422, "field 'mPostTagsEditText'");
    target.mPostTagsEditText = finder.castView(view, 2131427422, "field 'mPostTagsEditText'");
    view = finder.findRequiredView(source, 2131427423, "field 'mSubmitPostButton'");
    target.mSubmitPostButton = finder.castView(view, 2131427423, "field 'mSubmitPostButton'");
  }

  @Override public void reset(T target) {
    target.mTitleEditext = null;
    target.mPostContentEditText = null;
    target.mPostTagsEditText = null;
    target.mSubmitPostButton = null;
  }
}
