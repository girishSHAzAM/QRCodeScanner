package com.example.qrcodescanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME = "QRDatabase";
    public dbHelper(Context context){
        super(context,DATABASE_NAME,null,3);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE CustomerRecords(_id INTEGER Primary Key,EMP_ID INTEGER,NAME TEXT,TIME TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
