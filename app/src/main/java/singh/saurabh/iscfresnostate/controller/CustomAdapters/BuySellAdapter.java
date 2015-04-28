package singh.saurabh.iscfresnostate.controller.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import singh.saurabh.iscfresnostate.R;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class BuySellAdapter extends ArrayAdapter<HashMap<String, String>> {

    private static Context mContext;
    private static ArrayList<HashMap<String, String>> mList;

    public BuySellAdapter (Context context, ArrayList<HashMap<String, String>> postList) {
        super(context, R.layout.single_buylist_item, postList);
        mContext = context;
        mList = postList;
    }


    private static class ViewHolder {
        protected ImageView mImageView;
        protected TextView firstName, location, title, price, published_date, postTags;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            // brand new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_buylist_item, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.buy_sell_list_item_imageView);
            holder.title = (TextView) convertView.findViewById(R.id.buy_sell_title);
            holder.price = (TextView) convertView.findViewById(R.id.buy_sell_price);
            holder.location = (TextView) convertView.findViewById(R.id.buy_sell_location);
            holder.firstName = (TextView) convertView.findViewById(R.id.buy_sell_posted_by);
            holder.published_date = (TextView) convertView.findViewById(R.id.buy_sell_posted_date);
            holder.postTags = (TextView) convertView.findViewById(R.id.buy_sell_tags);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> listItem = mList.get(position);
//        holder.mImageView.setImageResource();
        holder.title.setText(listItem.get("title"));
        holder.price.setText(listItem.get("price"));
        holder.location.setText(listItem.get("location"));
        holder.firstName.setText(listItem.get("author"));
        holder.published_date.setText(listItem.get("createdAt"));
        holder.postTags.setText(listItem.get("tags"));

        return convertView;
    }
}
