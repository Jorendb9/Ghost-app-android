package com.example.pc.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Names extends ActionBarActivity {

    TextView namePrompt;
    EditText nameEntry;
    EditText nameEntry2;
    String playerName1;
    String playerName2;



    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_names);
        Intent intent = getIntent();

        nameEntry2 =(EditText)findViewById(R.id.nameEntry2);
        namePrompt = (TextView) findViewById(R.id.namePrompt);
        nameEntry = (EditText) findViewById(R.id.nameEntry);
        gestureDetectorCompat = new GestureDetectorCompat(this, new My2ndGestureListener());


        nameEntry.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    playerName1 = nameEntry.getText().toString();

                    //If name is new, add to list and initialize score as 0
                    if (MainActivity.NameList.contains(playerName1))
                    {
                        Toast.makeText(getBaseContext(),
                                playerName1+ " already exists",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),
                                playerName1+ " added as Player 1",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.NameList.add(playerName1);
                        MainActivity.ScoreList.put(playerName1, 0);
                    }
                    nameEntry.setText("");




                    return true;
                }
                return false;
            }
        });

        nameEntry2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    playerName2 = nameEntry2.getText().toString();



                    if (MainActivity.NameList.contains(playerName2))
                    {
                        Toast.makeText(getBaseContext(),
                                playerName2+ " already exists",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),
                                playerName2+ " added as Player 2",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.NameList.add(playerName2);
                        MainActivity.ScoreList.put(playerName2, 0);
                    }
                    nameEntry2.setText("");
                    return true;
                }
                return false;

            }
        });





    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class My2ndGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe right' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

         /*
         Toast.makeText(getBaseContext(),
          event1.toString() + "\n\n" +event2.toString(),
          Toast.LENGTH_SHORT).show();
         */

            if(event2.getX() > event1.getX()){
                Toast.makeText(getBaseContext(),
                        "Swipe right - finish()",
                        Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("name1", playerName1);
                returnIntent.putExtra("name2", playerName2);
                setResult(RESULT_OK, returnIntent);

                finish();


            }

            return true;
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
