package com.goodwill.filmyquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodwill.helper.WrapperClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Levels extends AppCompatActivity {

    private WrapperClass refer;
    private String category;
    private MediaPlayer buttonClickSound;
    // private MediaPlayer rightMp;
  //  private MediaPlayer game_bg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_levels);
        refer = WrapperClass.getWrapperclass(this);
        category = getIntent().getStringExtra("CATEGORY");
        addOnClickListener();
        starCount();
        levelButton();
        myAdd();
       // Toast.makeText(this,"on create",Toast.LENGTH_SHORT).show();
    }

    private void playClickSound(){
        int clickSound = refer.getsharedpreference(this, "sound", 1);
        if(clickSound>0){
            try{
                if(buttonClickSound!=null){
                    buttonClickSound.stop();
                    buttonClickSound.reset();
                    buttonClickSound.release();
                }
            }catch (Exception ex){

            }
            buttonClickSound=null;

            buttonClickSound = MediaPlayer.create(this, R.raw.on_click_sound);
            buttonClickSound.start();
        }

    }
    private void myAdd() {


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
//			adView.setAdUnitId("ca-app-pub-4256132691253130/6561465201");
        adView.setAdUnitId("ca-app-pub-2556962058997611/3824907943");
        LinearLayout layout = (LinearLayout) findViewById(R.id.levelBannerAd);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);


    }
    protected void onStart() {
        super.onStart();



    }

    @Override
    public void onResume() {
        super.onResume();
        int sound = refer.getsharedpreference(this, "music", 1);
        if (sound > 0) {
            // music_switch.setChecked(true);

            refer.startSound(Levels.this);
        } else {
            //  music_switch.setChecked(false);
            refer.stopSound(Levels.this);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        int sound = refer.getsharedpreference(Levels.this, getResources().getString(R.string.music), 1);
        Log.e("sound at pause", "" + sound);
        if (sound > 0) {
           /* game_bg.setLooping(false);
            game_bg.stop();
            game_bg.release();*/
            refer.stopSound(Levels.this);
        }
    }

    View.OnClickListener level = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            refer.setCurrentlevel((Integer) v.getTag());

            playClickSound();
            refer.setsharedpreference(Levels.this,"CURRENT",Integer.parseInt(v.getTag().toString()));
            Intent myIntent = new Intent(Levels.this, Category.class);
            myIntent.putExtra("CATEGORY", category);
            startActivity(myIntent);
            Levels.this.finish();
        }
    };

    private void addOnClickListener() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                Levels.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void starCount() {
        int star = refer.getsharedpreference(Levels.this, category + (getResources().getString(R.string.star)), 0);
        TextView  starCount = (TextView) findViewById(R.id.starCount);
        starCount.setText(star + "/900");

    }


    @SuppressLint("LongLogTag")
    private void levelButton() {
        LinearLayout levels = (LinearLayout) findViewById(R.id.levels);
        int width = getResources().getDisplayMetrics().widthPixels;
        int counter = 0;
        int count = 1;
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(width / 5, width / 5);
        buttonParams.setMargins(width / 25, width / 25, 0, 0);

        for (int i = 0; i < 50; i++) {

            LinearLayout rowLayout = new LinearLayout(this);
            for (int j = 0; j < 4; j++) {
                Button levelButton = new Button(this);
                levelButton.setLayoutParams(buttonParams);
                int currentLevel = refer.getsharedpreference(Levels.this, category + (getResources().getString(R.string.currentLevel)), 3);
                Log.e("current level value", String.valueOf(currentLevel));
                if (counter < currentLevel) {

                    levelButton.setBackgroundResource(R.drawable.unlock);
                    levelButton.setOnClickListener(level);
                    counter++;
                } else {
                    levelButton.setBackgroundResource(R.drawable.lockk);
                }
                levelButton.setTag(count);
                levelButton.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                levelButton.setPadding(0, 0, 0, width / 50);
                levelButton.setTextSize(25);
                levelButton.setTextColor(Color.parseColor("#FF6CA5D6"));
                levelButton.setText("" + count);
                rowLayout.addView(levelButton);
                count++;
            }
            levels.addView(rowLayout);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{

            if(buttonClickSound!=null){
                buttonClickSound.stop();
                buttonClickSound.reset();
                buttonClickSound.release();
                buttonClickSound=null;
            }
        }catch (Exception e){

        }
    }
}

