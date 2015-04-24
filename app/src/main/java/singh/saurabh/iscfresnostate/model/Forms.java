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
    private String FORM_LINK = "form_link";
    private String FORM_TITLE = "form_title";
    private String URL_1 = "https://docs.google.com/forms/d/12krpkKcbITUjffqj0TnoAl4cQDQ7PHEmNHNkDtClWZ4/viewform?embedded=true";
    private String URL_2 = "https://docs.google.com/forms/d/1A1vuQLm0Ek3DxCHJ0pJwzkBcvmaJnojpi-KPnNietlM/viewform?embedded=true";
    private String URL_3 = "https://docs.google.com/forms/d/1-vCCXamiOqqcnJFfyrBDjIv1SykijPsGBzA6YanWChg/viewform?embedded=true";

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
                intent.putExtra(FORM_LINK, URL_1);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.new_student_accommodation));
                mActivity.startActivity(intent);
            }
        });
        TextView form2TextView = (TextView) mView.findViewById(R.id.form2_textView);
        form2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_2);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.pick_up));
                mActivity.startActivity(intent);
            }
        });
        TextView form3TextView = (TextView) mView.findViewById(R.id.form3_textView);
        form3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(FORM_LINK, URL_3);
                intent.putExtra(FORM_TITLE, mActivity.getString(R.string.accommodation_volunteering));
                mActivity.startActivity(intent);
            }
        });
    }
}
