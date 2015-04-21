package singh.saurabh.iscfresnostate.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import singh.saurabh.iscfresnostate.R;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class CustomAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> postList) {
        super(context, R.layout.single_list_item, postList);
        mContext = context;
        mList = postList;
    }


    private static class ViewHolder {
        protected TextView firstName, title, published_date, postTags;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // brand new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_single_postlist_item);
            holder.firstName = (TextView) convertView.findViewById(R.id.author_name_single_postlist_item);
            holder.published_date = (TextView) convertView.findViewById(R.id.date_single_postlist_item);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_single_postlist_item);
            holder.postTags = (TextView) convertView.findViewById(R.id.post_tags_textView_single_postlist_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> listItem = mList.get(position);

        holder.firstName.setText(listItem.get("author"));
        holder.title.setText(listItem.get("title"));
        holder.published_date.setText(listItem.get("createdAt"));
        holder.postTags.setText(listItem.get("tags"));

        return convertView;
    }
}
