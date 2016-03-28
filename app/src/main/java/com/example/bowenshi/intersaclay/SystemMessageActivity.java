package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemMessageActivity extends AppCompatActivity {

    String username;
    ListView listView;
    SystemMessageAdapter adapter;
    ArrayList<String> titles;
    ArrayList<String> details;
    ArrayList<String> datetimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");

        titles=new ArrayList<String>();
        details=new ArrayList<String>();
        datetimes=new ArrayList<String>();

        Toolbar toolbar=(Toolbar) findViewById(R.id.system_message_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AccountActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            }
        });

        listView=(ListView) findViewById(R.id.system_message_listview);
        adapter=new SystemMessageAdapter(SystemMessageActivity.this, titles, details, datetimes, username);
        listView.setAdapter(adapter);
        RequestMessage();


    }

    public void RequestMessage(){

        String url ="http://systemmessage-1257.appspot.com/query";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("contents");
                    JSONArray jsonArray_datetime=response.getJSONArray("datetime");
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();++i){
                            String content=jsonArray.getString(i);
                            String datetime=jsonArray_datetime.getString(i);
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//                            String content=jsonObject.toString();

                            String[] parts=content.split("\r\n");
                            String title=parts[0];
                            String detail=content.substring(parts[0].length() + 1, content.length() - 1);
                            titles.add(title);
                            details.add(detail);
                            datetimes.add(datetime);
//                            adapter.add(title,detail,datetime);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

//                String str=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
