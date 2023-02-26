package com.example.txon_04_aditya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;

public class NotesEditorActivity extends AppCompatActivity {
    EditText noteEditText;
    int noteId;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        ActionBar actionBar = getSupportActionBar();
        sharedPreferences=this.getSharedPreferences("com.example.txon_04_aditya", Context.MODE_PRIVATE);
        noteEditText = findViewById(R.id.note_EditText);

        Intent intent = getIntent();
        noteId =intent.getIntExtra("noteId",-1);
        if (noteId!=-1){
            noteEditText.setText(MainActivity.notes.get(noteId));
            actionBar.setTitle("Edit Note");
        }else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size()-1;
            actionBar.setTitle("Add Note");
        }
        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notes.set(noteId,String.valueOf(s));
                MainActivity.adapter.notifyDataSetChanged();
                HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes",noteSet).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.save_note){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return true;
        }
        return false;
    }
}