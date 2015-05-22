package com.nl.mprog.ghost;

// Joren de Bruin
// Minor Programmeren App Studio
// Studentnr. 10631267


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;


public class ScoreActivity extends ActionBarActivity {

    private ListView lv;
    private GestureDetectorCompat gestureDetectorCompat;
    private Boolean deleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetectorCompat = new GestureDetectorCompat(this, new swipeListener3());
        setContentView(R.layout.activity_namelist);


        lv = (ListView) findViewById(R.id.listView);
        if (lv.getVisibility()== View.INVISIBLE)
        {
            lv.setVisibility(View.VISIBLE);
        }
        // fill listview using NameList
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                MainActivity.NameList );
        lv.setAdapter(arrayAdapter);



        // delete save data
        Button deleteButton = (Button) findViewById(R.id.button5);
        deleteButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(getBaseContext(),
                        "Scores Reset!",
                        Toast.LENGTH_SHORT).show();
                lv.setVisibility(View.INVISIBLE);
                emptyLists();
                deleteSave();
                SharedPreferences settings = getSharedPreferences("GhostPreferences", 0);
                SharedPreferences.Editor preferencesEditor = settings.edit();
                preferencesEditor.clear();
                preferencesEditor.commit();
            }
        });
    }

    public void emptyLists()
    {
        MainActivity.NameList.clear();
        MainActivity.ScoreList.clear();
    }

    public void deleteSave()
    {
        File dir = getFilesDir();
        File file = new File(dir, "scores");
        file.delete();
    }

    @Override
    public void onBackPressed()
    {
        finishNameList();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void finishNameList()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("deleted", deleted);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // return on swiping right
    class swipeListener3 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {


            if(event2.getX() < event1.getX())
            {
                finishNameList();
            }
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_namelist, menu);
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
