package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class SearchPostActivity extends AppCompatActivity {

    private String username;
    TopicSelector skillSearched;

    private static final String TAG = SearchPostActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("Username");

        SkillTree tree = new SkillTree(this, R.raw.hierarchy);
        LinearLayout skillSearchedContainer = (LinearLayout) findViewById(R.id.skill_search);
        skillSearched = new TopicSelector(this, skillSearchedContainer,
                tree.getTopics(), tree.getSubTopics());

        ImageButton addOrderButton=(ImageButton) findViewById(R.id.accountPost);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), AccountAddOrderActivity.class);
                intent.putExtra("Username",username);
                startActivity(intent);
            }
        });

        ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);
        showOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),AccountShowOrderActivity.class);
                intent.putExtra("Username",username);
                startActivity(intent);
            }
        });

        ImageButton editInfoButton=(ImageButton) findViewById(R.id.accountMore);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutAppActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AccountActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
    }

    public void searchPost(View v) {
        String base = getResources().getString(R.string.base_url);
        URI uri = null;
        try {
            uri = new URI("http", base, "/post/search", null, null);
        } catch (URISyntaxException e) {
            Log.e(TAG + "_uri", e.toString());
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("skill", this.skillSearched.toString());
        } catch (JSONException e) {
            Log.e(TAG + "_json", e.toString());
        }

        JsonObjectRequest getPosts = new JsonObjectRequest(Request.Method.POST, uri.toString(),
                requestBody.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray posts = response.getJSONArray("posts");

                            CharSequence[] postIds = new CharSequence[posts.length()];
                            for (int i = 0; i < posts.length(); ++i) {
                                postIds[i] = String.valueOf(posts.optLong(i));
                            }

                            Bundle resultBundle = new Bundle();
                            resultBundle.putString("username", username);
                            resultBundle.putCharSequenceArray("posts", postIds);

                            Intent intent = new Intent(SearchPostActivity.this, SearchResultActivity.class);
                            intent.putExtra("result", resultBundle);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Log.e(SearchPostActivity.TAG + "_json", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.e(SearchPostActivity.TAG + "_volley", error.networkResponse.toString());
                }
                Log.e(SearchPostActivity.TAG + "_volley", error.toString());
            }
        });
        GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(getPosts);
    }
}
