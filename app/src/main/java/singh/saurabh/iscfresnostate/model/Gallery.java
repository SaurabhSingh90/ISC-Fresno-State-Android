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
    private String FORM_LINK = "form_link";
    private String FORM_TITLE = "form_title";
    private String URL_1 = "http://www.iscfresnostate.com/icn-2014/";
    private String URL_2 = "http://www.iscfresnostate.com/isc-kickstart-2014/";
    private String URL_3 = "http://www.iscfresnostate.com/diwali-dhamaka-2014/";
    private String URL_4 = "http://www.iscfresnostate.com/education-week-isc-photobooth/";
    private String URL_5 = "http://www.iscfresnostate.com/career-development-workshop/";
    private String URL_6 = "http://www.iscfresnostate.com/isc-elections-fall-2014/";
    private String URL_7 = "http://www.iscfresnostate.com/gandhi-jayanthi-2014/";

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
                intent.putExtra(FORM_LINK, URL_1);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_1));
                mActivity.startActivity(intent);
            }
        });
        TextView textView2 = (TextView) mView.findViewById(R.id.gallery_textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_2);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_2));
                mActivity.startActivity(intent);
            }
        });
        TextView textView3 = (TextView) mView.findViewById(R.id.gallery_textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_3);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_3));
                mActivity.startActivity(intent);
            }
        });
        TextView textView4 = (TextView) mView.findViewById(R.id.gallery_textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_4);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_4));
                mActivity.startActivity(intent);
            }
        });
        TextView textView5 = (TextView) mView.findViewById(R.id.gallery_textView5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_5);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_5));
                mActivity.startActivity(intent);
            }
        });
        TextView textView6 = (TextView) mView.findViewById(R.id.gallery_textView6);
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_6);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_6));
                mActivity.startActivity(intent);
            }
        });
        TextView textView7 = (TextView) mView.findViewById(R.id.gallery_textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_7);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.gallery_7));
                mActivity.startActivity(intent);
            }
        });
    }
}
