package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Idar Vassdal on 13.05.2015.
 */
public class GuessActivity extends Activity{
    private ArrayList<String> randomArray;
    private String correctWord;
    private String[] wrongWords;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess);
        getExtras();
        setNames();
    }

    public void getExtras(){
        Intent intent = getIntent();
        randomArray = intent.getStringArrayListExtra("RemainingWords");
        Collections.shuffle(randomArray);
        correctWord = intent.getStringExtra("CorrectWord");
        wrongWords = intent.getStringArrayExtra("WrongWords");
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
        Button guessOne = (Button) findViewById(R.id.guessOne);
        guessOne.setText(correctWord);
        Button guessTwo = (Button) findViewById(R.id.guessTwo);
        guessTwo.setText(wrongWords[0]);
        Button guessThree = (Button) findViewById(R.id.guessThree);
        guessThree.setText(wrongWords[1]);
    }
}
