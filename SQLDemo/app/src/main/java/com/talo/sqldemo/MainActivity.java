package com.talo.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String DB_FILE = "Data.db",  //資料庫名稱
                                DB_TABLE = "text";    //資料表名稱
    private SQLiteOpenHelper sqLiteOpenHelper;

    private Button btnAdd, btnSearch, btnList;
    private EditText edtName, edtSex, edtLocation;
    private TextView txtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //啟動APP檢查是否有資料表，若無則建立一個
        sqLiteOpenHelper = new SQLiteOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        //檢查是否存在資料表
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DB_TABLE + "'", null);
        if (cursor != null){
            if (cursor.getCount() == 0) //沒有資料表，需要建立一個
                sqLiteDatabase.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                        "_id INTTEGER PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "Sex TEXT," +
                        "Location TEXT);");
            cursor.close();
        }
        sqLiteDatabase.close();

        edtName = findViewById(R.id.edtName);
        edtSex = findViewById(R.id.edtSex);
        edtLocation = findViewById(R.id.edtLocation);

        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        btnList = findViewById(R.id.btnList);

        btnAdd.setOnClickListener(btnAddClick);
        btnSearch.setOnClickListener(btnSearchClick);
        btnList.setOnClickListener(btnListClick);

        txtList = findViewById(R.id.txtList);
    }
    private View.OnClickListener btnAddClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", edtName.getText().toString());    //加入資料
            contentValues.put("Sex", edtSex.getText().toString());
            contentValues.put("Location", edtLocation.getText().toString());
            sqLiteDatabase.insert(DB_TABLE, null, contentValues);
            sqLiteDatabase.close();
        }
    };
    private View.OnClickListener btnSearchClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            Cursor cursor = null;

            if (!edtName.getText().toString().equals("")){
                cursor = sqLiteDatabase.query(true, DB_TABLE, new String[]
                        {"name", "Sex", "Location"}, "name=" + "\"" +
                        edtName.getText().toString() +
                        "\"", null, null, null, null, null);
            }else if (!edtSex.getText().toString().equals("")){
                cursor = sqLiteDatabase.query(true, DB_TABLE, new String[]
                        {"name", "Sex", "Location"}, "Sex=" + "\"" +
                        edtSex.getText().toString() +
                        "\"", null, null, null, null, null);
            }else if (!edtLocation.getText().toString().equals("")){
                cursor = sqLiteDatabase.query(true, DB_TABLE, new String[]
                        {"name", "Sex", "Location"}, "Location=" + "\"" +
                        edtLocation.getText().toString() +
                        "\"", null, null, null, null, null);
            }
            if (cursor == null)
                return;
            if (cursor.getCount() == 0){
                txtList.setText("");
                Toast.makeText(MainActivity.this, "無資料", Toast.LENGTH_SHORT).show();
            }else{
                cursor.moveToFirst();
                txtList.setText(cursor.getString(0) +
                                cursor.getString(1) +
                                cursor.getString(2));
                while (cursor.moveToNext())
                    txtList.append("\n" +
                            cursor.getString(0) +
                            cursor.getString(1) +
                            cursor.getString(2));
            }
            sqLiteDatabase.close();
        }
    };
    private View.OnClickListener btnListClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.query(true, DB_TABLE, new String[]{"name", "Sex", "Location"},
                    null, null, null, null, null, null);
            if (cursor == null)
                return;
            if (cursor.getCount() == 0){
                txtList.setText("");
                Toast.makeText(MainActivity.this, "無資料", Toast.LENGTH_SHORT).show();
            }else{
                cursor.moveToFirst();
                txtList.setText(
                        cursor.getString(0) +
                        cursor.getString(1) +
                        cursor.getString(2));
                while (cursor.moveToNext())
                    txtList.append("\n" +
                            cursor.getString(0) +
                            cursor.getString(1) +
                            cursor.getString(2));
            }
            sqLiteDatabase.close();
        }
    };
}