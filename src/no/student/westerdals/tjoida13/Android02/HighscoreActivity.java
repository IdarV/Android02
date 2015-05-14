package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Cyzla on 14.05.2015.
 */
public class HighscoreActivity extends Activity {
    private Context context;
    private ListView highscoreList;
    private SQLiteAdapterHighscore sqLiteAdapterHighscore;
    private ArrayList<String> myList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
        context = this;
        myList = new ArrayList<String>();
        highscoreList = (ListView) findViewById(R.id.highscoreListView);
        sqLiteAdapterHighscore = new SQLiteAdapterHighscore(context);
        sqLiteAdapterHighscore.open();
        Cursor cursor = sqLiteAdapterHighscore.readAll();

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("user"));
            String score = cursor.getString(cursor.getColumnIndex("score"));
            myList.add(name + " : " + score);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myList
        );
        sqLiteAdapterHighscore.close();
        highscoreList.setAdapter(arrayAdapter);


        highscoreList = (ListView) findViewById(R.id.highscoreListView);


        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MyActivity.class));
            }
        });
    }

}
