package com.daniel.czaterv2;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 15.12.2016.
 */

public class CzatListAdapter extends BaseAdapter {

    private final Context context;

    private final List<CzatListResponseDetails> chats;

    public CzatListAdapter(Context context, List<CzatListResponseDetails> chats) {
        this.context = context;
        this.chats = chats;
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CzatListResponseDetails chat = chats.get(position);
        double distance = distance(App.getInstance().getMyPosition().latitude,
                App.getInstance().getMyPosition().longitude,
                chat.getLatitude(),
                chat.getLongitude());
        String distanceView = String.format("%.2f", distance);

        holder.nameTv.setText(chat.getName());
        holder.numberUserTv.setText("Użytkowników " + String.valueOf(chat.getMaxUsersNumber()));
        holder.distance.setText(distanceView + " metrów");
        return convertView;
    }

    private static class ViewHolder {

        private TextView nameTv;
        private TextView numberUserTv;
        private TextView distance;

        private ViewHolder(View view) {
            nameTv = (TextView) view.findViewById(R.id.chat_name);
            numberUserTv = (TextView) view.findViewById(R.id.chat_user_number);
            distance = (TextView) view.findViewById(R.id.tv_distance);
        }
    }

    private double distance(double latitudeStart, double longitudeStart, double latitudeStop, double longitudeStop) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latitudeStart);
        locationA.setLongitude(longitudeStart);

        Location locationB = new Location("point B");

        locationB.setLatitude(latitudeStop);
        locationB.setLongitude(longitudeStop);

        double distance = locationA.distanceTo(locationB);
        return distance;

    }
}
