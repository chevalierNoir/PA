/**
 * Created by bowenshi on 22/03/16.
 */
package com.example.bowenshi.intersaclay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserMessageAdapter extends BaseAdapter {

    List<String> senderList;
    List<String> messageList;
    List<String> datetimeList;
    Activity context;
    private static LayoutInflater inflater=null;

    String username;

    public UserMessageAdapter(Activity context, List<String> senders, List<String> messages, List<String> datetime, String name) {
        this.context = context;
        this.senderList=senders;
        this.messageList = messages;
        this.datetimeList=datetime;
        this.username=name;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public class Holder{
        ImageView iv;
        TextView tv_sender;
        TextView tv_message;
        TextView tv_datetime;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder=new Holder();
        View view=inflater.inflate(R.layout.list_item_user_message,null);
        holder.iv=(ImageView) view.findViewById(R.id.message_user_logo);
        holder.tv_sender=(TextView) view.findViewById(R.id.message_sender_name);
        holder.tv_message=(TextView) view.findViewById(R.id.message_user_short);
        holder.tv_datetime=(TextView) view.findViewById(R.id.message_user_time);

        holder.tv_sender.setText(senderList.get(position));
        holder.tv_message.setText(messageList.get(position));
        holder.tv_datetime.setText(datetimeList.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("Username",username);
                intent.putExtra("Sender",senderList.get(position));
//                intent.putExtra("Sender",senderList.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }
}
