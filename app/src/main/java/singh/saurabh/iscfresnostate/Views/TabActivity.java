package singh.saurabh.iscfresnostate.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import singh.saurabh.iscfresnostate.R;

public class TabActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_board:
//                    mTextMessage.setText(R.string.title_board);
                    return true;
                case R.id.navigation_feed:
//                    mTextMessage.setText(R.string.title_feed);
                    return true;
                case R.id.navigation_store:
//                    mTextMessage.setText(R.string.title_store);
                    return true;
                case R.id.navigation_news:
//                    mTextMessage.setText(R.string.title_line);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_store);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
