package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class AccountAddOrderActivity extends AppCompatActivity {

    String username;

    private TopicSelector skillProvided;
    private TopicSelector skillWanted;
//    private EditText availability;
    private EditText description;

    private int[][] flags;


    public final static String TAG = AccountAddOrderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_add_order);
        Intent intent=getIntent();
        username=intent.getStringExtra("Username");


        SkillTree tree = new SkillTree(this, R.raw.hierarchy);

        LinearLayout skillProvidedContainer = (LinearLayout) findViewById(R.id.skill_provided);
        skillProvided = new TopicSelector(this, skillProvidedContainer,
                tree.getTopics(), tree.getSubTopics());

        LinearLayout skillWantedContainer = (LinearLayout) findViewById(R.id.skill_wanted);
        skillWanted = new TopicSelector(this, skillWantedContainer,
                tree.getTopics(), tree.getSubTopics());

//        availability = (EditText) findViewById(R.id.availability);
        description = (EditText) findViewById(R.id.description);
        fillTable();

        Button okButton=(Button) findViewById(R.id.account_add_order_ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputValid()) return;

                String base = getResources().getString(R.string.base_url);
                URI uri = null;
                try {
                    uri = new URI("http", base, "/post/new", null, null);
                } catch (URISyntaxException e) {
                    Log.e(AccountAddOrderActivity.TAG, e.getMessage());
                }

                JSONObject post = new JSONObject();
                try {
                    post.put("skill", skillProvided.toString());
                    post.put("price", skillWanted.toString());
                    post.put("availability", "Monday");
                    post.put("description", description.getText().toString());
                    post.put("username", username);
                } catch (JSONException e) {
                    Log.e(AccountAddOrderActivity.TAG, e.getMessage());
                }

                JsonObjectRequest submitPost = new JsonObjectRequest(Request.Method.POST, uri.toString(),
                        post.toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
//                                availability.setText("");
                                description.setText("");

                                Toast.makeText(getApplicationContext(),
                                        "Post created", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AccountAddOrderActivity.this,
                                        AccountActivity.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(getApplicationContext(),
                                        "bad request", Toast.LENGTH_SHORT).show();
                            }
                            Log.e(AccountAddOrderActivity.TAG, error.networkResponse.toString());
                        } else {
                            Log.e(AccountAddOrderActivity.TAG, error.toString());
                        }
                    }
                });
                GlobalRequestQueue.getInstance(getApplication()).addToRequestQueue(submitPost);
            }
        });

        Button cancelButton=(Button) findViewById(R.id.account_add_order_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(v.getContext(), AccountActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    public void fillTable(){

        //Change if still much time
        flags=new int[8][4];
        for(int i=0;i<8;++i){
            for (int j=0; j<4; ++j){
                flags[i][j]=0;
            }
        }
        int rowCount=8;
        TableLayout tableLayout=(TableLayout) findViewById(R.id.add_order_table);
        for(int i=0; i<rowCount; ++i){
            fillRow(tableLayout, i);
        }

    }

    public void fillRow(TableLayout table, final int indexRow){

        final String[] days = { "Time", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday", "Sunday" };

        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View fullRow=inflater.inflate(R.layout.table_rows, null, false);

        TextView[] view=new TextView[4];
        view[0]=(TextView) fullRow.findViewById(R.id.table_col0);
        view[1]=(TextView) fullRow.findViewById(R.id.table_col1);
        view[2]=(TextView) fullRow.findViewById(R.id.table_col2);
        view[3]=(TextView) fullRow.findViewById(R.id.table_col3);
        view[0].setText(days[indexRow]);
        if(indexRow==0){
            view[1].setText("Morning");
            view[2].setText("Afternoon");
            view[3].setText("Evening");
        }


        for(int i=0;i<4; ++i){
            view[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Drawable img_filled = ContextCompat.getDrawable(v.getContext(), R.drawable.cell_shape_filled);
                    Drawable img = ContextCompat.getDrawable(v.getContext(), R.drawable.cell_shape);

                    if (v.getTag().equals("1")) {
                        v.setTag("0");
                        v.setBackground(img_filled);
                    } else {
                        v.setTag("1");
                        v.setBackground(img);
                    }
//                    int white= Color.parseColor("#ffffff");
//                    v.setBackgroundColor(white);
//                    v.setBackground(img);
                }
            });
        }

        table.addView(fullRow);
    }

    private boolean isInputValid() {
        boolean rt = true;

        if (rt && !skillWanted.isTopicSelected()) {
            rt = false;
            Toast.makeText(AccountAddOrderActivity.this, "Choose Skill Wanted Before Post",
                    Toast.LENGTH_LONG).show();
        }

        if (rt && !skillWanted.isSubTopicSelected()) {
            skillWanted.defaultSubTopic();
        }

        if (rt && !skillProvided.isTopicSelected()) {
            rt = false;
            Toast.makeText(AccountAddOrderActivity.this, "Choose Skill Provided Before Post",
                    Toast.LENGTH_LONG).show();
        }

        if (rt && !skillProvided.isSubTopicSelected()) {
            skillProvided.defaultSubTopic();
        }

//        if (rt && availability.getText().toString().equals("")) {
//            rt = false;
//            Toast.makeText(AccountAddOrderActivity.this, "Fill in Availability Before Post",
//                    Toast.LENGTH_LONG).show();
//        }

        return rt;
    }


}
