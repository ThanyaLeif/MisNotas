package com.example.tanialeif.misnotas.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tanialeif.misnotas.Model.Memo;

import java.util.ArrayList;

public class DAOMemo {

    private SQLiteDatabase db;

    public DAOMemo(Context context) { db = new DBConnection(context).getWritableDatabase(); }

    public long insert(Memo memo){
        ContentValues cv = new ContentValues();

        cv.put("_date",memo.getDate());
        cv.put("_time",memo.getTime());
        cv.put("_idNote",memo.getIdNote());

        return db.insert("memo", null, cv);
    }

    public long update(Memo memo){
        ContentValues cv = new ContentValues();

        cv.put("_date",memo.getDate());
        cv.put("_time",memo.getTime());
        cv.put("_idNote",memo.getIdNote());

        return db.update("memo", cv, "_id=?",
                new String[] {String.valueOf(memo.getId())});
    }

    public long delete(long id){
        return db.delete("memo", "_id=?", new String[] {String.valueOf(id)});
    }

    public ArrayList<Memo> getAll(long idNote){
        ArrayList<Memo> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE _idNote=?",
                new String[] {String.valueOf(idNote)});

        while (cursor.moveToNext()){
            list.add(fromCursor(cursor));
        }

        return list;
    }

    public Memo get(long id){
        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE _id=? LIMIT 1",
                new String[] {String.valueOf(id)});

        cursor.moveToNext();

        return fromCursor(cursor);
    }

    private Memo fromCursor(Cursor cursor){
        return new Memo(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3)
        );
    }
}
