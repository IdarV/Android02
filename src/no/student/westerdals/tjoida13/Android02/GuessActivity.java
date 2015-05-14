package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class GuessActivity extends Activity{
    private ArrayList<String> randomArray;
    private String correctWord;
    private Context context;
    private ArrayList<String> wrongWords;
    private int score;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess);
        context = this;
        getExtras();
        setNames();
        initBackButton();
    }

    public void getExtras(){
        Intent intent = getIntent();
        randomArray = intent.getStringArrayListExtra("RemainingWords");
        Collections.shuffle(randomArray);
        correctWord = intent.getStringExtra("CorrectWord");
        wrongWords = new ArrayList<String>(Arrays.asList(intent.getStringArrayExtra("WrongWords")));
        score =intent.getIntExtra("Score", 0);
    }

    public void setNames() {
        initAnswerButtons();
        initTextViews();
    }

    public void initTextViews(){
        Log.v("MyActivity", "Updating textView names");
        ArrayList<TextView> textViews = new ArrayList<TextView>();
        // TODO: Fix loop
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

    public String addRandomSpacesToString(String word){
        Random random = new Random();
        int spaces = random.nextInt(45);
        String returnString = "";
        for(int i = 0; i < spaces; i++){
            returnString += " ";
        }
        return returnString += word;

    }

    public void initAnswerButtons(){
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
        for(String currentWord : allAnswers){
            if(currentWord.equals(correctWord)){
                answerButtons[i].setText(allAnswers.get(i));
                answerButtons[i].setOnClickListener(getSuccessClickListener());
            }
            else{
                answerButtons[i].setText(currentWord);
                answerButtons[i].setOnClickListener(getFailClickListener());
            }
            i++;
        }
    }

    public View.OnClickListener getSuccessClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MyActivity.class);
                intent.putExtra("Score", score);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener getFailClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HighscoreActivity.class);
                intent.putExtra("Score", score);
                startActivity(intent);
            }
        };
    }

    public void initBackButton(){
        Button backButton = (Button) findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guessIntent = new Intent(context, MyActivity.class);
                guessIntent.putExtra("score", score);
                startActivity(guessIntent);
            }
        });
    }
}
