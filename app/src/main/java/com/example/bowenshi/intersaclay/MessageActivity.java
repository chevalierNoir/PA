package com.example.bowenshi.intersaclay;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Cache;
import com.android.volley.Network;


import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    String username;
    UserMessageAdapter adapter;
    ArrayList<String> senders;
    ArrayList<String> messages;
    ArrayList<String> datetimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=(Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
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
        senders=new ArrayList<String>();
        messages=new ArrayList<String>();
        datetimes=new ArrayList<String>();
        ListView listView=(ListView) findViewById(R.id.user_message_listview);
        adapter=new UserMessageAdapter(MessageActivity.this,senders,messages,datetimes,username);
        listView.setAdapter(adapter);

        RequestMessage();


    }

    public void RequestMessage(){

        String url ="http://lunar-mercury-124221.appspot.com/shortquery";

//        HashMap<String, String> params=new HashMap<>();
//        params.put("receiver", username);
        url=url+"?"+"receiver="+username;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("contents");
                    JSONArray jsonArray_datetime=response.getJSONArray("datetime");
                    JSONArray jsonArray_sender=response.getJSONArray("sender");
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();++i){
                            String content=jsonArray.getString(i);
                            String datetime=jsonArray_datetime.getString(i);
                            String sender=jsonArray_sender.getString(i);

//                            String[] parts=content.split("\r\n");
//                            String title=parts[0];
//                            String detail=content.substring(parts[0].length() + 1, content.length() - 1);
                            senders.add(sender);
                            messages.add(content);
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


//    public void showMessages(){
//
//        int white= Color.parseColor("#ffffff");
//        int black=Color.parseColor("#000000");
//
//        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.message_layout);
////        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
////        ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);
//
//
//        int chatNumbers=10;
//        for(int i=0; i<chatNumbers; ++i){
//
//            final String receiver="jean";
//            LinearLayout sublayout =new LinearLayout(this);
//
//            sublayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),ChatActivity.class);
//                    intent.putExtra("Username",username);
//                    intent.putExtra("Receiver",receiver);
//                    startActivity(intent);
//                }
//            });
//            sublayout.setOrientation(LinearLayout.HORIZONTAL);
//            sublayout.setWeightSum(10);
//            int heightDp=60;
//            int widthDp=0;
//            sublayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(heightDp)));
//
//            //Create textview
//            TextView textView1 = new TextView(this);
//            LinearLayout.LayoutParams textParams1=new LinearLayout.LayoutParams(convertDpToPixels(0), ViewGroup.LayoutParams.MATCH_PARENT,2);
//
//            //ViewGroup.LayoutParams textLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            textView1.setLayoutParams(textParams1);
//            textView1.setText("Jean");
//            textView1.setTextColor(black);
//            textView1.setGravity(Gravity.CENTER_HORIZONTAL);
//            sublayout.addView(textView1);
//
//            TextView textView2 = new TextView(this);
//            LinearLayout.LayoutParams textParams2=new LinearLayout.LayoutParams(convertDpToPixels(0), ViewGroup.LayoutParams.MATCH_PARENT,8);
//
//            //ViewGroup.LayoutParams textLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            textView2.setLayoutParams(textParams2);
//            textView2.setText("Hello");
//            textView2.setGravity(Gravity.CENTER_HORIZONTAL);
//            sublayout.addView(textView2);
//
//            //Draw a bottom line
//            View line=new View(this);
//            int lineHeight=1;
//            ViewGroup.LayoutParams lineParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(lineHeight));
//            line.setLayoutParams(lineParams);
//            line.setBackgroundColor(black);
//
//            linearLayout.addView(sublayout);
//            linearLayout.addView(line);
//        }
//
//    }
//
//    private int convertDpToPixels(int dp){
//        int p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
//        return p;
//    }
//
//
//    public void send(){
//        String url ="http://lunar-mercury-124221.appspot.com";
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast toast=Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<>();
//                // the POST parameters:
//                params.put("sender", "bshi");
//                params.put("receiver", "jean");
////                params.put("contents",textSend.getText().toString());
//                return params;
//            }
//        };
//        Volley.newRequestQueue(this).add(postRequest);
//    }


}
