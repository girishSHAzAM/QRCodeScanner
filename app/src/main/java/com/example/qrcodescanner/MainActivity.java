package com.example.qrcodescanner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ajts.androidmads.library.SQLiteToExcel;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int REQUEST_CODE_ACCEPT = 0;
    IntentResult result;
    SQLiteDatabase db;
    SQLiteOpenHelper sqLiteOpenHelper;
    IntentIntegrator qrScan;
    ProgressBar progressBar;
    String customerName,customerPhone;
    DateFormat dateFormat;
    Button buttonScan,storeData,ViewData,exportCSV;
    TextView textViewName, textViewEmpno;
    ContentValues customerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        buttonScan = findViewById(R.id.buttonScan);
       // storeData = findViewById(R.id.storeButton);
        textViewName = findViewById(R.id.textName);
        textViewEmpno = findViewById(R.id.textOrdered);
        qrScan = new IntentIntegrator(this);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
      /*  storeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }); */
        /*ViewData = (Button) findViewById(R.id.viewData);
        ViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SDCardActivity.class);
                startActivity(intent);
            }
        });*/
        exportCSV = findViewById(R.id.exportData);
        exportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ACCEPT);
                }
                else{
                    exportData();
                }
            }
        });
    }
    public void exportData(){
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            // Export SQLite DB as EXCEL FILE
            SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), dbHelper.DATABASE_NAME, directory_path);
            sqliteToExcel.exportAllTables("CustomerDatabase.csv", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(getApplicationContext(), "Successfully exported", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Export Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    customerName = obj.getString("name");
                    customerPhone = obj.getString("emp_id");
                    Date date = Calendar.getInstance().getTime();
                    //setting values to textviews
                    customerData = new ContentValues();
                    customerData.put("NAME",customerName);
                    customerData.put("EMP_ID",customerPhone);
                    customerData.put("TIME",dateFormat.format(date));
                    sqLiteOpenHelper = new dbHelper(MainActivity.this);
                    db = sqLiteOpenHelper.getWritableDatabase();
                    db.insert("CustomerRecords", null, customerData);
                    textViewName.setText(customerName);
                    textViewEmpno.setText(customerPhone);
                    Toast.makeText(MainActivity.this, "Records Inserted", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(MainActivity.this, "Records not inserted", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        textViewName.setText("Name");
        textViewEmpno.setText("Employee ID");
        super.onBackPressed();
    }
}
