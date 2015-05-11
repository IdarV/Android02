package no.student.westerdals.tjoida13.Android02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MyActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] myList = getResources().getStringArray(R.array.things);
        ArrayList<String> myArrayList= new ArrayList<String>(Arrays.asList(myList));
        Log.d("oncreate", "array length: " + myList.length);
        TextView txtView1 = (TextView) findViewById(R.id.textView4);
        Log.d("oncreate", "setting 4 to " + myList[6] + "88");
        txtView1.setText(myList[6] + "88");
    }
}
