package com.nl.mprog.ghost;

// Joren de Bruin
// Minor Programmeren App Studio
// Studentnr. 10631267

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NamesActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    TextView namePrompt;
    EditText nameEntry, nameEntry2;
    String playerName1, playerName2, language;
    private Spinner names1, names2, languages;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_names);
        Intent intent = getIntent();

        nameEntry2 =(EditText)findViewById(R.id.nameEntry2);
        namePrompt = (TextView) findViewById(R.id.namePrompt);
        nameEntry = (EditText) findViewById(R.id.nameEntry);
        gestureDetectorCompat = new GestureDetectorCompat(this, new swipeListener2());


        // show spinner of existing names when applicable
        if (MainActivity.ScoreList.size() > 0)
        {
            List<String> tempList = new ArrayList<String>(MainActivity.NameList);
            tempList.add(0, "Existing Names");
            names1 = (Spinner) findViewById(R.id.spinner1);
            names2 = (Spinner) findViewById(R.id.spinner2);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    tempList);
            spinnerSetup(names1, arrayAdapter);
            spinnerSetup(names2, arrayAdapter);
        }

        languages = (Spinner) findViewById(R.id.spinner3);
        languages.setOnItemSelectedListener(this);


        nameEntry.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // ensure something gets entered
                    if (nameEntry.getText().toString().length() == 0)
                    {
                        nameEntry.setError("Enter a name!");
                    }
                    else if (nameEntry.getText().toString().length() > 10)
                    {
                        nameEntry.setError("Name is too long!");
                    }
                    else
                    {
                        playerName1 = nameEntry.getText().toString();
                        nameValidation(playerName1, "Player 1");
                    }

                    return true;
                }
                return false;
            }
        });

        nameEntry2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (nameEntry2.getText().toString().length() == 0)
                    {
                        nameEntry2.setError("Enter a name!");
                    }
                    else if (nameEntry2.getText().toString().length() > 10)
                    {
                        nameEntry2.setError("Name is too long!");
                    }
                    {
                        playerName2 = nameEntry2.getText().toString();
                        nameValidation(playerName2, "Player 2");
                        return true;
                    }
                }
                return false;
            }
        });

        Button startButton = (Button) findViewById(R.id.button4);
        startButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishNames();
            }
        });

    }

    public void spinnerSetup(Spinner spinner, ArrayAdapter<String> adapter)
    {
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
    }

    public String selectName(Spinner spinner, String player, EditText entry)
    {
        String name = spinner.getSelectedItem().toString();
        name = name.replaceAll("[^A-Za-z]", "");
        entry.setText(name);
        Toast.makeText(getBaseContext(),
                name+ " added as " + player,
                Toast.LENGTH_SHORT).show();
        return name;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id)
    {
        Spinner spinner = (Spinner) parentView;
        // ignore initial spinner position as selection
        if (spinner.getSelectedItemPosition() != 0)
        {
            if(spinner.getId() == R.id.spinner1)
            {
                playerName1 = selectName(spinner, "Player 1", nameEntry);
            }
            else if(spinner.getId() == R.id.spinner2)
            {
                playerName2 = selectName(spinner, "Player 2", nameEntry2);
            }
            else if(spinner.getId() == R.id.spinner3)
            {
                language = String.valueOf(spinner.getSelectedItem());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    // return on swiping left
    class swipeListener2 extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {


            if(event2.getX() > event1.getX()){

                finishNames();
            }
            return true;
        }
    }

    // prevent double name entries
    public void nameValidation(String Name, String Player)
    {
        if (MainActivity.ScoreList.containsKey(Name))
        {
            Toast.makeText(getBaseContext(),
                    Name+ " already exists",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getBaseContext(),
                    Name+ " added as " + Player,
                    Toast.LENGTH_SHORT).show();
            MainActivity.NameList.add(Name + " 0");
            MainActivity.ScoreList.put(Name, 0);
        }
    }

    // ensures names are entered, pass to main activity
    public void finishNames()
    {
        if (playerName1 == null || playerName2 == null) {
            Toast.makeText(getBaseContext(),
                    "No name(s) entered!",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("name1", playerName1);
            returnIntent.putExtra("name2", playerName2);
            returnIntent.putExtra("language", language);
            setResult(RESULT_OK, returnIntent);
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_names, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
