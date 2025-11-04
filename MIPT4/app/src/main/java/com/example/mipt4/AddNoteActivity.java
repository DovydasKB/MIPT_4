package com.example.mipt4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private RadioGroup storageRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        storageRadioGroup = findViewById(R.id.storage_radio_group);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = storageRadioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.json_radio_button) {
                JsonNoteHelper.saveNote(AddNoteActivity.this, title, content);
                Toast.makeText(AddNoteActivity.this, "Note saved to JSON file!", Toast.LENGTH_SHORT).show();
            } else if (selectedId == R.id.xml_radio_button) {
                XmlNoteHelper.saveNoteToXml(AddNoteActivity.this, title, content);
                Toast.makeText(AddNoteActivity.this, "Note saved to XML file!", Toast.LENGTH_SHORT).show();
            }

            Intent result = new Intent();
            setResult(RESULT_OK, result);
            finish();
        });
    }
}