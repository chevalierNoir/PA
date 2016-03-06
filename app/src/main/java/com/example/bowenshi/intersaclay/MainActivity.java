package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText nameEdit;
    EditText pswEdit;

    String name;
    String adrs;
    String school;
    String email;
    String psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        nameEdit=(EditText)findViewById(R.id.usernameEdit);
        pswEdit=(EditText)findViewById(R.id.pswEdit);
        nameEdit.setText("");
        pswEdit.setText("");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {
            register();
            return true;
        }

        if (id == R.id.action_tryapp) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void register(){

        Intent register=new Intent(this, RegisterActivity.class);
        startActivity(register);

    }

    public int login(View view){

        name=nameEdit.getText().toString();
        psw=pswEdit.getText().toString();
        PersonalFileOperator pfo=new PersonalFileOperator();
        if(pfo.hasUser(name)==false){
            Toast toast=Toast.makeText(getApplicationContext(),"No User!", Toast.LENGTH_SHORT);
            toast.show();
            return -1;
        }
        String[] item=pfo.readItem(name);
        //Get password
        String correctPsw=item[6];
        RegisterActivity registerActivity=new RegisterActivity();
        if(registerActivity.checkPsw(correctPsw,psw)==false){
            Toast toast=Toast.makeText(getApplicationContext(),"Wrong Password!", Toast.LENGTH_SHORT);
            toast.show();
            return -1;
        }
        Intent login=new Intent(this, AccountActivity.class);
        login.putExtra("Username",name);
//        login.putExtra("School",school);
//        login.putExtra("Adrs", adrs);
//        login.putExtra("Email",email);
//        login.putExtra("Psw", psw);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        return 0;
    }

    public boolean matchNamePsw(String name, String psw){

        String rightPsw=new String();
        boolean hasUser=false;
        FileInputStream is;
        File root= Environment.getExternalStorageDirectory();
        File file=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myData.txt");

        if(!file.exists()){
            return false;
        }
        try{
            is=new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        BufferedReader bufferedReader;
//        InputStreamReader streamReader=new InputStreamReader(is);
        try{
            bufferedReader=new BufferedReader(new FileReader(file));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        String line=new String("init");
        try{
            while(((line=bufferedReader.readLine())!=null)){

                String[] parts=line.split("/");
                if(parts[0].equals(name)){
                    hasUser=true;
                    rightPsw=parts[parts.length-1];
                    adrs=parts[1];
                    school=parts[5];
                    email=parts[2];
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        if(rightPsw.equals(psw) && hasUser==true){
            return true;
        }
        else{
            return false;
        }
    }



}
