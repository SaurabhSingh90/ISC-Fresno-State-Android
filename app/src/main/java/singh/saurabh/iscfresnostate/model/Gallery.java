package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.view.GalleryView;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class Gallery {

    private Activity mActivity;
    private View mView;

    public Gallery (Activity activity, View view) {
        this.mActivity = activity;
        this.mView = view;
    }

    public void startGalleryFragment() {
        final Intent intent = new Intent(mActivity, GalleryView.class);

        TextView textView1 = (TextView) mView.findViewById(R.id.gallery_textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_ICN_2014);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_1));
                mActivity.startActivity(intent);
            }
        });
        TextView textView2 = (TextView) mView.findViewById(R.id.gallery_textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_ISC_KICKSTART_2014);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_2));
                mActivity.startActivity(intent);
            }
        });
        TextView textView3 = (TextView) mView.findViewById(R.id.gallery_textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_DIWALI_DHAMAKA);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_3));
                mActivity.startActivity(intent);
            }
        });
        TextView textView4 = (TextView) mView.findViewById(R.id.gallery_textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_EDUCATION_WEEK_ISC_PHOTOGRAPHY);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_4));
                mActivity.startActivity(intent);
            }
        });
        TextView textView5 = (TextView) mView.findViewById(R.id.gallery_textView5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_CARRER_DEVELOPMENT_WORKSHOP);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_5));
                mActivity.startActivity(intent);
            }
        });
        TextView textView6 = (TextView) mView.findViewById(R.id.gallery_textView6);
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_ISC_ELECTION_FALL_2014);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_6));
                mActivity.startActivity(intent);
            }
        });
        TextView textView7 = (TextView) mView.findViewById(R.id.gallery_textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_GANDHI_JAYANTI_2014);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.gallery_7));
                mActivity.startActivity(intent);
            }
        });
    }
}
