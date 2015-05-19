package no.student.westerdals.tjoida13.Android02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore.TABLE_NAME_HIGHSCORE;
import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore.SCORE;
import static no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore.USER;

public class SQLiteHelperHighscore extends SQLiteOpenHelper {
    private static final String SCRIPT_CREATE_DATABASE = /*"CREATE TABLE "
            + TABLE_NAME_TECHNOLOGIES + " (" + WORD_ID + " NUMBER PRIMARY KEY,"
            + WORD + " TEXT NOT NULL)";*/
            "CREATE TABLE "
                    + TABLE_NAME_HIGHSCORE+
                    "(" + USER + " TEXT NOT NULL,"
                    + SCORE + " BIGINT NOT NULL)";


    public SQLiteHelperHighscore(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ToDo Add upgrade logic
    }
}
