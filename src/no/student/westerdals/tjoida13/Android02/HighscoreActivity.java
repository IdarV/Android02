package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import no.student.westerdals.tjoida13.Android02.db.SQLiteAdapterHighscore;

import java.util.ArrayList;

/**
 * Created by Cyzla on 14.05.2015.
 */
public class HighscoreActivity extends Activity {
    private Context context;
    private ListView highscoreList;
    private SQLiteAdapterHighscore sqLiteAdapterHighscore;
    private ArrayList<String> nameList;
    private ArrayList<Integer> scoreList;
    private String currentUser;
    private int currentScore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
        context = this;
        initialize();
        getExtras();
        readFromDatabase();
        updateHighScoreIfNew();
        addDataToListView();
        updateDatabase();
        setBackButtonAction();

    }

    public void initialize() {
        nameList = new ArrayList<String>();
        scoreList = new ArrayList<Integer>();
        highscoreList = (ListView) findViewById(R.id.highscoreListView);
        sqLiteAdapterHighscore = new SQLiteAdapterHighscore(context);
    }

    public ArrayAdapter<String> getArrayAdapter() {
        ArrayList<String> scoreList = createScoreArrayList();
        return new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                scoreList
        );
    }

    public ArrayList<String> createScoreArrayList() {
        ArrayList<String> highScoreArrayList = new ArrayList<String>();
        for (int i = 0; i < Math.max(nameList.size(), scoreList.size()) - 1; i++) {
            if (scoreList.get(i) > 0) {
                highScoreArrayList.add(nameList.get(i) + getString(R.string.spaceColonSpace) + scoreList.get(i));
            }
        }
        return highScoreArrayList;
    }

    public void addDataToListView() {
        highscoreList.setAdapter(getArrayAdapter());
        highscoreList = (ListView) findViewById(R.id.highscoreListView);
    }

    public void readFromDatabase() {
        sqLiteAdapterHighscore.open();
        Cursor cursor = sqLiteAdapterHighscore.readAllSortedDesc();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(getString(R.string.score)));
            String score = cursor.getString(cursor.getColumnIndex(getString(R.string.user)));
            nameList.add(name);
            scoreList.add(Integer.parseInt(score));
        }
        sqLiteAdapterHighscore.close();

        while (scoreList.size() < 7) {
            scoreList.add(-1);
            nameList.add(getString(R.string.Default));
        }
    }

    public void setBackButtonAction() {
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MyActivity.class));
            }
        });
    }

    public void updateHighScoreIfNew() {
        int tempscore;
        String tempName;
        boolean newNameRegistred = false;

        for (int i = 0; i < scoreList.size() - 1; i++) {
            if (currentScore > scoreList.get(i)) {
                if(!newNameRegistred){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    currentUser = sharedPreferences.getString(getString(R.string.User), getString(R.string.defaultUserName));
                    newNameRegistred = true;
                }
                tempscore = scoreList.get(i);
                tempName = nameList.get(i);

                scoreList.set(i, currentScore);
                nameList.set(i, currentUser);

                currentUser = tempName;
                currentScore = tempscore;
            }
        }
    }


    public void updateDatabase() {
        sqLiteAdapterHighscore.open();
        sqLiteAdapterHighscore.deleteAll();
        for (int i = 0; i < Math.max(nameList.size(), scoreList.size()); i++) {
            sqLiteAdapterHighscore.create(scoreList.get(i), nameList.get(i));
        }
        sqLiteAdapterHighscore.close();
    }

    public void getExtras() {
        Intent thisIntent = getIntent();
        currentScore = thisIntent.getIntExtra(getString(R.string.Score), 0);
    }

}
