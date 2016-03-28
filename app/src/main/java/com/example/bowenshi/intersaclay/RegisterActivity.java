package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.view.KeyEvent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    EditText nameEdit;
    EditText adrsEdit;
    EditText emailEdit;
    EditText pswEdit;
    EditText userNameEdit;
    EditText schoolEdit;
    EditText confirmPswEdit;
    Spinner sexSpinner;
    String username;
    String name;
    String sex;
    String adrs;
    String email;
    String school;
    String psw;

    public static final String TAG = RegisterActivity.class.getSimpleName();
    PISQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent=getIntent();

        pswEdit=(EditText)findViewById(R.id.pswEdit);
        pswEdit.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirmPswEdit=(EditText)findViewById(R.id.confirmEdit);
        confirmPswEdit.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        db=PISQLiteHelper.getInstance(this.getApplicationContext());
//        RelativeLayout layout=(RelativeLayout) findViewById(R.id.)

        Button register=(Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordLocal(v);
                record(v);

            }
        });
    }


    public void record(View view) {
        userNameEdit=(EditText) findViewById(R.id.pseudoNameEdit);
        nameEdit=(EditText) findViewById(R.id.nameEdit);
        adrsEdit=(EditText) findViewById(R.id.adrsEdit);
        emailEdit=(EditText) findViewById(R.id.emailEdit);
        schoolEdit=(EditText) findViewById(R.id.schoolEdit);
        confirmPswEdit=(EditText)findViewById(R.id.confirmEdit);
        sexSpinner=(Spinner) findViewById(R.id.sexSpin);

        username=userNameEdit.getText().toString();
        name=nameEdit.getText().toString();
        sex=sexSpinner.getSelectedItem().toString();
        adrs=adrsEdit.getText().toString();
        email=emailEdit.getText().toString();
        school=schoolEdit.getText().toString();
        psw=pswEdit.getText().toString();
        String confirmPsw=confirmPswEdit.getText().toString();

        if (!checkPsw(psw, confirmPsw)) {
            Toast.makeText(getApplicationContext(), "Password Not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(),"Wrong Email Format",Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> attributes = new HashMap<>();
        attributes.put("username", username);
        attributes.put("psw", psw);
        attributes.put("name", name);
        attributes.put("gender", sex);
        attributes.put("address", adrs);
        attributes.put("email", email);
        attributes.put("school", school);
        JSONObject info = new JSONObject(attributes);

        String base = getResources().getString(R.string.base_url);
        URI uri = null;
        try {
            uri = new URI("http", base, "/user/register", null, null);
        } catch (URISyntaxException e) {
            Log.e(RegisterActivity.TAG, e.getMessage());
        }

        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, uri.toString(),
                info.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent = new Intent(RegisterActivity.this, AccountActivity.class);
                            intent.putExtra("Username", response.getString("id"));
                            startActivity(intent);
                        } catch (JSONException error) {
                            Log.e(RegisterActivity.TAG, error.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(getApplicationContext(), "username existed", Toast.LENGTH_SHORT).show();
//                    Log.e(AccountShowOrderActivity.TAG, error.networkResponse.toString());
                }
//                Log.e(AccountActivity.TAG, toString());
            }
        });
        GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(registerRequest);
    }


    public int recordLocal(View view){
        Context context = getApplicationContext();
        String filename="MyInfo.txt";
        userNameEdit=(EditText) findViewById(R.id.pseudoNameEdit);
        nameEdit=(EditText) findViewById(R.id.nameEdit);
        adrsEdit=(EditText) findViewById(R.id.adrsEdit);
        emailEdit=(EditText) findViewById(R.id.emailEdit);
        schoolEdit=(EditText) findViewById(R.id.schoolEdit);
        confirmPswEdit=(EditText)findViewById(R.id.confirmEdit);
        sexSpinner=(Spinner) findViewById(R.id.sexSpin);

        String info=new String();
        username=userNameEdit.getText().toString();
        name=nameEdit.getText().toString();
        sex=sexSpinner.getSelectedItem().toString();
        adrs=adrsEdit.getText().toString();
        email=emailEdit.getText().toString();
        school=schoolEdit.getText().toString();
        psw=pswEdit.getText().toString();
        String confirmPsw=confirmPswEdit.getText().toString();

        if(checkPsw(psw,confirmPsw)==false){
            Toast inValidPsw=Toast.makeText(context,"Password Not Match",Toast.LENGTH_SHORT);
            inValidPsw.show();
            return -1;
        }

        if(isValidEmail(email)==false){
            Toast inValidEmail=Toast.makeText(context,"Wrong Email Format",Toast.LENGTH_SHORT);
            inValidEmail.show();
            return -1;
        }

        info+=username+"$"+name+"$"+sex+"$";
        info+=(adrs+"$"+email+"$"+school+"$"+psw+"\n");

        PersonalFileOperator pfo=new PersonalFileOperator();
        pfo.addItem(info);

        PersonalInformation user=new PersonalInformation(username,"");
        db.createUser(user);

//        CharSequence text = "Successfully Registered!";
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
//        Intent intent=new Intent(this,AccountActivity.class);
//        intent.putExtra("Username",username);
//        intent.putExtra("School",school);
//        intent.putExtra("Psw", psw);
//        intent.putExtra("Email",email);
//        startActivity(intent);
        return 0;
    }

