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
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterTechnologies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Idar Vassdal on 12.05.2015.
 */
public class MyActivity extends Activity {
    private Context context;
    public static SQLiteAdapterTechnologies sqLiteAdapterTechnologies;
    public static SQLiteAdapterHighscore sqLiteAdapterHighscore;
    private ArrayList<String> myDbArrayList;
    private ArrayList<String> randomArray;
    private String[] wrongWords;
    private int Score;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;

        initSqlLiteAdapter();
        initScore();
        initDataAsync();
        setOkButtonOnClick();
        TextView header = (TextView) findViewById(R.id.textViewHeader);
        header.setText(String.valueOf(Score));
        initHighScoreButton();
    }

    public void initSqlLiteAdapter() {

        sqLiteAdapterTechnologies = new SQLiteAdapterTechnologies(context);
        sqLiteAdapterHighscore = new SQLiteAdapterHighscore(context);
/*
        //TODO: DELETE THIS DEMO
        sqLiteAdapterHighscore.open();
        sqLiteAdapterHighscore.create(12, "IDAR");

        Cursor c = sqLiteAdapterHighscore.readAll();
        String name, score;
        while (c.moveToNext()){
            name = c.getString(c.getColumnIndex("user"));
            score = c.getString(c.getColumnIndex("score"));
            TextView header = (TextView) findViewById(R.id.textViewHeader);
            header.setText(name + ": " + score);
        }
        sqLiteAdapterHighscore.close();*/

    }

    public void initDataAsync() {
        myDbArrayList = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                populateTechnologies();
                readAllFromDatabase();
                setAndUpdateRandomStrings();
            }
        }).run();
    }

    public void populateTechnologies() {
        sqLiteAdapterTechnologies.open();

        if (!sqLiteAdapterTechnologies.hasRows()) {
            Log.wtf("MyActivity:populateTechnologies()", "sqlLiteAdapter doesn't have rows. Filling.");
            String[] technologiesArray = getResources().getStringArray(R.array.things);

            for (String word : technologiesArray) {
                sqLiteAdapterTechnologies.create(word);
            }
        }
        sqLiteAdapterTechnologies.close();
    }

    public void readAllFromDatabase() {
        sqLiteAdapterTechnologies.open();
        Cursor cursor = sqLiteAdapterTechnologies.readAll();

        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex(getString(R.string.word)));
            myDbArrayList.add(word);
        }
        sqLiteAdapterTechnologies.close();
    }

    public ArrayList<String> getRandomCollectionOfTechnologies(int limit) {
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
        Log.wtf("MyActivity():getRandomCollectionOfTechnologies", "Wrong words are " + wrongWords[0] + " and " + wrongWords[1]);

        return randomCollection;
    }

    public void setTechnologiesToTextViews() {
        Log.v("MyActivity", "Updating textView technologies");
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
            textViews.get(i).setText(addRandomSpacesToString(randomArray.get(i)));
        }
    }

    public String addRandomSpacesToString(String word){
        Random random = new Random();
        int spaces = random.nextInt(40);
        String returnString = "";
        for(int i = 0; i < spaces; i++){
            returnString += " ";
        }
        return returnString += word;

    }

    public void setOkButtonOnClick() {
        Button button = (Button) findViewById(R.id.OKbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent guessIntent = new Intent(context, GuessActivity.class);
                        guessIntent.putExtra("CorrectWord", pullCorrectWordFromList());
                        Collections.shuffle(randomArray);
                        guessIntent.putExtra("RemainingWords", randomArray);
                        guessIntent.putExtra("WrongWords", wrongWords);
                        guessIntent.putExtra("Score", (Score + 1));
                        startActivity(guessIntent);
                    }
                }).run();
            }
        });
    }

    public void setAndUpdateRandomStrings() {
        randomArray = getRandomCollectionOfTechnologies(8);
        setTechnologiesToTextViews();
    }

    public String pullCorrectWordFromList() {
        Random random = new Random();
        int randomIndex = random.nextInt(randomArray.size()-1);
        String correctWord = randomArray.get(randomIndex);
        randomArray.remove(randomIndex);
        return correctWord;
    }

    public void initScore(){
        Score = getIntent().getIntExtra("Score", 0);
        Log.wtf("initScore", "Score is " + Score);
    }

    public void initHighScoreButton(){
        Button highscoreButton = (Button) findViewById(R.id.highscoreButton);
        highscoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HighscoreActivity.class));
            }
        });
    }

}
