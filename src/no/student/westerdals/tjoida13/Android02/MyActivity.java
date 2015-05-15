package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private int score;
    private int totalRounds;
    private int round;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        initSqlLiteAdapter();
        initRound();
        initScore();
        initDataAsync();
        setOkButtonOnClick();
        initTextViews();
        initHighScoreButton();
    }

    public void initSqlLiteAdapter() {
        sqLiteAdapterTechnologies = new SQLiteAdapterTechnologies(context);
        sqLiteAdapterHighscore = new SQLiteAdapterHighscore(context);

    }

    public void initTextViews(){
        TextView header = (TextView) findViewById(R.id.textViewHeader);
        header.setText(getString(R.string.Round) + getString(R.string.space) + round + getString(R.string.spaceSlashSpace) + totalRounds);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String user = sharedPreferences.getString(getString(R.string.User), getString(R.string.defaultUserName));

        TextView textViewscore = (TextView) findViewById(R.id.textViewScore);
        textViewscore.setText(getString(R.string.Score) + getString(R.string.spaceColonSpace) + score + getString(R.string.spaceSlashSpace) + (round - 1));

        TextView userHeader = (TextView) findViewById(R.id.textViewUser);
        userHeader.setText(getString(R.string.User) + getString(R.string.spaceColonSpace) + user);
    }

    public void initRound(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        round = sharedPreferences.getInt(getString(R.string.Round), 0);
        round++;
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt(getString(R.string.Round), round);
        sharedPreferencesEditor.apply();
        totalRounds = sharedPreferences.getInt(getString(R.string.TotalRounds), 8);


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
        return randomCollection;
    }

    public void setTechnologiesToTextViews() {
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
            returnString += getString(R.string.space);
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
                        guessIntent.putExtra(getString(R.string.CorrectWord), pullCorrectWordFromList());
                        Collections.shuffle(randomArray);
                        guessIntent.putExtra(getString(R.string.RemainingWords), randomArray);
                        guessIntent.putExtra(getString(R.string.WrongWords), wrongWords);
                        guessIntent.putExtra(getString(R.string.Score), (score));
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
        score = getIntent().getIntExtra(getString(R.string.Score), 0);
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