//    public void writeToFile(String filename, String data){
//        File root= Environment.getExternalStorageDirectory();
//        File dir=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo");
//        boolean isExisted=dir.exists();
//        if(!isExisted)
//            dir.mkdirs();
//        File file=new File(dir, "myData.txt");
//        if(!file.exists()){
//            try{
//                file.createNewFile();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        try {
//            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
//            bufferedWriter.write(data);
//            bufferedWriter.close();
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    public void cancel(View view){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public static boolean checkPsw(String psw1, String psw2){
        if(psw1.equals(psw2))
            return true;
        else{
            return false;
        }
    }

    public static boolean isValidEmail(String email){
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }



    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() { }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_display_message,
//                    container, false);
//            return rootView;
//        }
//    }


}



//package com.example.bowenshi.intersaclay;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.Toast;
//import android.content.Intent;
//import android.view.KeyEvent;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
//public class RegisterActivity extends AppCompatActivity {
//
//    EditText nameEdit;
//    EditText adrsEdit;
//    EditText emailEdit;
//    EditText pswEdit;
//    EditText userNameEdit;
//    EditText schoolEdit;
//    EditText confirmPswEdit;
//    Spinner sexSpinner;
//    String username;
//    String name;
//    String sex;
//    String adrs;
//    String email;
//    String school;
//    String psw;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        Intent intent=getIntent();
//
//        pswEdit=(EditText)findViewById(R.id.pswEdit);
//        pswEdit.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//        confirmPswEdit=(EditText)findViewById(R.id.confirmEdit);
//        confirmPswEdit.setTransformationMethod(new AsteriskPasswordTransformationMethod());
////        RelativeLayout layout=(RelativeLayout) findViewById(R.id.)
//    }
//
//
//    public int record(View view){
//        Context context = getApplicationContext();
//        String filename="MyInfo.txt";
//        userNameEdit=(EditText) findViewById(R.id.pseudoNameEdit);
//        nameEdit=(EditText) findViewById(R.id.nameEdit);
//        adrsEdit=(EditText) findViewById(R.id.adrsEdit);
//        emailEdit=(EditText) findViewById(R.id.emailEdit);
//        schoolEdit=(EditText) findViewById(R.id.schoolEdit);
//        confirmPswEdit=(EditText)findViewById(R.id.confirmEdit);
//        sexSpinner=(Spinner) findViewById(R.id.sexSpin);
//
//        String info=new String();
//        username=userNameEdit.getText().toString();
//        name=nameEdit.getText().toString();
//        sex=sexSpinner.getSelectedItem().toString();
//        adrs=adrsEdit.getText().toString();
//        email=emailEdit.getText().toString();
//        school=schoolEdit.getText().toString();
//        psw=pswEdit.getText().toString();
//        String confirmPsw=confirmPswEdit.getText().toString();
//
//        if(checkPsw(psw,confirmPsw)==false){
//            Toast inValidPsw=Toast.makeText(context,"Password Not Match",Toast.LENGTH_SHORT);
//            inValidPsw.show();
//            return -1;
//        }
//
//        if(isValidEmail(email)==false){
//            Toast inValidEmail=Toast.makeText(context,"Wrong Email Format",Toast.LENGTH_SHORT);
//            inValidEmail.show();
//            return -1;
//        }
//
//        info+=username+"$"+name+"$"+sex+"$";
//        info+=(adrs+"$"+email+"$"+school+"$"+psw+"\n");
//
//        PersonalFileOperator pfo=new PersonalFileOperator();
//        pfo.addItem(info);
//
//        CharSequence text = "Successfully Registered!";
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
//        Intent intent=new Intent(this,AccountActivity.class);
//        intent.putExtra("Username",username);
//        intent.putExtra("School",school);
//        intent.putExtra("Psw", psw);
//        intent.putExtra("Email",email);
//        startActivity(intent);
//        return 0;
//    }
//
////    public void writeToFile(String filename, String data){
////        File root= Environment.getExternalStorageDirectory();
////        File dir=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo");
////        boolean isExisted=dir.exists();
////        if(!isExisted)
////            dir.mkdirs();
////        File file=new File(dir, "myData.txt");
////        if(!file.exists()){
////            try{
////                file.createNewFile();
////            }
////            catch (Exception e)
////            {
////                e.printStackTrace();
////            }
////        }
////        try {
////            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
////            bufferedWriter.write(data);
////            bufferedWriter.close();
////
////        }
////        catch (Exception e){
////            e.printStackTrace();
////        }
////    }
//
//
//    public void cancel(View view){
//        Intent intent=new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//
//
//    public static boolean checkPsw(String psw1, String psw2){
//        if(psw1.equals(psw2))
//            return true;
//        else{
//            return false;
//        }
//    }
//
//    public static boolean isValidEmail(String email){
//        boolean isValid = false;
//
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        CharSequence inputStr = email;
//
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(inputStr);
//        if (matcher.matches()) {
//            isValid = true;
//        }
//        return isValid;
//
//    }
//
//
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
////    public static class PlaceholderFragment extends Fragment {
////
////        public PlaceholderFragment() { }
////
////        @Override
////        public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                                 Bundle savedInstanceState) {
////            View rootView = inflater.inflate(R.layout.fragment_display_message,
////                    container, false);
////            return rootView;
////        }
////    }
//
//
//}
