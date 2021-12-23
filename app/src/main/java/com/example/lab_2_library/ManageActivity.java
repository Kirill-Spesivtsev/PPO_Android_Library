package com.example.lab_2_library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ManageActivity extends AppCompatActivity {

    String operation = "";
    int adapterItemId;
    int dbItemId;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseActivity();
            }
        });

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDataToDB();
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            operation = extras.getString("operation");
            if (operation.equals("edit")){
                adapterItemId = extras.getInt("adapterItemId");
                FillTextFields();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void CloseActivity(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    void SaveDataToDB(){
        if (operation.equals("edit")){
            EditBook();
        }
        else{
            CreateNewBook();
        }
    }

    void CreateNewBook(){
        TextInputLayout tilTitle = findViewById(R.id.textInputTitle);
        TextInputLayout tilAuthor = findViewById(R.id.textInputAuthor);
        TextInputLayout tilPages = findViewById(R.id.textInputPagecount);
        TextInputLayout tilPublisher = findViewById(R.id.textInputPublisher);
        TextInputLayout tilYear = findViewById(R.id.textInputYear);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TITLE, tilTitle.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_AUTHOR, tilAuthor.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_PAGECOUNT, Integer.parseInt(tilPages.getEditText().getText().toString()));
        cv.put(DatabaseHelper.COLUMN_PUBLISHER, tilPublisher.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(tilYear.getEditText().getText().toString()));

        long res = db.insert(DatabaseHelper.TABLE, null, cv);
        if (res != -1){
            Toast.makeText(this,"Book Created...", Toast.LENGTH_SHORT).show();
            MainActivity.myAdapter.notifyDataSetChanged();
            CloseActivity();
        }
        else{
            Toast.makeText(this,"Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    void EditBook(){
        TextInputLayout tilTitle = findViewById(R.id.textInputTitle);
        TextInputLayout tilAuthor = findViewById(R.id.textInputAuthor);
        TextInputLayout tilPages = findViewById(R.id.textInputPagecount);
        TextInputLayout tilPublisher = findViewById(R.id.textInputPublisher);
        TextInputLayout tilYear = findViewById(R.id.textInputYear);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TITLE, tilTitle.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_AUTHOR, tilAuthor.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_PAGECOUNT, Integer.parseInt(tilPages.getEditText().getText().toString()));
        cv.put(DatabaseHelper.COLUMN_PUBLISHER, tilPublisher.getEditText().getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(tilYear.getEditText().getText().toString()));

        int res = db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + " = " + dbItemId, null);
        if (res != 0){
            Toast.makeText(this,"Book Edited...", Toast.LENGTH_SHORT).show();
            MainActivity.myAdapter.notifyDataSetChanged();
            CloseActivity();
        }
        else{
            Toast.makeText(this,"Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }


    }

    void FillTextFields(){
        Book book = (Book) MainActivity.myAdapter.getItem(adapterItemId);
        TextInputLayout tilTitle = findViewById(R.id.textInputTitle);
        tilTitle.getEditText().setText(book.title);
        TextInputLayout tilAuthor = findViewById(R.id.textInputAuthor);
        tilAuthor.getEditText().setText(book.author);
        TextInputLayout tilPages = findViewById(R.id.textInputPagecount);
        tilPages.getEditText().setText(Integer.toString(book.pageCount));
        TextInputLayout tilPublisher = findViewById(R.id.textInputPublisher);
        tilPublisher.getEditText().setText(book.publisher);
        TextInputLayout tilYear = findViewById(R.id.textInputYear);
        tilYear.getEditText().setText(Integer.toString(book.year));
        dbItemId = book.id;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView tv = findViewById(R.id.textView_action);
        if (this.operation == "edit"){
            tv.setText("Edit Book");
        }
        else{
            tv.setText("Add new Book");
        }

    }
}