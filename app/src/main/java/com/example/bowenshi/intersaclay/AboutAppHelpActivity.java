package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutAppHelpActivity extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_help);

        final Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        Toolbar toolbar=(Toolbar) findViewById(R.id.about_app_help_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent(v.getContext(), AboutAppActivity.class);
                returnIntent.putExtra("Username", username);
                startActivity(returnIntent);

            }
        });


    }
}
