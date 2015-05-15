package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Cyzla on 14.05.2015.
 */
public class EnterNameActivity extends Activity {
    private Context context;
    private EditText nameInputField;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt);
        context = this;
        initTextInputField();
        initButton();
    }

    public void initTextInputField(){
        nameInputField = (EditText) findViewById(R.id.nameInputField);
    }

    public void initButton(){
        Button okButton = (Button) findViewById(R.id.nameOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInputField.getText().toString().length() > 0){
                    String name = nameInputField.getText().toString();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor sharedPreferencesEditor = preferences.edit();
                    sharedPreferencesEditor.putString(getString(R.string.User), name);
                    sharedPreferencesEditor.apply();

                    startActivity(new Intent(context, MyActivity.class));
                } else{
                    Toast.makeText(context, getString(R.string.PleaseEnterAName), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
