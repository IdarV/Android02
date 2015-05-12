package no.student.westerdals.tjoida13.Android02.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * User: tmg
 * Happy coding!
 */
public class SQLiteAdapter {

    public static final String DATABASE_NAME = "TechNames";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="Technologies";

    public static final String WORD_ID = "_id";
    public static final String WORD = "word";

    public static final String PREFERENCES_DB = "PreferencesDb";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    private SharedPreferences preferences;

    private long id = 1;

    public SQLiteAdapter (Context context) {
        this.context = context;

        preferences = context.getSharedPreferences(PREFERENCES_DB, Activity.MODE_APPEND);
        if(preferences != null){
            id = preferences.getLong("nextId", 1);
        }

    }

    public SQLiteAdapter open () {
        sqLiteHelper = new SQLiteHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase(); //Should be in a background thread, i.e. Async
        return this;
    }

    public long create(String word) {
        ContentValues values = new ContentValues();
        values.put(WORD_ID, id);
        values.put(WORD, word);

        sqLiteDatabase.insert(TABLE_NAME,null,values);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("nextId",id+1);
        editor.commit();

        return id++;


    }

    public Cursor readAll() {
        String[] columns = new String[]{WORD_ID, WORD};
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        //return sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        //(Table_name, columns, where, where args, group by, order by, having)
    }

    /*public boolean update(Long personId, Person person) {
        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_AGE, person.getAge());

        String whereClause = PERSON_ID + " = ?";
        String[] whereArgs = new String[]{personId.toString()};

        int numberOfRowsUpdated = sqLiteDatabase.update(TABLE_NAME, values, whereClause, whereArgs);

        return (numberOfRowsUpdated == 1);
    }*/

    /*public boolean delete(Long personId) {
        String whereClause = PERSON_ID + " = ?";
        String[] whereArgs = new String[]{personId.toString()};
        int numberOfRowsUpdated = sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);

        return (numberOfRowsUpdated == 1);
    }*/

    public void deleteAll(){
       sqLiteDatabase.delete(TABLE_NAME, null, null);
        Log.wtf("SQLiteAdapter", "ZOMFG DELETED ALL");

    }


    public void close() {
        sqLiteDatabase.close();
    }



}
