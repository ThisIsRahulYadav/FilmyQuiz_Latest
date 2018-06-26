package com.goodwill.emoji_dialog_classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.Emoji_Game_Screen;
import com.goodwill.filmyquiz.R;
import com.goodwill.helper.WrapperClass;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Emoji_GetPoints extends Dialog implements View.OnClickListener{
    public Activity activity;
    private String category;
    public Emoji_GetPoints(Activity activity,String category) {
        super(activity, R.style.full_screen_dialog);
        this.activity=activity;
        this.category=category;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.getpoints);
        setCanceledOnTouchOutside(false);
        Button rate_us = findViewById(R.id.rateUs);
        Button share_frnd = findViewById(R.id.shareFRND);
        Button facebook_like = findViewById(R.id.fbLike);
        Button whatch_video = findViewById(R.id.watchVideo);
        Button cross = findViewById(R.id.getPointsCross);
        rate_us.setOnClickListener(this);
        share_frnd.setOnClickListener(this);
        facebook_like.setOnClickListener(this);
        whatch_video.setOnClickListener(this);
        cross.setOnClickListener(this);


        WrapperClass refer = WrapperClass.getWrapperclass(activity);

        int coinValue = refer.getsharedpreference(activity, category + (activity.getResources().getString(R.string.clicked)), 0);
        Log.e("the value of rate us", String.valueOf(coinValue));
        if (coinValue > 0) {
            Log.e("the value of rate us af", String.valueOf(coinValue));
            findViewById(R.id.rate_us_layout).setVisibility(View.GONE);
        }

        int coinValuee = refer.getsharedpreference(activity, category + (activity.getResources().getString(R.string.clicked1)), 0);
        Log.e("the value facebook like", String.valueOf(coinValue));
        if (coinValuee > 0) {
            findViewById(R.id.facebook_like_layout).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rateUs:
                dismiss();
                Emoji_Game_Screen emoji_game_screen_rate_us =(Emoji_Game_Screen) activity;
               emoji_game_screen_rate_us.rate_US();
                break;

            case R.id.shareFRND:
                dismiss();
                Emoji_Game_Screen emoji_game_screen_share_frnds =(Emoji_Game_Screen) activity;
                emoji_game_screen_share_frnds.share_to_friends();
                break;

            case R.id.fbLike:
                dismiss();
                Emoji_Game_Screen emoji_game_screen_facebook_like =(Emoji_Game_Screen) activity;
                emoji_game_screen_facebook_like.like_Over_Facebook();
                break;

            case R.id.watchVideo:
                Emoji_Game_Screen emoji_game_screen_watch_video =(Emoji_Game_Screen) activity;
                emoji_game_screen_watch_video.watch_video();
                break;
            case R.id.getPointsCross:
                dismiss();
                break;
        }

    }
}
