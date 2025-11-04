package com.example.mipt4;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlNoteHelper {

    private static final String FILENAME = "notes.xml";
    private static final String TAG_NOTES = "notes";
    private static final String TAG_NOTE = "note";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";

    private static Map<String, String> readNotes(Context context) {
        Map<String, String> notes = new LinkedHashMap<>();
        try (FileInputStream fis = context.openFileInput(FILENAME)) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, "UTF-8");

            int eventType = parser.getEventType();
            String currentTag = null;
            String currentTitle = null;
            String currentContent = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if (TAG_NOTE.equals(currentTag)) {
                            currentTitle = null;
                            currentContent = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (currentTag != null && parser.getText() != null && !parser.getText().trim().isEmpty()) {
                            if (TAG_TITLE.equals(currentTag)) {
                                currentTitle = parser.getText();
                            } else if (TAG_CONTENT.equals(currentTag)) {
                                currentContent = parser.getText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (TAG_NOTE.equals(parser.getName())) {
                            if (currentTitle != null && currentContent != null) {
                                notes.put(currentTitle, currentContent);
                            }
                        }
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, which is fine.
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return notes;
    }

    private static void writeNotes(Context context, Map<String, String> notes) {
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, TAG_NOTES);
            for (Map.Entry<String, String> entry : notes.entrySet()) {
                serializer.startTag(null, TAG_NOTE);
                serializer.startTag(null, TAG_TITLE);
                serializer.text(entry.getKey());
                serializer.endTag(null, TAG_TITLE);
                serializer.startTag(null, TAG_CONTENT);
                serializer.text(entry.getValue());
                serializer.endTag(null, TAG_CONTENT);
                serializer.endTag(null, TAG_NOTE);
            }
            serializer.endTag(null, TAG_NOTES);
            serializer.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveNoteToXml(Context context, String title, String content) {
        Map<String, String> notes = readNotes(context);
        notes.put(title, content);
        writeNotes(context, notes);
    }

    public static void deleteNoteFromXml(Context context, String title) {
        Map<String, String> notes = readNotes(context);
        if (notes.containsKey(title)) {
            notes.remove(title);
            writeNotes(context, notes);
        }
    }

    public static List<String> getNoteTitlesFromXml(Context context) {
        return new ArrayList<>(readNotes(context).keySet());
    }

    public static String getNoteContentByTitleFromXml(Context context, String title) {
        return readNotes(context).get(title);
    }
}