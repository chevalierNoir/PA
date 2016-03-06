package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

public class AccountAddOrderActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_add_order);
        Intent intent=getIntent();
        username=intent.getStringExtra("Username");


        Button okButton=(Button) findViewById(R.id.account_add_order_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Successfully Posted!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(v.getContext(), AccountActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
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

//    public String loadJSONFromAssets(){
//        String json=null;
//        try{
//            InputStream is=getAssets().open("hierarchy.json");
//            int size=is.available();
//            byte[] buffer=new byte[];
//            is.read(buffer);
//            is.close();
//            json=new String(buffer,"UTF-8");
//
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return json;
//    }


//    public void getData(String data){
//        String json=loadJSONFromAssets();
//        try{
//            JSONObject obj=new JSONObject(json);
//            SkillTree skillTree=new SkillTree();
//            skillTree.name=obj.getString("name");
//            skillTree.value=obj.get
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


}
