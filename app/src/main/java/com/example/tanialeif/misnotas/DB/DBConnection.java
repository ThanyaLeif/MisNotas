package com.example.tanialeif.misnotas.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    private String CREATE_DB_SCRIPT = "create table note (" +
                    "_id integer primary key autoincrement," +
                    "_title text, _text text, _type text, _date text, _time text,"+
                    " _checked boolean, _actualDate text)";

    private String CREATE_TABLE_MEMO ="create table memo (" +
            "_id integer primary key autoincrement," +
            "_idNote integer, _date text, _time text)";

    private String CREATE_TABLE_MEDIA ="create table media (" +
            "_id integer primary key autoincrement," +
            "_archivo text, _idImage text, _type text, _idNote integer)";

    public DBConnection(Context context) {
        super(context, "db_mis_notas", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_SCRIPT);
        db.execSQL(CREATE_TABLE_MEMO);
        db.execSQL(CREATE_TABLE_MEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS note");
        db.execSQL("DROP TABLE IF EXISTS memo");
        db.execSQL("DROP TABLE IF EXISTS media");

        onCreate(db);
    }
}
