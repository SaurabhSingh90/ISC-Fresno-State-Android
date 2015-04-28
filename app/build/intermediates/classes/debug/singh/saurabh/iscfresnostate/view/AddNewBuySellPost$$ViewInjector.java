// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AddNewBuySellPost$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.AddNewBuySellPost> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427414, "field 'mTitleEditText'");
    target.mTitleEditText = finder.castView(view, 2131427414, "field 'mTitleEditText'");
    view = finder.findRequiredView(source, 2131427415, "field 'mPriceEditText'");
    target.mPriceEditText = finder.castView(view, 2131427415, "field 'mPriceEditText'");
    view = finder.findRequiredView(source, 2131427416, "field 'mLocationEditText'");
    target.mLocationEditText = finder.castView(view, 2131427416, "field 'mLocationEditText'");
    view = finder.findRequiredView(source, 2131427417, "field 'mTagEditText'");
    target.mTagEditText = finder.castView(view, 2131427417, "field 'mTagEditText'");
    view = finder.findRequiredView(source, 2131427419, "field 'mDescriptionEditText'");
    target.mDescriptionEditText = finder.castView(view, 2131427419, "field 'mDescriptionEditText'");
    view = finder.findRequiredView(source, 2131427423, "field 'mSubmitPost'");
    target.mSubmitPost = finder.castView(view, 2131427423, "field 'mSubmitPost'");
    view = finder.findRequiredView(source, 2131427422, "field 'mLinearLayout'");
    target.mLinearLayout = finder.castView(view, 2131427422, "field 'mLinearLayout'");
    view = finder.findRequiredView(source, 2131427421, "field 'mHorizontalScrollView'");
    target.mHorizontalScrollView = finder.castView(view, 2131427421, "field 'mHorizontalScrollView'");
  }

  @Override public void reset(T target) {
    target.mTitleEditText = null;
    target.mPriceEditText = null;
    target.mLocationEditText = null;
    target.mTagEditText = null;
    target.mDescriptionEditText = null;
    target.mSubmitPost = null;
    target.mLinearLayout = null;
    target.mHorizontalScrollView = null;
  }
}
