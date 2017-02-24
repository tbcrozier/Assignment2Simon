package com.davidroach.assignment2simon;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import java.util.Random;
import 	android.app.AlertDialog;

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

    int patternCount = 1;
    int turnPosition = 0;
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
        playerScore = 0;

        pattern = new int[100]; //100 should be a long enough pattern.


        play(modeResult);


    }






    private void playSound(int buttonId){

    }



 void play( String modeIn){

     /* Checks game mode and starts appropriate method*/

     if(modeIn.equals("SIMON_SAYS")){
         /* Fill pattern array with 100 random values that range between 1 and 4 */
         ssGeneratePattern();
         simonSays();
     }
     else if(modeIn.equals("PLAYER_ADDS")){
         playerAdds();

         //Add initial color to pattern pattern
         Random random = new Random();
         pattern[0] = random.nextInt(4 - 1 + 1) + 1;
         /* bot turn */
         paBotPlay();


     }
     else if(modeIn.equals("CHOOSE_YOUR_COLOR")){
         chooseYourColor();
     }

 }



////////////////////////////////////////////////////////////////////////


    void playerAdds(){
        Log.i("MODE: ", modeResult);


        /*In this mode the bot only plays once
         * The user repeats the pattern and adds one on their own.
         * The longest pattern you can muster before messing your self up is your score.
          * */



        /* Setup onclicks for each button */
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paInputCheck(R.id.green_button);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paInputCheck(R.id.red_button);
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paInputCheck(R.id.blue_button);
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paInputCheck(R.id.yellow_button);
            }
        });

    }


    /* Checks User input in "Player Add" Mode */
    private int paInputCheck(int buttonIdIn){
        Log.i("METHOD:", "ssInputCheck()");

        int colorCode  = getColorCode(buttonIdIn);

            /*check if button input is the one */

        if(colorCode != pattern[turnPosition]){
            playerLoses();
            return 0;
        }


        turnPosition++;
            /*Check if turn is over*/
        if(turnPosition == patternCount) {
            turnPosition = 0;

            /* Set flag that makes next input add to pattern */

            patternCount++;
            nap(1200);

            return 0;
        }else {

            playerScore++;


            nap(1200);
            return 0;
        }


    }


    /* Bot Play input in "Player Add" Mode */
    private void paBotPlay(){
        Log.i("PA_BOTPLAY:","STARTED");
        paBotTask botTurn =  new paBotTask();
        botTurn.execute();

    }


    class paBotTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            for( int x = 0; x < patternCount; x++){
                Log.i("colorCode = ", "" + pattern[x]);

                final int x2 = x;  //PROBLEM HERE
                int y=1+1;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int colorCode = pattern[x2];
                        lightButton(getButtonId(colorCode));
                    }
                });

                nap(700);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int colorCode = pattern[x2];
                        turnOffButton(getButtonId(colorCode));
                    }
                });

            }

            /* Add random button to sequence */

            return null;
        }
    }//end  pa async task


    //This probably is not needed.  I read the directions wrong.


    /*
    void paAddToPatern(){
        Random random = new Random();
        pattern[patternCount] = random.nextInt(4 - 1 + 1) + 1;

    }

    */

    ////////////////////////////////////////////////////////////////////////


    void chooseYourColor(){
        Log.i("MODE: ", modeResult);



    }


    void simonSays(){

        /* Computer play pattern */
        ssBotPlay();

        /* Setup onclicks for each button */
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssInputCheck(R.id.green_button);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssInputCheck(R.id.red_button);
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssInputCheck(R.id.blue_button);
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssInputCheck(R.id.yellow_button);
            }
        });

    }

    int ssInputCheck(int buttonIdIn){

        Log.i("METHOD:", "ssInputCheck()");

        int colorCode  = getColorCode(buttonIdIn);

            /*check if button input is the one */

        if(colorCode != pattern[turnPosition]){
            playerLoses();
            return 0;
        }


        turnPosition++;
            /*Check if turn is over*/
        if(turnPosition == patternCount) {
            turnPosition = 0;
            patternCount++;
            nap(1200);
            simonSays();
            return 0;
        }else {

            playerScore++;


            nap(1200);
            return 0;
        }

    }


    void ssBotPlay(){
        //Start BotPlay thread
        Log.i("ssBOT PLAY:", "Started");
        ssBotTask botTurn =  new ssBotTask();
        botTurn.execute();
    }


    void ssGeneratePattern(){
        /* Creates 100 color pattern for Simon Says mode */

        Random random = new Random();
        for(int x = 0; x<100; x++) {
            pattern[x] = random.nextInt(4 - 1 + 1) + 1;
        }
    }








    int getButtonId(int colorCodeIn){
        if(colorCodeIn == 1){
            return R.id.green_button;
        }
        else if (colorCodeIn == 2) {

            return R.id.red_button;
        }
        else if(colorCodeIn == 3){
            return R.id.yellow_button;
        }
        else if(colorCodeIn == 4){
            return R.id.blue_button;
        }
        else {

            Log.i("getButtonId:", "Unknown color code.");
            return 99;  //should never be returned.
        }

    }


    int getColorCode(int buttonIdIn){
        if(buttonIdIn == R.id.green_button){
            return 1;
        }
        else if (buttonIdIn == R.id.red_button) {

            return 2;
        }
        else if(buttonIdIn == R.id.yellow_button){
            return 3;
        }
        else if(buttonIdIn == R.id.blue_button){
            return 4;
        }
        else {

            Log.i("getButtonId:", "Unknown button id.");
            return 99;  //should never be returned.
        }

    }
    void nap(int time){
        try {
            Thread.sleep(time);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
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
            redButton.setBackgroundColor(Color.RED);
        }
        if(buttonIdIn == R.id.blue_button){
            blueButton.setBackgroundColor(Color.BLUE);
        }
        if(buttonIdIn == R.id.green_button){
            greenButton.setBackgroundColor(Color.GREEN);

        }
        if(buttonIdIn == R.id.yellow_button){
            yellowButton.setBackgroundColor(Color.YELLOW);
        }
    }


   void showYouLoseDialog(){
       Log.i("METHOD:", "showYouLoseDialog()");
       // 1. Instantiate an AlertDialog.Builder with its constructor
       AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        // 2. Chain together various setter methods to set the dialog characteristics
       builder.setMessage(R.string.you_lose);


        // 3. Get the AlertDialog from create()
       AlertDialog dialog = builder.create();
   }






    class ssBotTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            for( int x = 0; x < patternCount; x++){
                Log.i("X = ", "" + x);

                final int x2 = x;  //PROBLEM HERE
                 int y=1+1;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int colorCode = pattern[x2];
                        lightButton(getButtonId(colorCode));
                    }
                });

                nap(700);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int colorCode = pattern[x2];
                        turnOffButton(getButtonId(colorCode));
                    }
                });
            }
            return null;
        }

    }//end ss async task


    /* If this stays Redundant fix it.  Right now no time.*/
    void playerLoses(){
        showYouLoseDialog();
    }


}
