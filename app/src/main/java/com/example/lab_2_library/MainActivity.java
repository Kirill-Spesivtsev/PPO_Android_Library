package com.example.lab_2_library;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyAdapter myAdapter;
    ArrayList<Book> books = new ArrayList<>();
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listViewBooks = findViewById(R.id.listViewBooks);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        //databaseHelper.onCreate(db);
        //fillListData();
        pullData();

        myAdapter = new MyAdapter(this, books);
        listViewBooks.setAdapter(myAdapter);
    }

    private void fillListData() {
        for (int i = 1; i < 20; i++){
            books.add(new Book(books.size(), "Title " + i,
                    "Author " + i, 0, "", 0 ));
        }

    }

    public void pullData(){
        String query = "SELECT " + "* " + "FROM " + DatabaseHelper.TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0){
            Toast.makeText(MainActivity.this,"No data...", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()) {
                Book newBook = new Book();
                newBook.id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                newBook.title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                newBook.author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                newBook.publisher = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PUBLISHER));
                newBook.year = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR));
                newBook.pageCount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAGECOUNT));
                books.add(newBook);
            }
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}