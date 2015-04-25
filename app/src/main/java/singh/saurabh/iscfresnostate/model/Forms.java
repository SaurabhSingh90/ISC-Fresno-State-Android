package singh.saurabh.iscfresnostate.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.view.FormView;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class Forms {

    private Activity mActivity;
    private View mView;

    public Forms (Activity activity, View view) {
        this.mActivity = activity;
        this.mView = view;
    }

    public void startFormsFragment() {

        final Intent intent = new Intent(mActivity, FormView.class);

        TextView form1TextView = (TextView) mView.findViewById(R.id.form1_textView);
        form1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_FORM_1);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.new_student_accommodation));
                mActivity.startActivity(intent);
            }
        });
        TextView form2TextView = (TextView) mView.findViewById(R.id.form2_textView);
        form2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_FORM_2);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.pick_up));
                mActivity.startActivity(intent);
            }
        });
        TextView form3TextView = (TextView) mView.findViewById(R.id.form3_textView);
        form3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParseKeys.PAGE_LINK, ParseKeys.URL_FORM_3);
                intent.putExtra(ParseKeys.PAGE_TITLE, mActivity.getString(R.string.accommodation_volunteering));
                mActivity.startActivity(intent);
            }
        });
    }
}
