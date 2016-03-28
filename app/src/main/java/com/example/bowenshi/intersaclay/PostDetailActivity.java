package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {

    private String username;
    private String id;
    private String tutor;

    private static final String TAG = PostDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        Bundle postBundle = intent.getBundleExtra("post");

        this.username = postBundle.getString("me");
        this.id = postBundle.getString("id");
        ((TextView) findViewById(R.id.post_skill)).setText(postBundle.getString("skill"));
        this.tutor = postBundle.getString("username");
        ((TextView) findViewById(R.id.post_tutor)).setText(this.tutor);
        ((TextView) findViewById(R.id.post_price)).setText(postBundle.getString("price"));
        ((TextView) findViewById(R.id.post_availability)).setText(postBundle.getString("availability"));
        ((TextView) findViewById(R.id.post_description)).setText(postBundle.getString("description"));

        Button post_button = (Button) findViewById(R.id.post_button);
        if (!this.username.equals(this.tutor)) {
            post_button.setText("I'm interested.");
            post_button.setOnClickListener(new SendInterestButtonClickListener());
        } else {
            post_button.setText("Remove this post");
            post_button.setOnClickListener(new RemovePostButtonClickListener());
        }

        ArrayList<String> comments = postBundle.getStringArrayList("comments");
        if (comments != null && comments.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, comments);
            ((ListView) findViewById(R.id.post_comments)).setAdapter(adapter);
        }
    }

//    class SendInterestButtonClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            String message = "From: " + PostDetailActivity.this.username
//                    + " To: " + PostDetailActivity.this.tutor
//                    + " Msg: I'm interested.";
//            Toast.makeText(PostDetailActivity.this, message, Toast.LENGTH_LONG).show();
//        }
//    }

    class SendInterestButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "From: " + PostDetailActivity.this.username
                    + " To: " + PostDetailActivity.this.tutor
                    + " Msg: I'm interested.";
            Toast.makeText(PostDetailActivity.this, message, Toast.LENGTH_LONG).show();

            final String msg="Hello Buddy, I am interested";

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
                    Map<String, String>  params = new HashMap<>();
                    // the POST parameters:
                    params.put("sender", PostDetailActivity.this.username);
                    params.put("receiver", PostDetailActivity.this.tutor);
                    params.put("contents",msg);
                    return params;
                }
            };

            Volley.newRequestQueue(v.getContext()).add(stringRequest);
        }
    }

    class RemovePostButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String base = getResources().getString(R.string.base_url);
            String query = "id=" + id;

            URI uri = null;
            try {
                uri = new URI("http", base, "/post/remove", query, null);
            } catch (URISyntaxException e) {
                Log.e(PostDetailActivity.TAG + "_uri", e.toString());
            }

            JsonObjectRequest removeRequest = new JsonObjectRequest(Request.Method.POST, uri.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Post Removed", Toast.LENGTH_LONG).show();

                            Intent rt = new Intent();
                            rt.putExtra("id", id);
                            setResult(RESULT_OK, rt);
                            PostDetailActivity.this.finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(PostDetailActivity.TAG + "_volley", error.toString());
                            return;
                        }
                    });
            GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(removeRequest);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
