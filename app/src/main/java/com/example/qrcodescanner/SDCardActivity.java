package com.example.qrcodescanner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SDCardActivity extends AppCompatActivity {
    Cursor cursor;
    SearchView search;
List<String> nameParameter,nameSearchParameter,dateParameter,dateSearchParameter,phoneParameter,phoneSearchParameter;
List<Integer> idParameter,idSearchParameter;
RecyclerView r1;
R_Adapter rAdapter,SearchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard);
        r1 = findViewById(R.id.recyclerView);
        try {
            SQLiteOpenHelper sqlHelper = new dbHelper(SDCardActivity.this);
            SQLiteDatabase db = sqlHelper.getWritableDatabase();
            nameParameter= new ArrayList<>();
            phoneParameter = new ArrayList<>();
            idParameter = new ArrayList<>();
            dateParameter = new ArrayList<>();
            cursor = db.query("CustomerRecords", new String[]{"_id","NAME","PHONE","TIME"}, null, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                do {
                    idParameter.add(cursor.getInt(0));
                    nameParameter.add(cursor.getString(1));
                    phoneParameter.add(cursor.getString(2));
                    dateParameter.add(cursor.getString(3));
                }while(cursor.moveToNext());
            }

        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT).show();
        }
        search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    nameSearchParameter= new ArrayList<>();
                    phoneSearchParameter = new ArrayList<>();
                    idSearchParameter = new ArrayList<>();
                    dateSearchParameter = new ArrayList<>();
                    SQLiteOpenHelper sqlHelper = new dbHelper(SDCardActivity.this);
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                cursor = db.rawQuery("SELECT * FROM CustomerRecords WHERE PHONE MATCH '?' OR NAME MATCH '?' OR TIME MATCH '?'",new String[]{query,query,query});
                if(cursor != null && cursor.moveToFirst()){
                    do {
                        idSearchParameter.add(cursor.getInt(0));
                        nameSearchParameter.add(cursor.getString(1));
                        phoneSearchParameter.add(cursor.getString(2));
                        dateSearchParameter.add(cursor.getString(3));
                    }while(cursor.moveToNext());
                }}
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(SDCardActivity.this,"Database Unavailable",Toast.LENGTH_SHORT).show();
                }
                SearchAdapter = new R_Adapter(SDCardActivity.this,idSearchParameter,nameSearchParameter,phoneSearchParameter,dateSearchParameter);
                r1.setAdapter(SearchAdapter);
                r1.setLayoutManager(new LinearLayoutManager(SDCardActivity.this));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        rAdapter = new R_Adapter(this,idParameter,nameParameter,phoneParameter,dateParameter);
        r1.setAdapter(rAdapter);
        r1.setLayoutManager(new LinearLayoutManager(this));

    }
}
