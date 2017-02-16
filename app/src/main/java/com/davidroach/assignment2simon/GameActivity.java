package com.davidroach.assignment2simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by droach-dev on 2/15/17.
 */

public class GameActivity extends AppCompatActivity {

    Button greenButton;
    Button redButton;
    Button yellowButton;
    Button blueButton;

    int[] pattern;
    int patternCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        greenButton = (Button) findViewById(R.id.green_button);
        redButton = (Button) findViewById(R.id.red_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);
        blueButton = (Button) findViewById(R.id.blue_button);

        pattern = new int[100]; //100 should be a long enough pattern.
        patternCount = 0;

        play();





    }





    private void lightButton(int buttonId){

    }

    private void playSound(int buttonId){

    }

    private class GameTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


 void play(){

 }


}
