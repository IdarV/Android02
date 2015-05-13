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
    }

    public void getExtras(){
        Intent intent = getIntent();
        randomArray = intent.getStringArrayListExtra("RemainingWords");
        Collections.shuffle(randomArray);
        correctWord = intent.getStringExtra("CorrectWord");
        wrongWords = new ArrayList<String>(Arrays.asList(intent.getStringArrayExtra("WrongWords")));
    }

    public void setNames() {
        initAnswerButtons();
        initTextViews();
    }

    public void initTextViews(){
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

        for (int i = 0; i <= textViews.size() - 1; i++) {
            textViews.get(i).setText(randomArray.get(i));
        }
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
                answerButtons[i].setOnClickListener(getToastOnClickListener("Correct"));
            }
            else{
                answerButtons[i].setText(currentWord);
                answerButtons[i].setOnClickListener(getToastOnClickListener("Wrong"));
            }
            i++;
        }
    }

    public View.OnClickListener getToastOnClickListener(final String toastText){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
