package no.student.westerdals.tjoida13.Android02.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteAdapterHighscore {
    public static final String DATABASE_NAME = "Highscores";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_HIGHSCORE = "highscore";

    public static final String SCORE = "score";
    public static final String USER = "user";


    public static final String PREFERENCES_DB = "PreferencesDb";

    private SQLiteHelperHighscore sqLiteHelperHighscore;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    private SharedPreferences preferences;

    public SQLiteAdapterHighscore(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCES_DB, Activity.MODE_APPEND);
    }

    public SQLiteAdapterHighscore open() {
        sqLiteHelperHighscore = new SQLiteHelperHighscore(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelperHighscore.getWritableDatabase(); //Should be in a background thread, i.e. Async
        return this;
    }

    public void create(int user, String score) {
        ContentValues values = new ContentValues();
        values.put(SCORE, score);
        values.put(USER, user);

        sqLiteDatabase.insert(TABLE_NAME_HIGHSCORE, null, values);
        SharedPreferences.Editor editor = preferences.edit();
        editor.commit();
    }

    public Cursor readAllSortedDesc() {
        return sqLiteDatabase.rawQuery("SELECT " + SCORE + ", " + USER + " FROM " + TABLE_NAME_HIGHSCORE + " ORDER BY " + USER + " DESC LIMIT 7", null);
    }

    public void deleteAll() {
        sqLiteDatabase.delete(TABLE_NAME_HIGHSCORE, null, null);
    }


    public void close() {
        sqLiteDatabase.close();
    }


}
