package com.jimmy.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class JeekSQLiteHelper extends SQLiteOpenHelper {

    public JeekSQLiteHelper(Context context) {
        super(context, JeekDBConfig.DATABASE_NAME, null, JeekDBConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JeekDBConfig.CREATE_EVENT_SET_TABLE_SQL);
        db.execSQL(JeekDBConfig.CREATE_SCHEDULE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(JeekDBConfig.DROP_EVENT_SET_TABLE_SQL);
            db.execSQL(JeekDBConfig.DROP_SCHEDULE_TABLE_SQL);
            onCreate(db);
        }
    }
}
