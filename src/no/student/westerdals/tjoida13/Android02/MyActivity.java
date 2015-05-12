package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapter;
import no.student.westerdals.tjoida13.Android02.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MyActivity extends Activity {

    private Context context;
    public static SQLiteAdapter sqLiteAdapter;
    private ArrayList<String> myDbArrayList;
    private ArrayList<String> myLocalArrayList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        sqLiteAdapter = new SQLiteAdapter(context);
        initSqlLiteAdapter();
        readDatabaseAll();
        populateSqlLiteAdapter();
        getRandomCollection(5);
    }

    @Override
    public void onDestroy() {
        sqLiteAdapter.open();
        sqLiteAdapter.deleteAll();
    }

    public void initSqlLiteAdapter() {
        String[] technologiesArray = getResources().getStringArray(R.array.things);
        myLocalArrayList = new ArrayList<String>(Arrays.asList(technologiesArray));
        myDbArrayList = new ArrayList<String>();
    }

    public void populateSqlLiteAdapter(){
        sqLiteAdapter.open();
        for (String word : myLocalArrayList) {
            if(!myDbArrayList.contains(word)) {
                sqLiteAdapter.create(word);
            }
        }
        sqLiteAdapter.close();
    }

    public void readDatabaseAll() {
        sqLiteAdapter.open();

        Cursor cursor = sqLiteAdapter.readAll();
        if (cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                Log.wtf("MAIN FOUND IN DATABASE", word);
                myDbArrayList.add(word);
            }
        }
        sqLiteAdapter.deleteAll();
        sqLiteAdapter.close();
    }

    public ArrayList<String> getRandomCollection(int limit){
        ArrayList<String> randomCollection = new ArrayList<String>();
        Random random = new Random();
        if(limit > myDbArrayList.size()-1){
            return new ArrayList<String>(myDbArrayList);
        }

        while(randomCollection.size() < limit){
            String word = myDbArrayList.get(random.nextInt(myDbArrayList.size()-1));
            if(!randomCollection.contains(word)){
                randomCollection.add(word);
            }
        }

        for(String s : randomCollection){
            Log.wtf("RANDOMCOLLECTION", s);
        }

        return randomCollection;
    }
}
