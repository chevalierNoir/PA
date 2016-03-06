package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountShowOrderActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_show_order);
        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        showOrders();

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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }


    public void returnToMe(View view){
        Intent intent=new Intent(view.getContext(), AccountActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);

    }


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

    }


    private int convertDpToPixels(int dp){
        int p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return p;
    }

}
