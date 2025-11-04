package com.example.mipt4;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spinner;
    private SwitchMaterial storageSwitch;
    private ArrayList<String> titlesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        spinner = findViewById(R.id.spinner);
        storageSwitch = findViewById(R.id.storage_switch);
        Button deleteButton = findViewById(R.id.deleteButton);

        titlesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titlesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        storageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storageSwitch.setText(isChecked ? "Storage: XML" : "Storage: JSON");
            loadNotes();
        });

        loadNotes(); // Initial load

        deleteButton.setOnClickListener(v -> {
            String selectedTitle = (String) spinner.getSelectedItem();
            if (selectedTitle != null) {
                if (storageSwitch.isChecked()) {
                    XmlNoteHelper.deleteNoteFromXml(this, selectedTitle);
                    Toast.makeText(DeleteNoteActivity.this, "Note deleted from XML!", Toast.LENGTH_SHORT).show();
                } else {
                    JsonNoteHelper.deleteNote(this, selectedTitle);
                    Toast.makeText(DeleteNoteActivity.this, "Note deleted from JSON!", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void loadNotes() {
        titlesList.clear();
        if (storageSwitch.isChecked()) {
            titlesList.addAll(XmlNoteHelper.getNoteTitlesFromXml(this));
        } else {
            titlesList.addAll(JsonNoteHelper.getNoteTitles(this));
        }

        if (titlesList.isEmpty()) {
            Toast.makeText(this, "No notes found", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }
}