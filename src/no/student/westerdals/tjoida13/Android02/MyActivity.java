package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MyActivity extends Activity {

    private Context context;
    public static SQLiteAdapter sqLiteAdapter;
    private ArrayList<String> myDbArrayList;
    private ArrayList<String> myLocalArrayList;
    private ArrayList<String> randomArray;

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
       /* readDatabaseAll();
        populateSqlLiteAdapter();
        setAndUpdateRandomStrings();*/
        initDataAsync();
        setButtonUpdate();
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

    public void initDataAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                readDatabaseAll();
                populateSqlLiteAdapter();
                setAndUpdateRandomStrings();
            }
        }).run();
    }

    public void populateSqlLiteAdapter() {
        //TODO: Instead of checkin all, just check if it exists (?)
        sqLiteAdapter.open();
        if(!sqLiteAdapter.checkIfTableExists()) {
            Log.wtf("TABLE NOT EXIST", "YAEHGJAL");
            for (String word : myLocalArrayList) {
                // if (!myDbArrayList.contains(word)) {
                sqLiteAdapter.create(word);
                //   }
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
        //sqLiteAdapter.deleteAll();
        sqLiteAdapter.close();
    }

    public ArrayList<String> getRandomCollection(int limit) {
        ArrayList<String> randomCollection = new ArrayList<String>();
        Random random = new Random();
        if (limit > myDbArrayList.size() - 1) {
            return new ArrayList<String>(myDbArrayList);
        }

        while (randomCollection.size() < limit) {
            String word = myDbArrayList.get(random.nextInt(myDbArrayList.size() - 1));
            if (!randomCollection.contains(word)) {
                randomCollection.add(word);
            }
        }
        for (String s : randomCollection) {
            Log.wtf("RANDOMCOLLECTION", s);
        }

        return randomCollection;
    }

    public void setNames() {
        ArrayList<TextView> textViews = new ArrayList<TextView>();
        // TODO: Fix loop
        textViews.add((TextView) findViewById(R.id.textView2));
        textViews.add((TextView) findViewById(R.id.textView3));
        textViews.add((TextView) findViewById(R.id.textView4));
        textViews.add((TextView) findViewById(R.id.textView5));
        textViews.add((TextView) findViewById(R.id.textView6));
        textViews.add((TextView) findViewById(R.id.textView7));
        textViews.add((TextView) findViewById(R.id.textView8));
        textViews.add((TextView) findViewById(R.id.textView9));

        for (int i = 0; i <= textViews.size() -1; i++) {
            TextView textView = textViews.get(i);
            textView.setText(randomArray.get(i));
        }
    }

    public void setButtonUpdate(){
        Button button = (Button) findViewById(R.id.OKbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       setAndUpdateRandomStrings();
                   }
               }).run();
            }
        });
    }

    public void setAndUpdateRandomStrings(){
        randomArray = getRandomCollection(9);
        setNames();

    }

}
