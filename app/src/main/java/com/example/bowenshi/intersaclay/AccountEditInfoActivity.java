package com.example.bowenshi.intersaclay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AccountEditInfoActivity extends AppCompatActivity {

    ImageView accountEditUserImage;
    Uri userImageUri;
    String username, psw;
    EditText usernameEdit;
    EditText nameEdit;
    EditText adrsEdit;
    EditText emailEdit;
    EditText schoolEdit;
    EditText skillEdit;
    EditText sdEdit;
    Spinner sexSpinner;

    PISQLiteHelper db;

    PersonalInformation user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_edit_info);
        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
        accountEditUserImage=(ImageView) findViewById(R.id.account_edit_use_image);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        usernameEdit=(EditText) findViewById(R.id.account_edit_username);
        nameEdit=(EditText) findViewById(R.id.account_edit_realname);
        adrsEdit=(EditText) findViewById(R.id.account_edit_adrs);
        emailEdit=(EditText) findViewById(R.id.account_edit_email);
        schoolEdit=(EditText) findViewById(R.id.account_edit_school);
        skillEdit=(EditText) findViewById(R.id.account_edit_skills);
        sexSpinner=(Spinner) findViewById(R.id.account_edit_sexSpin);
        sdEdit=(EditText) findViewById(R.id.account_edit_self_description);

        db=PISQLiteHelper.getInstance(this.getApplicationContext());
        user=db.readUser(username);

        fillForms();

        Button accountChangeImageButton=(Button) findViewById(R.id.account_change_image_button);
        accountChangeImageButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button cancelButton=(Button) findViewById(R.id.account_edit_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMe(v);

            }
        });

        Button okButton=(Button) findViewById(R.id.account_edit_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update info in myData.txt
                accountEditUserImage.setImageResource(android.R.color.transparent);
                String info=new String();
                String name=nameEdit.getText().toString();
                String sex=sexSpinner.getSelectedItem().toString();
                String adrs=adrsEdit.getText().toString();
                String email=emailEdit.getText().toString();
                String school=schoolEdit.getText().toString();
                info+=username+"$"+name+"$"+sex+"$"+adrs+"$"+email+"$"+school+"$"+psw+"$";
                if(userImageUri!=null){
                    String stringUri=userImageUri.toString();
                    info+=stringUri;
                }
                if(!(skillEdit.getText().toString().equals(""))){
                    info+="$"+skillEdit.getText().toString();
                }
                info+="\n";
                PersonalFileOperator pfo=new PersonalFileOperator();

                String sd=sdEdit.getText().toString();
                user.setSelfDescription(sd);
                db.updateUser(user);

                if(pfo.updateItem(username,info)){

                    returnToMe(v);
                }
                else{
                    Toast toast=Toast.makeText(getApplicationContext(),"Cannot Update MyData!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    public void fillForms(){
        PersonalFileOperator pfo=new PersonalFileOperator();
        String[] userInfo=pfo.readItem(username);
        psw=userInfo[6];
        usernameEdit.setText(userInfo[0]);
        nameEdit.setText(userInfo[1]);
        adrsEdit.setText(userInfo[3]);
        emailEdit.setText(userInfo[4]);
        schoolEdit.setText(userInfo[5]);
        if(userInfo[8]!=null){
            skillEdit.setText(userInfo[8]);
        }

        //Set sd

        sdEdit.setText(user.getSelfDescription());

        //Set sexSpinner
        int spinnerPosition=getSpinnerIndex(sexSpinner, userInfo[2]);
        sexSpinner.setSelection(spinnerPosition);

        //Set User Photo
        if(userInfo[7]!=null){
            userImageUri=Uri.parse(userInfo[7]);
            Bitmap bitmap;
            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;
                bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(userImageUri),null,options);
                accountEditUserImage.setImageBitmap(bitmap);
//                accountMainUserImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    public int getSpinnerIndex(Spinner spinner, String str){
        int index=0;
        for(int i=0;i<spinner.getCount();++i){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
                index=i;
                break;
            }
        }
        return index;
    }


    public void returnToMe(View view){
        Intent intent=new Intent(view.getContext(), AccountActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);

    }

    //Load user image
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            userImageUri=data.getData();
            Bitmap bitmap;
            try{
                bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(userImageUri));
                accountEditUserImage.setImageBitmap(bitmap);
//                accountMainUserImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }



}
