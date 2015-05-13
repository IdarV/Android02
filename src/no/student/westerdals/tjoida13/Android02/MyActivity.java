package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapter;

import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends Activity {
    private Context context;
    public static SQLiteAdapter sqLiteAdapter;
    private ArrayList<String> myDbArrayList;
    private ArrayList<String> randomArray;
    private String[] wrongWords;
    private String correctWord;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;

        initSqlLiteAdapter();
        initDataAsync();
        setOkButtonOnClick();
    }

    public void initSqlLiteAdapter() {
        sqLiteAdapter = new SQLiteAdapter(context);
    }

    public void initDataAsync() {
        myDbArrayList = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                populateSqlLiteAdapter();
                readAllFromDatabase();
                setAndUpdateRandomStrings();
            }
        }).run();
    }

    public void populateSqlLiteAdapter() {
        sqLiteAdapter.open();

        if (!sqLiteAdapter.hasRows()) {
            Log.wtf("MyActivity:populateSqlLiteAdapter()", "sqlLiteAdapter doesn't have rows. Filling.");
            String[] technologiesArray = getResources().getStringArray(R.array.things);

            for (String word : technologiesArray) {
                sqLiteAdapter.create(word);
            }
        }
        sqLiteAdapter.close();
    }

    public void readAllFromDatabase() {
        sqLiteAdapter.open();
        Cursor cursor = sqLiteAdapter.readAll();

        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex(getString(R.string.word)));
            myDbArrayList.add(word);
        }
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
        wrongWords = new String[2];
        for(int i = 0; i < 2; ){
            String word = myDbArrayList.get(random.nextInt(myDbArrayList.size() - 1));
            if (!randomCollection.contains(word)) {
                wrongWords[i] = word;
                i++;
            }
        }
        Log.wtf("MyActivity():getRandomCollection", "Wrong words are " + wrongWords[0] + " and " + wrongWords[1]);

        return randomCollection;
    }

    public void setNames() {
        Log.v("MyActivity:setNames()", "Updating textView names");
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

        for (int i = 0; i <= textViews.size() - 1; i++) {
            textViews.get(i).setText(randomArray.get(i));
        }
    }

    public void setOkButtonOnClick() {
        Button button = (Button) findViewById(R.id.OKbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //setAndUpdateRandomStrings();
                        Intent guessIntent = new Intent(context, GuessActivity.class);
                        guessIntent.putExtra("CorrectWord", pullCorrectWordFromList());
                        guessIntent.putExtra("WrongWords", wrongWords);
                        guessIntent.putExtra("RemainingWords", randomArray);
                        startActivity(guessIntent);
                    }
                }).run();
            }
        });
    }

    public void setAndUpdateRandomStrings() {
        randomArray = getRandomCollection(8);
        setNames();

    }

    public String pullCorrectWordFromList(){
        Random random = new Random();
        int randomIndex = random.nextInt(randomArray.size()-1);
        String correctWord = randomArray.get(randomIndex);
        randomArray.remove(randomIndex);
        return correctWord;
    }

}
