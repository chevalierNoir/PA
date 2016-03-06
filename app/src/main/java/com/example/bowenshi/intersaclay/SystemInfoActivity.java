package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;

public class SystemInfoActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        Toolbar toolbar=(Toolbar) findViewById(R.id.system_info_toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),AccountActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            }
        });

    }
}
