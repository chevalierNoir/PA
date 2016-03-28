package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;

public class AccountActivity extends AppCompatActivity {

    String username;
    String school;
    String psw;
    String email;
    TextView nameTextView;
    TextView schoolTextView;
    TextView skillTextView;
    TextView selfDesTextview;
    ImageView accountMainUserImage;

    public final static String TAG = AccountActivity.class.getSimpleName();

    PISQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account);
        initToolBar();

        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        nameTextView=(TextView) findViewById(R.id.account_textview_name);
        schoolTextView=(TextView) findViewById(R.id.account_textview_school);
        skillTextView=(TextView) findViewById(R.id.account_textview_skill);
        selfDesTextview=(TextView) findViewById(R.id.account_textview_avail);
        accountMainUserImage=(ImageView) findViewById(R.id.userImage);

//        db=new PISQLiteHelper(getApplicationContext());

        db= PISQLiteHelper.getInstance(this.getApplicationContext());
//        fillForm(username);

        fillFormLocal();

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
                Intent intent=new Intent(v.getContext(),AccountAddOrderActivity.class);
                intent.putExtra("Username",username);
                startActivity(intent);
            }
        });

        ImageButton editInfoButton=(ImageButton) findViewById(R.id.accountMore);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),AboutAppActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
//                Intent intent = new Intent(v.getContext(), AccountEditInfoActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
            }
        });

        ImageButton systemInfoButton=(ImageButton) findViewById(R.id.account_system_message);
        systemInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),SystemMessageActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        ImageButton messageButton=(ImageButton) findViewById(R.id.account_user_message);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MessageActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        ImageButton searchButton=(ImageButton) findViewById(R.id.accountHome);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(v.getContext(), SearchPostActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });


    }

    public void fillForm(String id) {
        String base = getResources().getString(R.string.base_url);
        String query = "id=" + id;
        URI uri = null;
        try {
            uri = new URI("http", base, "/user/info", query, null);
        } catch (URISyntaxException e) {
            Log.e(AccountActivity.TAG, e.getMessage());
        }

        JsonObjectRequest infoRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            nameTextView.append(response.getString("name"));
                            schoolTextView.append(response.getString("school"));

                            /*
                            JSONArray skills = response.optJSONArray("skills");
                            if (skills != null && skills.length() != 0) {
                                StringBuilder skillSB = new StringBuilder();

                                for (int i = skills.length() - 1; i >= 0; --i) {
                                    String skill = skills.getString(i);
                                    String[] skillChain = skill.split(">");
                                    String skillName = skillChain[skillChain.length - 1];
                                    if (!skillName.equals("Autres")) {
                                        skillSB.append(skillName).append(';');
                                    }
                                }

                                skillTextView.append(skillSB.toString());
                            }*/
                        } catch (JSONException error) {
                            Log.e(AccountActivity.TAG, error.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 404)
                        Toast.makeText(getApplicationContext(), "username not found", Toast.LENGTH_SHORT).show();
                    Log.e(AccountShowOrderActivity.TAG, error.networkResponse.toString());
                }
                Log.e(AccountActivity.TAG, error.toString());
            }
        });
        GlobalRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(infoRequest);
    }

    public void fillFormLocal(){

        String usernameNewline=username+"\n";
        nameTextView.append(usernameNewline);
        //Add school item
        PersonalFileOperator pfo=new PersonalFileOperator();
        String[] personalInfo=pfo.readItem(username);
        school=personalInfo[5];
        String schoolNewline=school+"\n";
        schoolTextView.append(schoolNewline);

        if(personalInfo[7]!=null){
            //Add Image to ImageView

            Bitmap bitmap;
            Uri userImageUri=Uri.parse(personalInfo[7]);
            try{
                String convertedPath=getRealPathFromURI(userImageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;
                bitmap=BitmapFactory.decodeFile(convertedPath,options);

                //bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(userImageUri));
                accountMainUserImage.setImageBitmap(bitmap);
//                accountMainUserImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
                int a=0;
            }
        }

        if(personalInfo[8]!=null){
            //Add Skill to textView
            skillTextView.append(personalInfo[8]);
        }

        //Append self description

//        db.onUpgrade(db.getWritableDatabase(),1,2);
//        db.createUser(new PersonalInformation(username, "First SD"));
        PersonalInformation user=db.readUser(username);
        selfDesTextview.append(user.getSelfDescription());

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }






    public void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_edit_profile) {

            Intent intent=new Intent(this, AccountEditInfoActivity.class);
            intent.putExtra("Username", username);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public void postOrder(View view){
        Intent intent=new Intent(this, AccountAddOrderActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);

    }

    public void showMore(View view){


    }


    public void returnToMe(View view){

    }

}


