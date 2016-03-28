package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountShowOrderActivity extends AppCompatActivity {

    private String username;
    private JSONArray posts;
    private Map<Long, JSONObject> postMap;

    private LinearLayout postsContainer;

    public final static String TAG = AccountShowOrderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_show_order);

        postsContainer = (LinearLayout) findViewById(R.id.order_linear_layout);
        postMap = new HashMap<>();

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");

        getOrders(username);
        //showOrders();

        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMe(v);
            }
        });

        ImageButton addOrderButton=(ImageButton) findViewById(R.id.accountPost);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), AccountAddOrderActivity.class);
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

        ImageButton searchButton=(ImageButton) findViewById(R.id.accountHome);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(v.getContext(), SearchPostActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }


    public void returnToMe(View view){
        Intent intent=new Intent(view.getContext(), AccountActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    /*
    public void showOrders(){
        int white= Color.parseColor("#ffffff");
        int black=Color.parseColor("#000000");
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.order_linear_layout);
//        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
//        ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);


        int orderNumbers=10;
        for(int i=0; i<orderNumbers; ++i){
            LinearLayout sublayout =new LinearLayout(this);
            sublayout.setOrientation(LinearLayout.HORIZONTAL);
            int heightDp=60;
            sublayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(heightDp), 8));
            //Create textview
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);

            //ViewGroup.LayoutParams textLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(textParams);
            textView.setText("Order No:\n Date:\n Details:");

            //Create ImageButton
            ImageButton expandButton=new ImageButton(this);
            LinearLayout.LayoutParams buttonParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,7);
            expandButton.setImageResource(R.drawable.arrow);
            expandButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            expandButton.setBackgroundColor(Color.TRANSPARENT);

            sublayout.addView(textView);
            sublayout.addView(expandButton);

            //Draw a bottom line
            View line=new View(this);
            int lineHeight=1;
            ViewGroup.LayoutParams lineParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(lineHeight));
            line.setLayoutParams(lineParams);
            line.setBackgroundColor(black);

            linearLayout.addView(sublayout);
            linearLayout.addView(line);
        }

    }*/


    private int convertDpToPixels(int dp){
        int p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return p;
    }


    private void getOrders(String username) {
        String base = getResources().getString(R.string.base_url);
        String query = "id=" + username;
        URI uri = null;
        try {
            uri = new URI("http", base, "/user/posts", query, null);
        } catch (URISyntaxException e) {
            Log.e(AccountShowOrderActivity.TAG, e.getMessage());
        }

        JsonObjectRequest getPosts = new JsonObjectRequest(Request.Method.GET, uri.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            posts = response.getJSONArray("posts");
                            Log.d(AccountShowOrderActivity.TAG, posts.toString());

                            showOrders();
                        } catch (JSONException e) {
                            Log.e(AccountShowOrderActivity.TAG, e.getMessage());
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
        GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(getPosts);
    }


    private void showOrders() {
        if (posts == null) return;

        String base = getResources().getString(R.string.base_url);
        URI uri = null;

        for (int i = 0; i < posts.length(); ++i) {
            final long postId = posts.optLong(i);
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

                                String[] skills = post.optString("skill").split(">");
                                String[] prices = post.optString("price").split(">");

                                skill.setText(skills[skills.length - 1]);
                                price.setText(prices[prices.length - 1]);
                                id.setText(String.valueOf(postId));

                                postsContainer.addView(itemLayout);
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

            Intent intent = new Intent(AccountShowOrderActivity.this, PostDetailActivity.class);
            intent.putExtra("post", postBundle);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String idRemoved = data.getStringExtra("id");
            for (int i = postsContainer.getChildCount() - 1; i >= 0; --i) {
                LinearLayout wrapper = (LinearLayout) postsContainer.getChildAt(i);
                TextView id = (TextView) wrapper.findViewById(R.id.post_id);
                if (id.getText().toString().equals(idRemoved)) {
                    postsContainer.removeView(wrapper);
                    break;
                }
            }
        }
    }
}


//package com.example.bowenshi.intersaclay;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class AccountShowOrderActivity extends AppCompatActivity {
//
//    String username;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_account_show_order);
//        Intent intent=getIntent();
//        username=intent.getStringExtra("Username");
//        showOrders();
//
//        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
//        meButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                returnToMe(v);
//            }
//        });
//
//        ImageButton addOrderButton=(ImageButton) findViewById(R.id.accountPost);
//        addOrderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(v.getContext(), AccountAddOrderActivity.class);
//                intent.putExtra("Username",username);
//                startActivity(intent);
//            }
//        });
//
//        ImageButton editInfoButton=(ImageButton) findViewById(R.id.accountMore);
//        editInfoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), AboutAppActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//            }
//        });
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//    }
//
//
//    public void returnToMe(View view){
//        Intent intent=new Intent(view.getContext(), AccountActivity.class);
//        intent.putExtra("Username",username);
//        startActivity(intent);
//
//    }
//
//
//    public void showOrders(){
//        int white= Color.parseColor("#ffffff");
//        int black=Color.parseColor("#000000");
//        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.order_linear_layout);
////        ImageButton meButton=(ImageButton) findViewById(R.id.accountMe);
////        ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);
//
//
//        int orderNumbers=10;
//        for(int i=0; i<orderNumbers; ++i){
//            LinearLayout sublayout =new LinearLayout(this);
//            sublayout.setOrientation(LinearLayout.HORIZONTAL);
//            int heightDp=60;
//            sublayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(heightDp), 8));
//            //Create textview
//            TextView textView = new TextView(this);
//            LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
//
//            //ViewGroup.LayoutParams textLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            textView.setLayoutParams(textParams);
//            textView.setText("Order No:\n Date:\n Details:");
//
//            //Create ImageButton
//            ImageButton expandButton=new ImageButton(this);
//            LinearLayout.LayoutParams buttonParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,7);
//            expandButton.setImageResource(R.drawable.arrow);
//            expandButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            expandButton.setBackgroundColor(Color.TRANSPARENT);
//
//            sublayout.addView(textView);
//            sublayout.addView(expandButton);
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
//
//    private int convertDpToPixels(int dp){
//        int p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
//        return p;
//    }
//
//}
