package com.example.txon_04_aditya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView notesListView;
    TextView emptyTv;

    static List<String> notes;
    static ArrayAdapter adapter;

    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        notesListView= findViewById(R.id.notes_Listview);
        emptyTv = findViewById(R.id.emptyTV);

        notes= new ArrayList<>();
        HashSet<String> noteSet = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(noteSet.isEmpty() || noteSet == null){
            emptyTv.setVisibility(View.VISIBLE);
        }
        else{
            emptyTv.setVisibility(View.GONE);
            notes=new ArrayList<>(noteSet);
        }
        adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.custom_notes_row,R.id.notesTV,notes);
        notesListView.setAdapter(adapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NotesEditorActivity.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemToDelete = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to  delete this button ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(itemToDelete);
                        adapter.notifyDataSetChanged();

                        HashSet<String> noteSet = new HashSet<>(notes);
                        sharedPreferences.edit().putStringSet("notes",noteSet).apply();

                        if (noteSet.isEmpty() || noteSet == null){
                            emptyTv.setVisibility(View.VISIBLE);
                        }
                    }
                }).setNegativeButton("No",null).show();
                return ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_notes,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.add_note){
            startActivity(new Intent(getApplicationContext(),NotesEditorActivity.class));
            return  true;
        }
        return  false;
    }
}