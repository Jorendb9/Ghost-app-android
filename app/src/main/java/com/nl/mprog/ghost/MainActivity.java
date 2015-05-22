package com.nl.mprog.ghost;

// Joren de Bruin
// Minor Programmeren App Studio
// Studentnr. 10631267



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends ActionBarActivity {

    EditText WordEntry;
    Game game;
    TextView playerView, wordView;
    Dictionary dictionary;
    String letter, Player1Name, Player2Name, language, cWord;
    Boolean first = true;

    private GestureDetectorCompat gestureDetectorCompat;
    public static List<String> NameList = new ArrayList<String>();
    public static HashMap<String, Integer> ScoreList = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cWord = "";


        // load scores when applicable
        try
        {
            loadScores();
            createNameList();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        language = "English";

        // boolean to track if a game is in progress
        first = loadFirst();
        if (first)
        {
            retrieveNames();
        }
        setContentView(R.layout.activity_main);


        gestureDetectorCompat = new GestureDetectorCompat(this, new swipeListener());
        playerView = (TextView) findViewById(R.id.playerTurn);
        wordView = (TextView) findViewById(R.id.playerWord);
        WordEntry = (EditText) findViewById(R.id.editText);
        WordEntry.setEnabled(true);


        createDictionary(language);
        newGameStart(dictionary);

        // load state if app was shut down with a game in progress
        if (!first)
            {
                loadState();
            }
        onLetterInput();


        Button resetButton = (Button) findViewById(R.id.button);
        resetButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(getBaseContext(),
                        "Reset!",
                        Toast.LENGTH_SHORT).show();
                onReset();
            }
        });
    }


    public void onReset()
    {
        dictionary.reset();
        first = true;
        newGameStart(dictionary);
        WordEntry.setEnabled(true);
        WordEntry.setVisibility(View.VISIBLE);
        playerView.setText(Player1Name+ "'s Turn");
        wordView.setText("");
        onLetterInput();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK)
            {
                Player1Name = data.getStringExtra("name1");
                Player2Name = data.getStringExtra("name2");


                // load new dictionary if language has been changed
                if (language != null)
                {
                    if (!language.equals(data.getStringExtra("language")))
                    {
                        language = data.getStringExtra("language");
                        createDictionary(language);
                    }
                    try
                    {
                        saveScores();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    onReset();
                }
            }
        }
        else if (requestCode == 2)
        {
            if (resultCode == RESULT_OK)
            {
                // back to name entry screen when coming from scores
                retrieveNames();
            }
        }
    }

    public void newGameStart(Dictionary dict)
    {
        game = new Game(dict);
        game.turn();
    }

    public void createDictionary(String language)
    {
        dictionary = new Dictionary(getApplicationContext(), language);
    }


    public void onLetterInput()
    {
        // activates when letter is submitted through editText
        WordEntry.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    first = false;
                    showLetter();
                    game.customEntry(cWord);
                    cWord = game.guess(letter);
                    dictionary.filter(cWord);
                    wordView.setText(cWord);
                    WordEntry.setText("");
                    // indicates the players turn
                    if (game.turn())
                    {
                        playerView.setText(Player1Name+ "'s Turn");
                    }
                    else
                    {
                        playerView.setText(Player2Name+ "'s Turn");
                    }
                    game.turn();
                    saveCurrentState();
                    victoryChecker();
                    return true;
                }
                return false;
            }
        });

    }

    // ignore words of 3 or fewer characters
    public void victoryChecker()
    {
        if (cWord.length() > 3)
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
    }

    public void saveCurrentState()
    {
        SharedPreferences settings = getSharedPreferences("GhostPreferences", 0);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putString("CurrentWord", wordView.getText().toString());
        preferencesEditor.putString("Player1", Player1Name);
        preferencesEditor.putString("Player2", Player2Name);
        preferencesEditor.putString("Language", language);
        preferencesEditor.putBoolean("Turn", game.turn());
        preferencesEditor.putBoolean("First", first);
        preferencesEditor.commit();
    }

    public void loadState()
    {
        SharedPreferences settings = getSharedPreferences("GhostPreferences", 0);
        String Word = settings.getString("CurrentWord", null);
        String Player1 = settings.getString("Player1", null);
        String Player2 = settings.getString("Player2", null);
        String Language = settings.getString("Language", null);
        Boolean turn = settings.getBoolean("Turn", false);
        if (Word != null && Player1!= null && Player2 != null && Language != null)
        {
            game.resume();
            cWord = Word;
            wordView.setText(Word);
            Player1Name = Player1;
            Player2Name = Player2;
            language = Language;
            if (turn == true)
            {
                playerView.setText(Player1Name + "'s Turn");
            }
            else
            {
                playerView.setText(Player2Name + "'s Turn");
            }
            game.turn();
        }

    }

    public boolean loadFirst()
    {
        SharedPreferences settings = getSharedPreferences("GhostPreferences", 0);
        return settings.getBoolean("First", true);
    }


    public void saveScores() throws IOException
    {
        FileOutputStream fos = openFileOutput("scores", Context.MODE_PRIVATE);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(ScoreList);
        oos.flush();
        oos.close();
        fos.close();
    }

    public void loadScores() throws IOException, ClassNotFoundException
    {
        try{
            FileInputStream fis = openFileInput("scores");
            ObjectInputStream ois=new ObjectInputStream(fis);

            ScoreList =(HashMap<String,Integer>)ois.readObject();
            ois.close();
            fis.close();

        }
        catch(Exception e)
        {
            System.out.println("Error loading file");
        }

    }

    public void retrieveNames()
    {
        Intent intent = new Intent(
                MainActivity.this, NamesActivity.class);
        startActivityForResult(intent, 1);
    }


    public void showLetter()
    {
        letter = WordEntry.getText().toString();
    }


    // update ScoreList and save
    public void onVictory(String Player)
    {
        first = true;
        NameList.clear();
        WordEntry.setVisibility(View.GONE);
        WordEntry.setEnabled(false);
        ScoreList.put(Player, ScoreList.get(Player) + 1);
        createNameList();
        String displayScore = Integer.toString(ScoreList.get(Player));
        playerView.setText(Player+ " Wins! Your total score is: " + displayScore);
        wordView.setText(game.reason());
        try {
            saveScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // generate ordered list of names based on ScoreList
    public void createNameList()
    {
        for (Map.Entry<String, Integer> entry  : entriesSortedByValues(ScoreList))
        {
            String toBeAdded = entry.getKey()+" "+ entry.getValue().toString();
            if (!NameList.contains(toBeAdded))
            {
                NameList.add(toBeAdded);
            }
        }
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


    // algorithm to sort the list of names
    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );
        return sortedEntries;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }



    class swipeListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY)
        {

            //switch to names activity by swiping right
            if(event2.getX() < event1.getX())
            {

                retrieveNames();
            }


            //switch to scoreActivity by swiping left
            if(event2.getX() > event1.getX())
            {

                Intent intent = new Intent(
                        MainActivity.this, ScoreActivity.class);
                startActivityForResult(intent, 2);
            }

            return true;

        }
    }
}

