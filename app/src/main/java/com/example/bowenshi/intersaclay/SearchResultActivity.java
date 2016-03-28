package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {

    private CharSequence[] postIds;
    private LinearLayout resultContainer;
    private HashMap<Long, JSONObject> postMap;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle resultBundle = intent.getBundleExtra("result");
        this.username = resultBundle.getString("username");
        this.postIds = resultBundle.getCharSequenceArray("posts");

        this.resultContainer = (LinearLayout) findViewById(R.id.result_container);
        this.postMap = new HashMap<>();

        showOrders();
    }

    private void showOrders() {
        if (postIds == null || postIds.length == 0) return;

        String base = getResources().getString(R.string.base_url);
        URI uri = null;

        for (int i = 0; i < postIds.length; ++i) {
            final long postId = Long.valueOf(postIds[i].toString());
            String query = "id=" + postId;
            try {
                uri = new URI("http", base, "/post/info", query, null);
            } catch (URISyntaxException e) {
                Log.e(AccountShowOrderActivity.TAG, e.getMessage());
            }

            JsonObjectRequest postInfo = new JsonObjectRequest(Request.Method.GET, uri.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject post) {
                            if (post != null) {
                                postMap.put(postId, post);
                                LinearLayout itemLayout = (LinearLayout) getLayoutInflater()
                                        .inflate(R.layout.post_item_layout, null);

                                TextView skill = (TextView) itemLayout.findViewById(R.id.post_skill);
                                TextView price = (TextView) itemLayout.findViewById(R.id.post_price);
                                TextView id    = (TextView) itemLayout.findViewById(R.id.post_id);

                                String[] skillText = post.optString("skill").split(">");
                                String[] priceText = post.optString("price").split(">");

                                skill.setText(skillText[skillText.length - 1]);
                                price.setText(priceText[priceText.length - 1]);
                                id.setText(String.valueOf(postId));

                                resultContainer.addView(itemLayout);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        Log.e(AccountShowOrderActivity.TAG, error.networkResponse.toString());
                    }
                    Log.e(AccountShowOrderActivity.TAG, error.toString());
                }
            });
            GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(postInfo);
        }
    }

    public void showDetail(View v) {

        String id = ((TextView) v.findViewById(R.id.post_id)).getText().toString();
        JSONObject post = postMap.get(Long.valueOf(id));

        if (post != null) {
            Bundle postBundle = new Bundle();
            postBundle.putString("me", this.username);
            postBundle.putString("id", id);
            postBundle.putString("skill", post.optString("skill"));
            postBundle.putString("price", post.optString("price"));
            postBundle.putString("username", post.optString("username"));
            postBundle.putString("availability", post.optString("availability"));
            postBundle.putString("description", post.optString("description"));

            JSONArray postComments = post.optJSONArray("comments");
            if (postComments != null && postComments.length() != 0) {

                ArrayList<String> comments = new ArrayList<>();

                for (int i = postComments.length(); i > 0; --i)
                    comments.add(postComments.optString(i));

                postBundle.putStringArrayList("comments", comments);
            } else {
                postBundle.putStringArrayList("comments", new ArrayList<String>());
            }

            Intent intent = new Intent(SearchResultActivity.this, PostDetailActivity.class);
            intent.putExtra("post", postBundle);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String idRemoved = data.getStringExtra("id");
            for (int i = resultContainer.getChildCount() - 1; i >= 0; --i) {
                LinearLayout wrapper = (LinearLayout) resultContainer.getChildAt(i);
                TextView id = (TextView) wrapper.findViewById(R.id.post_id);
                if (id.getText().toString().equals(idRemoved)) {
                    resultContainer.removeView(wrapper);
                    break;
                }
            }
        }
    }
}
