package com.rssin.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote) {
            Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        SharedPreferences sP=getApplicationContext().getSharedPreferences("com.rssin.notes", Context.MODE_PRIVATE);
        HashSet<String> set=(HashSet)sP.getStringSet("notes",null);
        if(set==null){
            notes.add("Example Text");
        }else{
        notes=new ArrayList(set);
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });



        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setTitle("Delete")
                        .setMessage("Do you want to delelte?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sP=getApplicationContext().getSharedPreferences("com.rssin.notes", Context.MODE_PRIVATE);
                                HashSet<String> set=new HashSet(MainActivity.notes);
                                sP.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show() ;
                return true;
            }

        });

    }
}
