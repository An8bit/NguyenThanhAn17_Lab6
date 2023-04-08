package com.example.nguyenthanhan17_lab6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper {
    String DB_NAME = "Database.db";
    SQLiteDatabase db;
    Context context;

    public DBHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDB(){
        return context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null);
    }


    public void CopyDatabaseFromAssets(){
        File dbFile = context.getDatabasePath(DB_NAME);
        if(!dbFile.exists()){
            try {
                InputStream is= context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                while (is.read(buffer) > 0){
                    os.write(buffer);
                }

                os.flush();
                os.close();
                is.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<Info> getInfo(){
        ArrayList<Info> tmp = new ArrayList<>();
        db = openDB();
        String sql = "SELECT * FROM Info";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String fname = cursor.getString(1);
            String lname = cursor.getString(2);
            String image = cursor.getString(3);
            String phone = cursor.getString(4);
            String  email = cursor.getString(5);
            String birthday = cursor.getString(6);
            Info info = new Info(id,fname,lname,image,phone,email,birthday);
            tmp.add(info);
        }

        db.close();

        return tmp;
    }

    // insert into Contact()
    // values(?,?,?)

    public long insertInfo(Info info){
        db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fname", info.getFname());
        contentValues.put("lname", info.getLname());
        contentValues.put("image", info.getImage());
        contentValues.put("email",info.getMail());
        contentValues.put("phone", info.getPhone());
        contentValues.put("birthday", info.getBirthday());
        long tmp = db.insert("info","",contentValues);
        db.close();
        return tmp;
    }
    //update Contact
    //set  ?
    //where ?

   public long updateInfo(Info info){
        db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lname", info.getFname());
        contentValues.put("fname", info.getLname());
        contentValues.put("phone", info.getPhone());
        contentValues.put("email", info.getMail());
        contentValues.put("image", info.getImage());
        contentValues.put("birthday", info.getBirthday());
        long tmp = db.update("info", contentValues, "id="+ info.getId(), null);
        db.close();
        return tmp;
    }

    // delete from Contact
    //where ?

    public long deleteInfo(Info info){
        db = openDB();
        long tmp = db.delete("info", "id = " +info.getId(),null);
        db.close();
        return tmp;
    }

    //get info
    //Select *
    //From *
    //WHERE ID = ?


    //SEARCH
    //Select *
    //From *
    //WHERE like
    public Cursor searchForName(String keyword) {
        db=openDB();

        String[] projection = {
                "fname",
                "lname",
                "phone",
                "email"
        };

        String selection = "name" + " LIKE ?";
        String[] selectionArgs = {"%" + keyword + "%"};

        Cursor cursor = db.query(
                "info",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );db.close();

        return cursor;
    }

    //SORT
    //SELECT
    //FROM
    //ORDER BY

}
