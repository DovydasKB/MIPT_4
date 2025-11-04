package com.example.mipt4;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonNoteHelper {

    private static final String FILENAME = "notes.json";

    private static JSONArray readNotesArray(Context context) {
        try (FileInputStream fis = context.openFileInput(FILENAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }

            String jsonString = stringBuilder.toString();
            if (!jsonString.isEmpty()) {
                return new JSONArray(jsonString);
            }

        } catch (FileNotFoundException e) {
            // nothing yet :(
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private static void writeNotesArray(Context context, JSONArray notes) {
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            fos.write(notes.toString(2).getBytes(StandardCharsets.UTF_8));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveNote(Context context, String title, String content) {
        JSONArray notes = readNotesArray(context);
        JSONObject newNote = new JSONObject();
        try {
            newNote.put("title", title);
            newNote.put("content", content);
            notes.put(newNote);
            writeNotesArray(context, notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNote(Context context, String title) {
        JSONArray oldArray = readNotesArray(context);
        JSONArray newArray = new JSONArray();
        try {
            for (int i = 0; i < oldArray.length(); i++) {
                JSONObject note = oldArray.getJSONObject(i);
                if (!note.getString("title").equals(title)) {
                    newArray.put(note);
                }
            }
            writeNotesArray(context, newArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getNoteTitles(Context context) {
        JSONArray notes = readNotesArray(context);
        List<String> titles = new ArrayList<>();
        try {
            for (int i = 0; i < notes.length(); i++) {
                titles.add(notes.getJSONObject(i).getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titles;
    }

    public static String getNoteContent(Context context, String title) {
        JSONArray notes = readNotesArray(context);
        try {
            for (int i = 0; i < notes.length(); i++) {
                JSONObject note = notes.getJSONObject(i);
                if (note.getString("title").equals(title)) {
                    return note.getString("content");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}