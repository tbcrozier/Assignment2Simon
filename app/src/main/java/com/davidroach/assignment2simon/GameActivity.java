package com.davidroach.assignment2simon;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import 	android.app.AlertDialog;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by droach-dev on 2/15/17.
 */

public class GameActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;



    Button greenButton;
    Button redButton;
    Button yellowButton;
    Button blueButton;

    TextView scoreTextView;

    String modeResult;

    boolean paAddFlag = false;

    int[] pattern;

    int patternCount = 1;
    int turnPosition = 0;
    int playerScore = 0;
    int ssHighScore;
    int paHighScore;
    int ccHighScore;

    int button1SoundID;
    int button2SoundID;
    int button3SoundID;
    int button4SoundID;
    int failButtonSoundID;
    int razzButtonSoundID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //get game mode selection
        Intent intentIn = getIntent();

        //load game sounds
        soundsLoaded = new HashSet<Integer>();

         modeResult = intentIn.getStringExtra("GAMEMODE");

        Log.i("MODE_IN","->>" + modeResult); //debug code

        greenButton = (Button) findViewById(R.id.green_button);
        redButton = (Button) findViewById(R.id.red_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);
        blueButton = (Button) findViewById(R.id.blue_button);




        lockGameButtons();

        playerScore = 0;

        scoreTextView = (TextView) findViewById(R.id.score_tv);

        pattern = new int[100]; //100 should be a long enough pattern.

        findViewById(R.id.startGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.startGameButton).setClickable(false);
                findViewById(R.id.startGameButton).setAlpha(.2f);
                play(modeResult);
            }
        });





        //set on release listeners





    }


    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(2);

        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                if(status==0){//success
                    soundsLoaded.add(sampleId);
                    Log.i("SOUND","Sound loaded: " + sampleId);

                }else{
                    Log.i("SOUND","Error: Cannot load sound status = " +status);
                }


            }


        });

        button1SoundID = soundPool.load(this, R.raw.button1sound,1);
        button2SoundID = soundPool.load(this, R.raw.button2sound,1);
        button3SoundID = soundPool.load(this, R.raw.button3sound,1);
        button4SoundID = soundPool.load(this, R.raw.button4sound,1);
        failButtonSoundID = soundPool.load(this, R.raw.failsound,1);
        razzButtonSoundID = soundPool.load(this, R.raw.razzsound,1);


    }

    private void playSound(int soundId){
        if(soundsLoaded.contains(soundId)){
            soundPool.play(soundId,1.0f,1.0f,0,0,1.0f);
        }

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

        onTouchSetup();


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




        //check player add flag

        if(paAddFlag == true){
            playSound(chooseButtonSound(buttonIdIn));
            pattern[patternCount-1] = colorCode;
            paAddFlag = false;
            return 0;
        }
       else if(colorCode != pattern[turnPosition]){
            playSound(failButtonSoundID);
            playerLoses();
            return 0;
        }

        playSound(chooseButtonSound(buttonIdIn));
        turnPosition++;
            /*Check if turn is over*/
        if(turnPosition == patternCount) {
            turnPosition = 0;



            /* Set flag that makes next input add to pattern */
            paAddFlag = true;



            patternCount++;
            //nap(1200);

            return 0;
        }else {

            //check change
            playerScore = patternCount;
            scoreTextView.setText("Score: " + playerScore);


            //nap(600);
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

            lockGameButtons();

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


            unlockGameButtons();
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

        onTouchSetup();

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
            playSound(failButtonSoundID);
            playerLoses();
            return 0;
        }

        playSound(chooseButtonSound(buttonIdIn));


        turnPosition++;
            /*Check if turn is over*/
        if(turnPosition == patternCount) {
            turnPosition = 0;
            patternCount++;
            playerScore = patternCount-1;
            scoreTextView.setText("Score: " + playerScore);
            nap(1200);
            simonSays();
            return 0;
        }else {







            //nap(1200);
            return 0;
        }

    }

    /* return sound id to be play depending on button pressed */
    int chooseButtonSound(int buttonIdIn){
        if(buttonIdIn == R.id.green_button){
           return button1SoundID;
        }
        if(buttonIdIn == R.id.red_button){
            return button2SoundID;

        }
        if(buttonIdIn == R.id.yellow_button){
            return button3SoundID;

        }
        if(buttonIdIn == R.id.blue_button){
            return button4SoundID;
        }
        return 0;
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
            playSound(button2SoundID);
            //redButton.setBackgroundResource(android.R.drawable.btn_default);
        }
        if(buttonIdIn == R.id.blue_button){
            blueButton.setBackgroundColor(android.graphics.Color.WHITE);
            playSound(button4SoundID);
        }
        if(buttonIdIn == R.id.green_button){
            greenButton.setBackgroundColor(android.graphics.Color.WHITE);
            playSound(button1SoundID);

        }
        if(buttonIdIn == R.id.yellow_button){
            yellowButton.setBackgroundColor(android.graphics.Color.WHITE);
            playSound(button3SoundID);
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
       AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

       builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

               /*this.recreate() does not work here for some reason */

               Intent intent = getIntent();
               finish();
               startActivity(intent);

           }
       });


       builder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               Intent intent = new Intent(getApplicationContext(), Start.class);
               startActivity(intent);
           }
       });



        // 2. Chain together various setter methods to set the dialog characteristics
       builder.setMessage(R.string.you_lose);



        // 3. Get the AlertDialog from create()
       AlertDialog dialog = builder.create();

       dialog.show();
   }






    class ssBotTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            lockGameButtons();

            for( int x = 0; x < patternCount; x++){
                Log.i("X = ", "" + x);

                final int x2 = x;  //PROBLEM HERE
                 int y=1+1;

                final int colorCode = pattern[x2];
                final int buttonID = getButtonId(colorCode);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        lightButton(buttonID);
                    }
                });

                nap(700);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        turnOffButton(buttonID);
                    }
                });
            }

            unlockGameButtons();
            return null;
        }

    }//end ss async task


    /* If this stays Redundant fix it.  Right now no time.*/
    void playerLoses(){
        lockGameButtons();
        saveHighScore(playerScore, modeResult);
        showYouLoseDialog();
    }


    void lockGameButtons(){
        findViewById(R.id.red_button).setOnTouchListener(null);
        findViewById(R.id.blue_button).setOnTouchListener(null);
        findViewById(R.id.green_button).setOnTouchListener(null);
        findViewById(R.id.yellow_button).setOnTouchListener(null);
        findViewById(R.id.red_button).setClickable(false);
        findViewById(R.id.blue_button).setClickable(false);
        findViewById(R.id.green_button).setClickable(false);
        findViewById(R.id.yellow_button).setClickable(false);


    }

    void unlockGameButtons(){



        greenButton = (Button) findViewById(R.id.green_button);
        redButton = (Button) findViewById(R.id.red_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);
        blueButton = (Button) findViewById(R.id.blue_button);


        findViewById(R.id.red_button).setClickable(true);
        findViewById(R.id.blue_button).setClickable(true);
        findViewById(R.id.green_button).setClickable(true);
        findViewById(R.id.yellow_button).setClickable(true);

        //set on touch listeners for color change
        redButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    redButton.setBackgroundColor(android.graphics.Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    redButton.setBackgroundColor(android.graphics.Color.RED);

                }
                return false;
            }
        });

        yellowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    yellowButton.setBackgroundColor(android.graphics.Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    yellowButton.setBackgroundColor(android.graphics.Color.YELLOW);

                }                return false;
            }
        });

        blueButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    blueButton.setBackgroundColor(android.graphics.Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    blueButton.setBackgroundColor(android.graphics.Color.BLUE);

                }                return false;
            }
        });

        greenButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    greenButton.setBackgroundColor(android.graphics.Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    greenButton.setBackgroundColor(android.graphics.Color.GREEN);

                }
                return false;
            }
        });
    }


    void saveHighScore(int scoreIn, String modeIn){
        //determine game mode
        playerScore++;

        //get current saved high score


        //if current score is higher than saved high score save to persistent data

    }

    void onTouchSetup() {
    }




}
