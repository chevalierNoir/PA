package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AboutAppFeedbackActivity extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_feedback);

        final Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        Toolbar toolbar=(Toolbar) findViewById(R.id.about_app_feedback_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(v.getContext(), AboutAppActivity.class);
                returnIntent.putExtra("Username", username);
                startActivity(returnIntent);
            }
        });

        Button submitButton=(Button) findViewById(R.id.about_app_feedback_submit);
        final EditText editText=(EditText) findViewById(R.id.about_app_feedback_edittext);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                Toast toast=Toast.makeText(getApplicationContext(),"Your feedback has been submitted successfully", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }
}
