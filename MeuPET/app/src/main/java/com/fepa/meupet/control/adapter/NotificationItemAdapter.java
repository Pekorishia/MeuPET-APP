package com.fepa.meupet.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.notification.Notification;

public class NotificationItemAdapter  extends BaseAdapter {

    private ArrayAdapter<Notification> notificationList;
    private Context context;

    private static class ViewHolder {
        public TextView name;
        public TextView date;
        public TextView time;
    }

    public NotificationItemAdapter(Context context){
        this.context = context;
        this.notificationList = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);;
    }

    public void addItem(Notification notification) {
        this.notificationList.add(notification);
    }

    public void editItem(int position, Notification notification) {
        this.removeItem(position);
        this.notificationList.insert(notification, position);
    }

    public void removeItem(int position) {
        this.notificationList.remove(this.notificationList.getItem(position));
    }

    @Override
    public int getCount() {
        return (this.notificationList != null ? this.notificationList.getCount() : 0);
    }

    @Override
    public Object getItem(int i) {
        return this.notificationList.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return this.notificationList.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(context, R.layout.notification_list_item, null);
            // configures view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.tvNotificationName);
            viewHolder.date = view.findViewById(R.id.tvNotificationDate);
            viewHolder.time = view.findViewById(R.id.tvNotificationTime);
            view.setTag(viewHolder);
        }

        Notification notification = notificationList.getItem(i);

        // fills data
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(notification.getName());
        holder.date.setText(notification.getDate());
        holder.time.setText(notification.getTime());

        return view;
    }
}
