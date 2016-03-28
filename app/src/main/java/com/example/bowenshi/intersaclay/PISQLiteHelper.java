/**
 * Created by bowenshi on 25/03/16.
 */
package com.example.bowenshi.intersaclay;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PISQLiteHelper extends SQLiteOpenHelper {

    private static PISQLiteHelper sInstance;

    // database version
    private static final int database_VERSION = 1;
    // database name
    private static final String database_NAME = "UserDB";
    private static final String table_USERS = "users";
    private static final String user_ID = "id";
    private static final String user_NAME = "name";
    private static final String user_SD = "sd";

    private static final String[] COLUMNS = { user_ID, user_NAME, user_SD };

    public static synchronized PISQLiteHelper getInstance(Context context) {

        // Use the application context
        if (sInstance == null) {
            sInstance = new PISQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public PISQLiteHelper(Context context){
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE users ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "sd TEXT )";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop books table if already exists
        db.execSQL("DROP TABLE IF EXISTS users");
        this.onCreate(db);
    }

    public void createUser(PersonalInformation user){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
//        values.put(user_ID, user.getId());
        values.put(user_NAME,user.getName());
        values.put(user_SD, user.getSelfDescription());

        db.insert(table_USERS, null, values);

        db.close();
    }

    public PersonalInformation readUser(String name){
        SQLiteDatabase db=this.getReadableDatabase();

//        Cursor cursor = db.query(table_USERS, // a. table
//                COLUMNS, " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        Cursor cursor =null;
        cursor=db.query(table_USERS, // a. table
                COLUMNS, user_NAME+"=?", new String[] { name }, null, null, null, null);
        if(cursor !=null){
            cursor.moveToFirst();
        }

        PersonalInformation user=new PersonalInformation();
        user.setId(Integer.parseInt(cursor.getString(0)));
        user.setName(cursor.getString(1));
        user.setSelfDescription(cursor.getString(2));

        return user;

    }

    public boolean hasUser(String name){
        boolean exist=true;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =null;
        cursor=db.query(table_USERS, // a. table
                COLUMNS, user_NAME+"=?", new String[] { name }, null, null, null, null);

        if(cursor.getCount() <=0){
            exist=false;
            cursor.close();
        }
        cursor.close();
        return exist;
    }

    public int updateUser(PersonalInformation user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(user_ID,user.getId());
        values.put(user_NAME,user.getName());
        values.put(user_SD, user.getSelfDescription());

        int i=db.update(table_USERS, values, user_ID+ " =?", new String[]{String.valueOf(user.getId())});

        db.close();
        return i;
    }

    public void deleteUser(PersonalInformation user){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(table_USERS, user_ID+ " =?", new String[]{String.valueOf(user.getId())});
        db.close();
    }


}
