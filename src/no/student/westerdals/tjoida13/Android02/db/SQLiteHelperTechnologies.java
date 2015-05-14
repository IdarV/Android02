package no.student.westerdals.tjoida13.Android02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterTechnologies.TABLE_NAME_TECHNOLOGIES;

import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterTechnologies.WORD_ID;

import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterTechnologies.WORD;


/**
 * User: tmg
 * Happy coding!
 */
public class SQLiteHelperTechnologies extends SQLiteOpenHelper {

    private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE "
            + TABLE_NAME_TECHNOLOGIES + " (" + WORD_ID + " NUMBER PRIMARY KEY,"
            + WORD + " TEXT NOT NULL)";

    public SQLiteHelperTechnologies(Context context, String name, CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_DATABASE);
        Log.wtf("SQLiteHelperTechnologies:OnCreate", "OnCreate()");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ToDo Add upgrade logic
    }
}
