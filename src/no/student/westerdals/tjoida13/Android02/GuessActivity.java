package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Idar Vassdal on 13.05.2015.
 */
public class GuessActivity extends Activity {
    private ArrayList<String> randomArray;
    private String correctWord;
    private Context context;
    private ArrayList<String> wrongWords;
    private int score;
    private int round;
    private int totalRounds;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess);
        context = this;
        initRound();
        getExtras();
        // DECOMMENT TO GET CORRECT WORD IN LOG
        Log.wtf("GuessActivity", "Correct word is " + correctWord);
        setNames();
        initBackButton();
    }

    public void getExtras() {
        Intent intent = getIntent();
        randomArray = intent.getStringArrayListExtra(getString(R.string.RemainingWords));
        Collections.shuffle(randomArray);
        correctWord = intent.getStringExtra(getString(R.string.CorrectWord));
        wrongWords = new ArrayList<String>(Arrays.asList(intent.getStringArrayExtra(getString(R.string.WrongWords))));
        score = intent.getIntExtra(getString(R.string.Score), 0);
    }

    public void setNames() {
        initAnswerButtons();
        initTextViews();
        initPlayerInfo();
    }

    public void initRound() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        round = sharedPreferences.getInt(getString(R.string.Round), 1);
        totalRounds = sharedPreferences.getInt(getString(R.string.TotalRounds), 8);
    }

    public void initTextViews() {
        ArrayList<TextView> textViews = new ArrayList<TextView>();
        // TODO: Fix loop (?)
        textViews.add((TextView) findViewById(R.id.textView2));
        textViews.add((TextView) findViewById(R.id.textView3));
        textViews.add((TextView) findViewById(R.id.textView4));
        textViews.add((TextView) findViewById(R.id.textView5));
        textViews.add((TextView) findViewById(R.id.textView6));
        textViews.add((TextView) findViewById(R.id.textView7));
        textViews.add((TextView) findViewById(R.id.textView8));

        for (int i = 0; i <= textViews.size() - 1; i++) {
            textViews.get(i).setText(addRandomSpacesToString(randomArray.get(i)));
        }
    }

    public void initPlayerInfo(){
        TextView header = (TextView) findViewById(R.id.textViewHeader);
        header.setText(getString(R.string.Round) + getString(R.string.space) + round + getString(R.string.spaceSlashSpace) + totalRounds);

        TextView textViewscore = (TextView) findViewById(R.id.textViewScore);
        textViewscore.setText(getString(R.string.Score) + getString(R.string.spaceColonSpace) + score + getString(R.string.spaceSlashSpace) + (round - 1));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String user = sharedPreferences.getString(getString(R.string.User), getString(R.string.defaultUserName));

        TextView userHeader = (TextView) findViewById(R.id.textViewUser);
        userHeader.setText(getString(R.string.User) + getString(R.string.spaceColonSpace) + user);
    }

    public String addRandomSpacesToString(String word) {
        Random random = new Random();
        int spaces = random.nextInt(45);
        String returnString = "";
        for (int i = 0; i < spaces; i++) {
            returnString += " ";
        }
        return returnString + word;

    }

    public void initAnswerButtons() {
        Button[] answerButtons = new Button[3];
        Button guessOne = (Button) findViewById(R.id.guessOne);
        Button guessTwo = (Button) findViewById(R.id.guessTwo);
        Button guessThree = (Button) findViewById(R.id.guessThree);

        answerButtons[0] = guessOne;
        answerButtons[1] = guessTwo;
        answerButtons[2] = guessThree;

        ArrayList<String> allAnswers = new ArrayList<String>();
        allAnswers.add(correctWord);
        allAnswers.addAll(wrongWords);
        Collections.shuffle(allAnswers);

        int i = 0;
        for (String currentWord : allAnswers) {
            if (currentWord.equals(correctWord)) {
                answerButtons[i].setText(allAnswers.get(i));
                answerButtons[i].setOnClickListener(getSuccessClickListener());
            } else {
                answerButtons[i].setText(currentWord);
                answerButtons[i].setOnClickListener(getFailClickListener());
            }
            i++;
        }
    }

    public View.OnClickListener getSuccessClickListener() {
        if(round == totalRounds){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getString(R.string.Correct), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, HighscoreActivity.class);
                    intent.putExtra(getString(R.string.Score), score + 1);
                    startActivity(intent);
                }
            };
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, getString(R.string.Correct), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MyActivity.class);
                intent.putExtra(getString(R.string.Score), score + 1);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener getFailClickListener() {
        if(round == totalRounds) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getString(R.string.Fail), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, HighscoreActivity.class);
                    intent.putExtra(getString(R.string.Score), score);
                    startActivity(intent);
                }
            };
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, getString(R.string.Fail), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MyActivity.class);
                intent.putExtra(getString(R.string.Score), score);
                startActivity(intent);
            }
        };
    }

    public void initBackButton() {
        Button backButton = (Button) findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guessIntent = new Intent(context, MyActivity.class);
                guessIntent.putExtra(getString(R.string.score), score);
                startActivity(guessIntent);
            }
        });
    }
}
