package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SystemMessageDetailActivity extends AppCompatActivity {

    String username;
    String title;
    String detail;
    String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message_detail);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        title=intent.getStringExtra("Title");
        detail=intent.getStringExtra("Detail");
        datetime=intent.getStringExtra("Datetime");

        TextView titleView=(TextView) findViewById(R.id.system_message_detail_title);
        TextView contentView=(TextView) findViewById(R.id.system_message_detail_contents);
        TextView datetimeView=(TextView) findViewById(R.id.system_message_detail_datetime);

        titleView.setText(title);
        contentView.setText(detail);
        datetimeView.setText(datetime);


        Toolbar toolbar=(Toolbar) findViewById(R.id.system_message_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), SystemMessageActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            }
        });
    }
}
