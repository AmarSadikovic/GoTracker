package se.mah.af6260.gotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by oskar on 2017-03-14.
 */

public class SessionAdapter extends ArrayAdapter<Session> {
    private LayoutInflater inflater;


    public SessionAdapter(Context context, ArrayList<Session> sessionList) {
        super(context, R.layout.cursor_adapter_sessions , sessionList);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = (LinearLayout)inflater.inflate( R.layout.cursor_adapter_sessions,parent,false);
            holder = new ViewHolder();
            holder.ivActivity = (ImageView)convertView.findViewById(R.id.ivActivity);
            holder.tvStarTime = (TextView)convertView.findViewById(R.id.tvStartTime);
            holder.tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if(getItem(position).getActivityType().equals("running")){
            holder.ivActivity.setImageResource(R.drawable.running);
        } else  if(getItem(position).getActivityType().equals("cycling")){
            holder.ivActivity.setImageResource(R.drawable.bicycling);
        } else  if(getItem(position).getActivityType().equals("walking")){
            holder.ivActivity.setImageResource(R.drawable.walking);
        }
        holder.tvStarTime.setText(getItem(position).getStartTime());
        holder.tvDuration.setText(getItem(position).getDuration());

        return convertView;
    }

    private class ViewHolder{
        ImageView ivActivity;
        TextView tvStarTime;
        TextView tvDuration;
    }
}