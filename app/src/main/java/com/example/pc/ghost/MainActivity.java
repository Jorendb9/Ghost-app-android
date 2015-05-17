package com.example.pc.ghost;


import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.view.GestureDetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText WordEntry;
    String letter;
    Game game;
    TextView view;
    TextView view2;
    Dictionary dictionary;
    String Player1Name;
    String Player2Name;
    private GestureDetectorCompat gestureDetectorCompat;
    public static List<String> NameList = new ArrayList<String>();
    public static HashMap<String, Integer> ScoreList = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Joren", "onCreate started");

        // retrieve player names
        if (NameList.size() == 0)
        {
            Intent intent = new Intent(
                    MainActivity.this, Names.class);
            startActivityForResult(intent, 1);
        }

        setContentView(R.layout.activity_main);


        EditText edit_txt = (EditText) findViewById(R.id.editText);
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());

        view = (TextView) findViewById(R.id.playerTurn);
        view2 = (TextView) findViewById(R.id.playerWord);
        WordEntry = (EditText) findViewById(R.id.editText);
        dictionary = new Dictionary(getApplicationContext(), "english");
        game = new Game(dictionary);
        game.turn();


        // activates when letter is submitted through editText
        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {


                    letter = WordEntry.getText().toString();
                    String cWord = game.guess(letter);
                    dictionary.filter(cWord);
                    view2.setText(cWord);
                    WordEntry.setText("");

                    // indicates the players turn
                    if (game.turn()== true)
                    {
                            view.setText(Player1Name+ "s Turn");
                    }
                    else
                    {
                        view.setText(Player2Name+ "s Turn");
                    }

                    if (dictionary.count() <= 1)
                    {
                        if (game.ended())
                        {
                            if (game.winner())
                            {
                                onVictory(Player1Name);

                            }
                            else
                            {
                                onVictory(Player2Name);
                            }
                        }

                    }
                    return true;
                }
                return false;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Player1Name = data.getStringExtra("name1");
                Player2Name = data.getStringExtra("name2");
            }
        }
    }


    public void onVictory(String Player)
    {
        ScoreList.put(Player, ScoreList.get(Player) + 1);
        int score = ScoreList.get(Player);
        String displayScore = Integer.toString(score);
        view.setText(Player+ " Wins! Your total score is: " + displayScore);
        int index = NameList.indexOf(Player);
        NameList.set(index, Player + " " + displayScore);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    //
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe left' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY)
        {

            if(event2.getX() < event1.getX())
            {
                Toast.makeText(getBaseContext(),
                        "Swipe left - startNames()",
                        Toast.LENGTH_SHORT).show();

                //switch to names activity
                Intent intent = new Intent(
                        MainActivity.this, Names.class);
                startActivity(intent);
            }

            if(event2.getX() > event1.getX())
            {
                Toast.makeText(getBaseContext(),
                        "Swipe right - startNamesList()",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        MainActivity.this, Namelist.class);
                startActivity(intent);




            }

            return true;


        }
    }
}

