package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity {

    String username;
    String receiver;
    String olderResponse;
    boolean inChat;
    boolean receiveFinished;

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private Lock lock;

    private boolean loadHistoryFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar=(Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        receiver=intent.getStringExtra("Sender");//Sender is the receiver in last activity
        inChat=true;
        receiveFinished=false;
        loadHistoryFinished=false;
        lock=new ReentrantLock();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inChat = false;
                while (!receiveFinished) {
                    int time_ms = 200;
                    try {
                        Thread.sleep(time_ms);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(v.getContext(), MessageActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            }
        });

        initSetup();
//        int timeWaitHistory=0;
//        while (!loadHistoryFinished){
//            try{
//                int time_ms=200;
//                Thread.sleep(time_ms);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        new AsyncReceiveMessage().execute();


    }

    private void initSetup() {
        messagesContainer = (ListView) findViewById(R.id.chat_messagesContainer);
        messageET = (EditText) findViewById(R.id.chat_messageEdit);
        sendBtn = (Button) findViewById(R.id.chat_sendButton);

        TextView meLabel = (TextView) findViewById(R.id.chat_meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.chat_friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.chat_container);
        companionLabel.setText(receiver);
        meLabel.setText("Me");

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        loadHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");

                lock.lock();
                displayMessage(chatMessage);
                lock.unlock();
                send(chatMessage.getMessage());
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

//    private void loadHistory(){
//
//        chatHistory = new ArrayList<ChatMessage>();
//
//        ChatMessage msg = new ChatMessage();
//        msg.setId(1);
//        msg.setMe(false);
//        msg.setMessage("Hi");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg);
//        ChatMessage msg1 = new ChatMessage();
//        msg1.setId(2);
//        msg1.setMe(false);
//        msg1.setMessage("How r u doing???");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg1);
//
//
//
//        for(int i=0; i<chatHistory.size(); i++) {
//            ChatMessage message = chatHistory.get(i);
//            displayMessage(message);
//        }
//    }

    public void loadHistory(){

        String url ="http://lunar-mercury-124221.appspot.com/queryhistory";
        String userField= "user="+username;
        //Check if space
//        Pattern pattern = Pattern.compile("\\s+");
//        Matcher matcher = pattern.matcher(receiver);
//        if( matcher.find()){
//            receiver.replaceAll("\\s+", "\\%20");
//        }
        String otherField= "&other="+receiver;
        url= url+"?"+userField+otherField;

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

                            ChatMessage msg = new ChatMessage();
                            msg.setId(i);
                            msg.setMessage(content);
                            msg.setDate(datetime);
                            if(sender.equals(username)){
                                msg.setMe(true);
                            }
                            else {
                                msg.setMe(false);
                            }
                            lock.lock();
                            displayMessage(msg);
                            lock.unlock();
                        }
                        loadHistoryFinished=true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    loadHistoryFinished=true;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadHistoryFinished=true;
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    public void send(final String str){

        String url ="http://lunar-mercury-124221.appspot.com";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String str=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                EditText editText=(EditText) findViewById(R.id.chat_messageEdit);
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("sender", username);
                params.put("receiver", receiver);
                params.put("contents",str);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void receive(){

        String url ="http://lunar-mercury-124221.appspot.com/query";

//        HashMap<String, String> params=new HashMap<String, String>();
//        params.put("sender", receiver);
//        params.put("receiver", username);

        String senderField="sender="+receiver;
        String receiverField="receiver="+username;
        url=url+"?"+senderField+"&"+receiverField;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray=response.getJSONArray("contents");
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();++i){
                            String content=jsonArray.getString(i);
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//                            String content=jsonObject.toString();

                            ChatMessage msg = new ChatMessage();
                            msg.setId(i);
                            msg.setMe(false);
                            msg.setMessage(content);
                            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                            lock.lock();
                            displayMessage(msg);
                            lock.unlock();

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }



    private class AsyncReceiveMessage extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){

//            cleanMessage();

        }


        @Override
        protected String doInBackground(String... params) {
            while (inChat){
                if(loadHistoryFinished){
                    receive();
                    try {
                        int time_ms=1000;
                        Thread.sleep(time_ms);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            receiveFinished=true;
            return null;
        }

        @Override
        protected void onProgressUpdate(String... result){
            ;

        }

        @Override
        protected void onPostExecute(String result){
            ;

        }


    }

    public void cleanMessage(){
        String url ="http://lunar-mercury-124221.appspot.com/clean";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast toast=Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("sender", "bshi");
                params.put("receiver", "jean");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

}
