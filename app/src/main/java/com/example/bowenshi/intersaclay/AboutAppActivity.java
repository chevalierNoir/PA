package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AboutAppActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");

        ImageButton showOrderButton=(ImageButton) findViewById(R.id.accountOrder);
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
    }
}
