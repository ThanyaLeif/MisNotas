package com.example.tanialeif.misnotas.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.example.tanialeif.misnotas.Model.Media;

import java.util.ArrayList;

public class DAOMedia {

    private SQLiteDatabase db;

    public DAOMedia(Context context) { db = new DBConnection(context).getWritableDatabase();}

    public long insert(Media media){
        ContentValues cv = new ContentValues();

        cv.put("_archivo",media.getArchivo());
        cv.put("_idImage",media.getIdImage());
        cv.put("_type",media.getType().toString());
        cv.put("_idNote",media.getIdNote());

        return db.insert("media",null,cv);
    }

    public long update(Media media){
        ContentValues cv = new ContentValues();

        cv.put("_archivo",media.getArchivo());
        cv.put("_idImage",media.getIdImage());
        cv.put("_type",media.getType().toString());
        cv.put("_idNote",media.getIdNote());

        return db.update("media",cv, "_id=?",
                new String[] {String.valueOf(media.getId())});
    }

    public long delete (long id){
        return db.delete("media", "_id=?",
                new String[] {String.valueOf(id)});
    }

    public ArrayList<Media> getAll(long idNote){
        ArrayList<Media> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM MEDIA WHERE _idNote=?",
                new String[]{String.valueOf(idNote)});

        while (cursor.moveToNext()){
            list.add(fromCursor(cursor));
        }

        return list;
    }

    public Media get(long id){
        Cursor cursor = db.rawQuery("SELECT * FROM media WHERE _id=? LIMIT 1",
                new String[] {String.valueOf(id)});

        cursor.moveToNext();

        return fromCursor(cursor);
    }

    private Media fromCursor(Cursor cursor){
        return new Media(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3).equals("Audio")
                        ? Media.TypeMedia.Audio
                        : cursor.getString(3).equals("Photo") ?
                        Media.TypeMedia.Photo
                        : cursor.getString(3).equals("Multi") ?
                        Media.TypeMedia.Multi :
                        Media.TypeMedia.Video,
                cursor.getLong(4)
        );
    }

}
