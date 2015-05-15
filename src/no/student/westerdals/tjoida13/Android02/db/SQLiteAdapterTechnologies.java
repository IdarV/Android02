package no.student.westerdals.tjoida13.Android02.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteAdapterTechnologies {

    public static final String DATABASE_NAME = "TechNames";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_TECHNOLOGIES="Technologies";
    public static final String WORD = "word";
    public static final String PREF_DB = "PreferencesDb";

    private SQLiteHelperTechnologies sqLiteHelperTechnologies;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    private SharedPreferences preferences;


    public SQLiteAdapterTechnologies(Context context) {
        this.context = context;

        preferences = context.getSharedPreferences(PREF_DB, Activity.MODE_APPEND);

    }

    public SQLiteAdapterTechnologies open () {
        sqLiteHelperTechnologies = new SQLiteHelperTechnologies(context,DATABASE_NAME,null,DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelperTechnologies.getWritableDatabase();
        return this;
    }

    public void create(String word) {
        ContentValues values = new ContentValues();
        values.put(WORD, word);

        sqLiteDatabase.insert(TABLE_NAME_TECHNOLOGIES,null,values);
        SharedPreferences.Editor editor = preferences.edit();
        editor.commit();
    }

    public boolean hasRows(){
        int rowcount = sqLiteDatabase.rawQuery("SELECT word FROM " + TABLE_NAME_TECHNOLOGIES, null).getCount();
        return  rowcount > 1;
    }

    public Cursor readAll() {
        //String[] columns = new String[]{SCORE_ID, SCORE};
        return sqLiteDatabase.rawQuery("SELECT " + WORD + " FROM " + TABLE_NAME_TECHNOLOGIES, null);
        //return sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        //(Table_name, columns, where, where args, group by, order by, having)
    }

    public void close() {
        sqLiteDatabase.close();
    }
}
