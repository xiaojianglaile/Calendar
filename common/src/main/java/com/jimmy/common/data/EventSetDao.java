package com.jimmy.common.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jimmy.common.bean.EventSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class EventSetDao {

    private JeekSQLiteHelper mHelper;

    private EventSetDao(Context context) {
        mHelper = new JeekSQLiteHelper(context);
    }

    public static EventSetDao getInstance(Context context) {
        return new EventSetDao(context);
    }

    public int addEventSet(EventSet eventSet) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JeekDBConfig.EVENT_SET_NAME, eventSet.getName());
        values.put(JeekDBConfig.EVENT_SET_COLOR, eventSet.getColor());
        values.put(JeekDBConfig.EVENT_SET_ICON, eventSet.getIcon());
        long row = db.insert(JeekDBConfig.EVENT_SET_TABLE_NAME, null, values);
        db.close();
        return row > 0 ? getLastEventSetId() : 0;
    }

    public Map<Integer, EventSet> getAllEventSetMap() {
        Map<Integer, EventSet> eventSets = new HashMap<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.EVENT_SET_TABLE_NAME, null, null, null, null, null, null);
        EventSet eventSet;
        while (cursor.moveToNext()) {
            eventSet = new EventSet();
            eventSet.setId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_ID)));
            eventSet.setName(cursor.getString(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_NAME)));
            eventSet.setColor(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_COLOR)));
            eventSet.setIcon(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_ICON)));
            eventSets.put(eventSet.getId(), eventSet);
        }
        cursor.close();
        db.close();
        mHelper.close();
        return eventSets;
    }

    public List<EventSet> getAllEventSet() {
        List<EventSet>  eventSets = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.EVENT_SET_TABLE_NAME, null, null, null, null, null, null);
        EventSet eventSet;
        while (cursor.moveToNext()) {
            eventSet = new EventSet();
            eventSet.setId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_ID)));
            eventSet.setName(cursor.getString(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_NAME)));
            eventSet.setColor(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_COLOR)));
            eventSet.setIcon(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_ICON)));
            eventSets.add(eventSet);
        }
        cursor.close();
        db.close();
        mHelper.close();
        return eventSets;
    }

    public boolean removeEventSet(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int row = db.delete(JeekDBConfig.EVENT_SET_TABLE_NAME, String.format("%s=?", JeekDBConfig.EVENT_SET_ID), new String[]{String.valueOf(id)});
        db.close();
        mHelper.close();
        return row != 0;
    }

    private int getLastEventSetId() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.EVENT_SET_TABLE_NAME, null, null, null, null, null, null);
        int id = 0;
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex(JeekDBConfig.EVENT_SET_ID));
        }
        cursor.close();
        db.close();
        mHelper.close();
        return id;
    }

}
