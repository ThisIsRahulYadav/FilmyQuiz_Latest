package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.Game_Screen;
import com.goodwill.filmyquiz.R;
import com.goodwill.helper.WrapperClass;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Get_Points extends Dialog implements View.OnClickListener {

    public Activity activity;
    private String category;

    Get_Points(Activity activity, String category) {
        super(activity, R.style.full_screen_dialog);
        this.activity = activity;
        this.category = category;
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
//        Game_Screen game_screen =(Game_Screen)activity;
//        game_screen.rate_us_hide();

    }


   private void clickable() {
       Log.e("run click","yes");
       activity.findViewById(R.id.optionA).setClickable(true);
       activity.findViewById(R.id.optionB).setClickable(true);
       activity.findViewById(R.id.optionC).setClickable(true);
       activity.findViewById(R.id.optionD).setClickable(true);
    }

    @Override
    public void onClick(View v) {
        clickable();
        Log.e("is clickable",""+activity.findViewById(R.id.optionA).isClickable());
        switch (v.getId()) {
            case R.id.rateUs:
                dismiss();
                Game_Screen game_screen_rate_us = (Game_Screen) activity;
                game_screen_rate_us.rateUS();
                break;

            case R.id.shareFRND:
                dismiss();
                Game_Screen game_screen_share = (Game_Screen) activity;
                game_screen_share.share_to_friend();
                break;

            case R.id.fbLike:
                dismiss();
                Game_Screen game_screen_facebook = (Game_Screen) activity;
                game_screen_facebook.like_over_facebook();
                break;

            case R.id.watchVideo:
                Game_Screen game_screen_watch_video= (Game_Screen) activity;
                game_screen_watch_video.watch_video();
                dismiss();
                break;
            case R.id.getPointsCross:
                Game_Screen game_screen= (Game_Screen) activity;
                game_screen.finish();
                dismiss();
                break;
        }

    }

}
