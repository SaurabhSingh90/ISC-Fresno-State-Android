package singh.saurabh.iscfresnostate.controller.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import singh.saurabh.iscfresnostate.R;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class JobPostAdapter extends ArrayAdapter<HashMap<String, String>> {

    private static Context mContext;
    private static ArrayList<HashMap<String, String>> mList;

    public JobPostAdapter (Context context, ArrayList<HashMap<String, String>> postList) {
        super(context, R.layout.single_postlist_item, postList);
        mContext = context;
        mList = postList;
    }


    private static class ViewHolder {
        protected TextView title, location, firstName, published_date, postTags;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            // brand new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_joblist_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_single_joblist_item);
            holder.location = (TextView) convertView.findViewById(R.id.location_single_joblist_item);
            holder.firstName = (TextView) convertView.findViewById(R.id.author_name_single_joblist_item);
            holder.published_date = (TextView) convertView.findViewById(R.id.date_single_joblist_item);
            holder.postTags = (TextView) convertView.findViewById(R.id.tags_single_joblist_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> listItem = mList.get(position);
        holder.firstName.setText(listItem.get("author"));
        holder.location.setText(listItem.get("location"));
        holder.title.setText(listItem.get("title"));
        holder.published_date.setText(listItem.get("createdAt"));
        holder.postTags.setText(listItem.get("tags"));

        return convertView;
    }
}
