package com.example.lab_2_library;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class FirstFragment extends Fragment {

    MyAdapter myAdapter;
    ArrayList<Book> books = new ArrayList<>();
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton createBook = getView().findViewById(R.id.create_book_button);
        createBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ManageActivity.class);
                i.putExtra("operation", "create");
                startActivityForResult(i, getActivity().RESULT_OK);
            }
        });


        registerForContextMenu(getView().findViewById(R.id.listViewBooks));

        ListView listViewBooks = getView().findViewById(R.id.listViewBooks);
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        //databaseHelper.onCreate(db);
        pullData();
        myAdapter = new MyAdapter(getActivity(), books);
        listViewBooks.setAdapter(myAdapter);
        MainActivity.myAdapter = this.myAdapter;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_book, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_item_edit_book:
            {
                Book book = (Book) myAdapter.getItem(info.position);
                Intent i = new Intent(getActivity(), ManageActivity.class);
                i.putExtra("operation", "edit");
                i.putExtra("adapterItemId", info.position);
                startActivityForResult(i, getActivity().RESULT_OK);
                return true;
            }
            case R.id.menu_item_delete_book:
            {
                Book book = (Book) myAdapter.getItem(info.position);
                deleteBook(info.position);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            myAdapter.notifyDataSetChanged();
            //new Handler().postDelayed(() -> refreshAdapter(), 1000);
    }

    public void refreshAdapter(){}

    private void fillListData() {
        for (int i = 1; i < 20; i++){
            books.add(new Book(books.size(), "Title " + i,
                    "Author " + i, 0, "", 0 ));
        }

    }

    private void deleteBook(int adapterIndex){
        int bookId = ((Book)myAdapter.getItem(adapterIndex)).id;
        boolean success = myAdapter.deleteItem(adapterIndex);
        if (success){
            myAdapter.notifyDataSetChanged();
            db.delete(DatabaseHelper.TABLE, "id = " + bookId, null);
            Toast.makeText(getActivity(),"Book Deleted...", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"Something went wrong...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            db.close();
    }

    public void pullData(){
        String query = "SELECT " + "* " + "FROM " + DatabaseHelper.TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0){
            Toast.makeText(getActivity(),"No data...", Toast.LENGTH_SHORT).show();
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
}