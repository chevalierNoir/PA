package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AboutAppActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");

        final ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);
        showOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AccountShowOrderActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        ImageButton addOrderButton=(ImageButton) findViewById(R.id.accountPost);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AccountAddOrderActivity.class);
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

        LinearLayout versionUpdate=(LinearLayout) findViewById(R.id.about_app_version);
        versionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(),"Already up to date!", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        LinearLayout help_layout=(LinearLayout) findViewById(R.id.about_app_help);
        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch help activity
                Intent helpIntent=new Intent(v.getContext(),AboutAppHelpActivity.class);
                helpIntent.putExtra("Username",username);
                startActivity(helpIntent);
            }
        });


        final LinearLayout feedback_layout=(LinearLayout) findViewById(R.id.about_app_feedback);
        feedback_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch feedback
                Intent feedbackIntent=new Intent(v.getContext(),AboutAppFeedbackActivity.class);
                feedbackIntent.putExtra("Username", username);
                startActivity(feedbackIntent);
            }
        });
    }
}
