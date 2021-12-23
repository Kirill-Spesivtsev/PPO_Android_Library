package com.example.lab_2_library;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Book> objects;

    MyAdapter(Context con, ArrayList<Book> obj) {
        context = con;
        objects = obj;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int index) {
        return objects.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
        {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        Book item = (Book) getItem(index);

        ((TextView) view.findViewById(R.id.textViewTitle)).setText(item.title);//set to fragment
        ((TextView) view.findViewById(R.id.textViewAuthor)).setText(item.author);

        return view;
    }

    public boolean deleteItem(int index){
        try
        {
            objects.remove(index);
            return true;
        }catch (IndexOutOfBoundsException e){
            return  false;
        }
    }

    public void Clear(){
        objects.clear();
    }
}
