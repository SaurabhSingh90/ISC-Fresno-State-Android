// Generated code from Butter Knife. Do not modify!
package singh.saurabh.iscfresnostate.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class BuySellPostDescription$$ViewInjector<T extends singh.saurabh.iscfresnostate.view.BuySellPostDescription> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427440, "field 'mTitleTextView'");
    target.mTitleTextView = finder.castView(view, 2131427440, "field 'mTitleTextView'");
    view = finder.findRequiredView(source, 2131427441, "field 'mPriceTextView'");
    target.mPriceTextView = finder.castView(view, 2131427441, "field 'mPriceTextView'");
    view = finder.findRequiredView(source, 2131427442, "field 'mLocationTextView'");
    target.mLocationTextView = finder.castView(view, 2131427442, "field 'mLocationTextView'");
    view = finder.findRequiredView(source, 2131427443, "field 'mSellByTextView'");
    target.mSellByTextView = finder.castView(view, 2131427443, "field 'mSellByTextView'");
    view = finder.findRequiredView(source, 2131427444, "field 'mSellDateTextView'");
    target.mSellDateTextView = finder.castView(view, 2131427444, "field 'mSellDateTextView'");
    view = finder.findRequiredView(source, 2131427445, "field 'mTagsTextView'");
    target.mTagsTextView = finder.castView(view, 2131427445, "field 'mTagsTextView'");
    view = finder.findRequiredView(source, 2131427446, "field 'mDescriptionTextView'");
    target.mDescriptionTextView = finder.castView(view, 2131427446, "field 'mDescriptionTextView'");
    view = finder.findRequiredView(source, 2131427439, "field 'mLinearLayout'");
    target.mLinearLayout = finder.castView(view, 2131427439, "field 'mLinearLayout'");
  }

  @Override public void reset(T target) {
    target.mTitleTextView = null;
    target.mPriceTextView = null;
    target.mLocationTextView = null;
    target.mSellByTextView = null;
    target.mSellDateTextView = null;
    target.mTagsTextView = null;
    target.mDescriptionTextView = null;
    target.mLinearLayout = null;
  }
}
