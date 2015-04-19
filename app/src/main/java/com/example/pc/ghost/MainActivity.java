package com.example.pc.ghost;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Dictionary dictionary = new Dictionary("rabbit\ntiger\nox\ndragon\nsnake\ndog\nhorse\npig\nrooster\nmonkey");
        Game game = new Game(dictionary);


        TextView view = (TextView) findViewById(R.id.playerTurn);
        TextView view2 = (TextView) findViewById(R.id.playerWord);




        // Test values
        view.setText("Player 1 Turn");
        game.guess("r");
        view2.setText(game.word());
        game.turn();
        view.setText("Player 2 Turn");
        game.guess("a");
        view2.setText(game.word());
        game.turn();
        view.setText("Player 1 Turn");
        game.guess("b");
        view2.setText(game.word());
        game.turn();
        view.setText("Player 2 Turn");
        game.guess("b");
        view2.setText(game.word());
        game.turn();
        view.setText("Player 1 Turn");
        game.guess("i");
        view2.setText(game.word());
        game.turn();
        view.setText("Player 2 Turn");
        game.guess("t");
        view2.setText(game.word());

        // check if game ends, show winner
        if (game.ended())
        {
            if (game.winner())
            {
                view.setText("Player 1 Wins!");
            }
            else
            {
                view.setText("Player 2 Wins!");
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

}
