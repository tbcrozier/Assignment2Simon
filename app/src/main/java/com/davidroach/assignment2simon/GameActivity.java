package com.davidroach.assignment2simon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import java.util.Random;

/**
 * Created by droach-dev on 2/15/17.
 */

public class GameActivity extends AppCompatActivity {

    Button greenButton;
    Button redButton;
    Button yellowButton;
    Button blueButton;

    String modeResult;

    boolean userMadeError = false;

    int[] pattern;

    int patternCount = 0;
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
            //redButton.setBackgroundResource(android.R.drawable.btn_default);
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


    private void turnOffButton(int buttonIdIn){
        if(buttonIdIn == R.id.red_button){
            redButton.setBackgroundResource(android.R.drawable.btn_default);
        }
        if(buttonIdIn == R.id.blue_button){
            blueButton.setBackgroundResource(android.R.drawable.btn_default);
        }
        if(buttonIdIn == R.id.green_button){
            greenButton.setBackgroundResource(android.R.drawable.btn_default);

        }
        if(buttonIdIn == R.id.yellow_button){
            yellowButton.setBackgroundResource(android.R.drawable.btn_default);
        }
    }

    private void playSound(int buttonId){

    }


    /*
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
*/

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

        /* Fill pattern array with 100 random values that range between 1 and 4 */
        generatePattern();




        /* Computer play pattern */
            botPlay();


        /* User try */
            userPlay();

        //patternCount++;


    }


    void playerAdds(){
        Log.i("MODE: ", modeResult);




    }


    void chooseYourColor(){
        Log.i("MODE: ", modeResult);



    }

    void botPlay(){

        //Start BotPlay thread
        Log.i("BOT PLAY:", "Started");
        ssBotTask botTurn =  new ssBotTask();
        botTurn.execute();


    }

    int userPlay(){
        //will return zero if user is right
        return 0;
    }


    void generatePattern(){
        /* Creates 100 color pattern for Simon Says mode */

        Random random = new Random();
        for(int x = 0; x<100; x++) {
            pattern[x] = random.nextInt(4 - 1 + 1) + 1;
        }
    }


    /* may not be needed */
    int checkButtonPlayed(){
        return 0;
    }


    class ssBotTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            for( int x = 0; x <= patternCount; x++){
                Log.i("X = ", "" + x);

                final int x2 = x;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int colorCode = pattern[x2];
                        lightButton(getButtonId(colorCode));
                    }
                });

                nap();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int colorCode = pattern[x2];
                        turnOffButton(getButtonId(colorCode));
                    }
                });

            }
            patternCount++;
            return null;
        }

    }//end async task



    int getButtonId(int colorCodeIn){
        if(colorCodeIn == 1){
            return R.id.green_button;
        }
        if (colorCodeIn == 2) {

            return R.id.red_button;
        }
        if(colorCodeIn == 3){
            return R.id.yellow_button;
        }
        if(colorCodeIn == 4){
            return R.id.blue_button;
        }

        Log.i("getButtonId:","Unknown color code.");
        return 99;  //should never be returned.

    }

    void nap(){
        try {
            Thread.sleep(700);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
