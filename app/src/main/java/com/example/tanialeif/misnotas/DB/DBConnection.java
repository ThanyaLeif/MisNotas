package com.example.tanialeif.misnotas.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    private String CREATE_DB_SCRIPT = "create table note (" +
                    "_id integer primary key autoincrement," +
                    "_title text, _text text, _type text)";

    public DBConnection(Context context) {
        super(context, "db_mis_notas", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
