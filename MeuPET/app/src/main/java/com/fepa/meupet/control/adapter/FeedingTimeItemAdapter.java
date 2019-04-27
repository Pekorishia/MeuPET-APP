package com.fepa.meupet.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.feeder.FeedingTime;

public class FeedingTimeItemAdapter extends BaseAdapter {

    private ArrayAdapter<FeedingTime> feedingList;
    private Context context;

    private static class ViewHolder {
        public TextView amount;
        public TextView time;
    }

    public FeedingTimeItemAdapter(Context context){
        this.context = context;
        this.feedingList = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);;
    }

    public void addItem(FeedingTime feedingTime) {
        this.feedingList.add(feedingTime);
    }

    public void editItem(int position, FeedingTime feedingTime) {
        this.removeItem(position);
        this.feedingList.insert(feedingTime, position);
    }

    public void removeItem(int position) {
        this.feedingList.remove(this.feedingList.getItem(position));
    }

    @Override
    public int getCount() {
        return (this.feedingList != null ? this.feedingList.getCount() : 0);
    }

    @Override
    public Object getItem(int i) {
        return this.feedingList.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return this.feedingList.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(context, R.layout.feeding_time_list_item, null);
            // configures view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.amount = view.findViewById(R.id.tvFeedingAmount);
            viewHolder.time = view.findViewById(R.id.tvFeedingTime);
            view.setTag(viewHolder);
        }

        FeedingTime feedingTime = feedingList.getItem(i);

        // fills data
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.amount.setText(Integer.toString(feedingTime.getDrop_amount()));
        holder.time.setText(feedingTime.getTime());

        return view;
    }
}
