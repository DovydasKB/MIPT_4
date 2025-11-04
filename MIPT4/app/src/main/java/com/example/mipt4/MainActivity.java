package com.example.mipt4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_DELETE_NOTE = 2;

    private List<String> notesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        notesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(adapter);

        loadNotes();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTitle = notesList.get(position);
            String content = getNoteContentByTitle(selectedTitle);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(selectedTitle)
                    .setMessage(content)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });

        Button addNoteButton = findViewById(R.id.button_add_note);
        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
        });

        Button deleteNoteButton = findViewById(R.id.button_delete_note);
        deleteNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeleteNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DELETE_NOTE);
        });
    }

    private void loadNotes() {
        notesList.clear();
        notesList.addAll(JsonNoteHelper.getNoteTitles(this));
        notesList.addAll(XmlNoteHelper.getNoteTitlesFromXml(this));
        adapter.notifyDataSetChanged();
    }

    private String getNoteContentByTitle(String title) {
        String content = JsonNoteHelper.getNoteContent(this, title);
        if (content != null) {
            return content;
        }
        return XmlNoteHelper.getNoteContentByTitleFromXml(this, title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ADD_NOTE || requestCode == REQUEST_CODE_DELETE_NOTE) && resultCode == RESULT_OK) {
            loadNotes();
        }
    }
}