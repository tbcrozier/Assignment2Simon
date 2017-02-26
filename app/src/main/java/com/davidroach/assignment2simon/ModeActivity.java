package com.davidroach.assignment2simon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.Window;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by droach-dev on 2/16/17.
 */

public class ModeActivity extends AppCompatActivity {


    TextView sshigh_tv;
    TextView pahigh_tv;
    TextView cychigh_tv;


    private enum Mode {
        SIMON_SAYS("SIMON_SAYS"),
        PLAYER_ADDS("PLAYER_ADDS"),
        CHOOSE_YOUR_COLOR("CHOOSE_YOUR_COLOR");

        private final String name;

        private Mode(String s) {
            name = s;
           }
        }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Disable title bar */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mode);


        /* High scores will be loade dinto these textviews */
        TextView sshigh_tv = (TextView) findViewById(R.id.ssh_score_tv);
        TextView pahigh_tv = (TextView) findViewById(R.id.pah_score_tv);
        TextView cychigh_tv = (TextView) findViewById(R.id.cych_score_tv);



        //Mode buttons
        //Pass game mode to next activity

        Button simonSaysButton = (Button) findViewById(R.id.simonsays_button);
        Button playerAddButton = (Button) findViewById(R.id.playeradds_button);
        Button chooseColorButton = (Button) findViewById(R.id.choosecolor_button);

        simonSaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGame(Mode.SIMON_SAYS);
            }
        });

        playerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGame(Mode.PLAYER_ADDS);
            }
        });

        chooseColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGame(Mode.CHOOSE_YOUR_COLOR);
            }
        });


    }

    private void goToGame(Mode modeIn){
        //Pass game mode Go to Mode Activity
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("GAMEMODE", modeIn.name);
        startActivity(intent);
    }
}
