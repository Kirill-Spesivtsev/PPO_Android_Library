package com.example.lab_2_library;

import android.app.LauncherActivity;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Comparator;
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

        ListView lv = getView().findViewById(R.id.listViewBooks);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout fl = getView().findViewById(R.id.frame_detailed);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fl.getLayoutParams();
                params.weight = 4f;


                Book curBook = (Book) myAdapter.getItem(position);
                TextView tvTitle = getView().findViewById(R.id.textViewTitle);
                TextView tvAuthor = getView().findViewById(R.id.textViewAuthor);
                TextView tvPages = getView().findViewById(R.id.textViewPagecount);
                TextView tvPublisher = getView().findViewById(R.id.textViewPublisher);
                TextView tvYear = getView().findViewById(R.id.textViewPublicationYear);
                tvTitle.setText(curBook.title);
                tvAuthor.setText(curBook.author);
                tvPages.setText("Страниц: " + Integer.toString(curBook.pageCount));
                tvPublisher.setText("Издатель: " + curBook.publisher);
                tvYear.setText("Год издания: " + Integer.toString(curBook.year));

                fl.setLayoutParams(params);

            }
        });

        Button b = getView().findViewById(R.id.hide_frame_detailed_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout fl = getView().findViewById(R.id.frame_detailed);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fl.getLayoutParams();
                params.weight = 0f;
                fl.setLayoutParams(params);
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
        refreshBooksList();
    }

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
            Toast.makeText(getActivity(),"Книга Удалена...", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"Что-то пошло не так... Повторите попытку", Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshBooksList(){
        pullData();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        pullData();
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
            Toast.makeText(getActivity(),"Пусто...", Toast.LENGTH_SHORT).show();
        }
        else{
            ArrayList<Book> bookList = new ArrayList<>();
            while (cursor.moveToNext()) {
                Book newBook = new Book();
                newBook.id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                newBook.title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                newBook.author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
                newBook.publisher = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PUBLISHER));
                newBook.year = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR));
                newBook.pageCount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAGECOUNT));
                bookList.add(newBook);
            }
            bookList.sort(Comparator.comparing(o -> o.title));
            books.clear();
            books.addAll(bookList);
        }
        cursor.close();
    }
}