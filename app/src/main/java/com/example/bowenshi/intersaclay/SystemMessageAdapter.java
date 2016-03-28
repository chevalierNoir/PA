/**
 * Created by bowenshi on 21/03/16.
 */
package com.example.bowenshi.intersaclay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.Date;
import java.util.List;

public class SystemMessageAdapter extends BaseAdapter {

    List<String> messageList;
    List<String> messageDetailList;
    List<String> datetimeList;
    Activity context;
    private static LayoutInflater inflater=null;

    String username;

    public SystemMessageAdapter(Activity context, List<String> messages, List<String> messageDetails, List<String> datetime, String name) {
        this.context = context;
        this.messageList = messages;
        this.messageDetailList=messageDetails;
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

    public void add(String message, String details, String datetime){
        messageList.add(message);
        messageDetailList.add(details);
        datetimeList.add(datetime);
    }

    public class Holder{
        TextView tv_message;
        TextView tv_datetime;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder=new Holder();
        View view=inflater.inflate(R.layout.list_item_system_message,null);
        holder.tv_message=(TextView) view.findViewById(R.id.system_message_content);
        holder.tv_datetime=(TextView) view.findViewById(R.id.system_message_datetime);

        holder.tv_message.setText(messageList.get(position));
        holder.tv_datetime.setText(datetimeList.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,SystemMessageDetailActivity.class);
                intent.putExtra("Username",username);
                intent.putExtra("Title",messageList.get(position));
                intent.putExtra("Datetime", datetimeList.get(position));
                intent.putExtra("Detail", messageDetailList.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }






}
