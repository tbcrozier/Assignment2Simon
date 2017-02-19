package com.davidroach.assignment2simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;

/**
 * Created by droach-dev on 2/15/17.
 */

public class GameActivity extends AppCompatActivity {

    Button greenButton;
    Button redButton;
    Button yellowButton;
    Button blueButton;

    String modeResult;

    int[] pattern;
    int patternCount;
    int playerScore = 0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get game mode selection
        Intent intentIn = getIntent();

         modeResult = intentIn.getStringExtra("GAMEMODE");

        Log.i("MODE_IN","->>" + modeResult); //debug code


        greenButton = (Button) findViewById(R.id.green_button);
        redButton = (Button) findViewById(R.id.red_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);
        blueButton = (Button) findViewById(R.id.blue_button);


        pattern = new int[100]; //100 should be a long enough pattern.
        patternCount = 0;

        play(modeResult);


    }





    private void lightButton(int buttonIdIn){

        if(buttonIdIn == R.id.red_button){
            redButton.setBackgroundColor(android.graphics.Color.WHITE);

        }
        if(buttonIdIn == R.id.blue_button){
            blueButton.setBackgroundColor(android.graphics.Color.WHITE);

        }
        if(buttonIdIn == R.id.green_button){
            greenButton.setBackgroundColor(android.graphics.Color.WHITE);

        }
        if(buttonIdIn == R.id.yellow_button){
            yellowButton.setBackgroundColor(android.graphics.Color.WHITE);

        }


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


 void play( String modeIn){

     /* Checks game mode and starts appropriate method*/

     if(modeIn.equals("SIMON_SAYS")){
         simonSays();

     }
     else if(modeIn.equals("PLAYER_ADDS")){
         playerAdds();
     }
     else if(modeIn.equals("CHOOSE_YOUR_COLOR")){
         chooseYourColor();

     }

 }


    void simonSays(){

        Log.i("MODE: ", modeResult);


    }


    void playerAdds(){
        Log.i("MODE: ", modeResult);




    }


    void chooseYourColor(){
        Log.i("MODE: ", modeResult);



    }


}
