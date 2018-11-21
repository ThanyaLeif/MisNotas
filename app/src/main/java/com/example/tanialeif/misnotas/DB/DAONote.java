package com.example.tanialeif.misnotas.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tanialeif.misnotas.Model.Note;

import java.util.ArrayList;

public class DAONote {

    private SQLiteDatabase db;

    public DAONote(Context context) {
        db = new DBConnection(context).getWritableDatabase();
    }

    public long insert(Note note) {
        ContentValues cv = new ContentValues();

        cv.put("_title", note.getTitle());
        cv.put("_text", note.getText());
        cv.put("_type", note.getType() == Note.TypeNote.Note ? "Note" : "Task");
        cv.put("_date", note.getDate());
        cv.put("_time", note.getTime());


        return db.insert("note", null, cv);
    }

    public long update(Note note) {
        ContentValues cv = new ContentValues();

        cv.put("_title", note.getTitle());
        cv.put("_text", note.getText());
        cv.put("_type", note.getType() == Note.TypeNote.Note ? "Note" : "Task");
        cv.put("_date", note.getDate());
        cv.put("_time", note.getTime());

        return db.update("note", cv, "_id=?",
                new String[] { String.valueOf(note.getId()) });
    }

    public long delete(long id) {
        return db.delete("note", "_id=?",
                new String[] { String.valueOf(id) });
    }

    public ArrayList<Note> getAll() {
        ArrayList<Note> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM note", new String[]{});

        while (cursor.moveToNext())
            list.add(fromCursor(cursor));

        return list;
    }

    public Note get(long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM note WHERE _id=? LIMIT 1",
                new String[]{ String.valueOf(id) });

        cursor.moveToNext();

        return fromCursor(cursor);
    }

    private Note fromCursor(Cursor cursor) {
        return new Note(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3).equals("Note")
                        ? Note.TypeNote.Note
                        : Note.TypeNote.Task,
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6) > 0,
                cursor.getString(7)
        );
    }
}

