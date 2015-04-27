package singh.saurabh.iscfresnostate.controller.CustomAdapters;

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
public class DiscussionPostAdapter extends ArrayAdapter<HashMap<String, String>> {

    private static Context mContext;
    private static ArrayList<HashMap<String, String>> mList;
    private static Boolean checkBoxVisibilityFlag = false;
    public static Boolean[] mArrayForCheckMarks;

    public DiscussionPostAdapter(Context context, ArrayList<HashMap<String, String>> postList, Boolean flag) {
        super(context, R.layout.single_postlist_item, postList);
        mContext = context;
        mList = postList;
        checkBoxVisibilityFlag = flag;
        mArrayForCheckMarks = new Boolean[mList.size()];
        for (int i = 0; i < mArrayForCheckMarks.length; i++)
            mArrayForCheckMarks[i] = false;
    }


    private static class ViewHolder {
        protected View postTagContainer;
        protected TextView firstName, title, published_date, postTags;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            // brand new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_postlist_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_single_list_item);
            holder.firstName = (TextView) convertView.findViewById(R.id.author_name_single_list_item);
            holder.published_date = (TextView) convertView.findViewById(R.id.date_single_list_item);
            holder.postTags = (TextView) convertView.findViewById(R.id.tags_single_list_item);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_single_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // item clicked for posts
        if (checkBoxVisibilityFlag) {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(mArrayForCheckMarks[position]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mArrayForCheckMarks[position])
                        mArrayForCheckMarks[position] = true;
                    else
                        mArrayForCheckMarks[position] = false;
                    holder.checkbox.setChecked(mArrayForCheckMarks[position]);
                }
            });
        }
        else
            holder.checkbox.setVisibility(View.INVISIBLE);

        HashMap<String, String> listItem = mList.get(position);

        holder.firstName.setText(listItem.get("author"));
        holder.title.setText(listItem.get("title"));
        holder.published_date.setText(listItem.get("createdAt"));
        holder.postTags.setText(listItem.get("tags"));

        return convertView;
    }

    public static void markAll() {
        for (int i = 0; i < mArrayForCheckMarks.length; i++)
            mArrayForCheckMarks[i] = true;
    }

    public static void unMarkAll() {
        for (int i = 0; i < mArrayForCheckMarks.length; i++)
            mArrayForCheckMarks[i] = false;
    }
}
