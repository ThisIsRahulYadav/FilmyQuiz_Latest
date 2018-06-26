package com.goodwill.emoji_dialog_classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.Emoji_Game_Screen;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Emoji_hint2 extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Emoji_hint2(Activity activity) {
        super(activity);
    this.activity=activity;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_hint2_emoji_screen);
        setCanceledOnTouchOutside(false);
        Button yes = findViewById(R.id.hint_2_Yes_Emoji_Screen);
        Button cancel = findViewById(R.id.hint_2_Cancel_Emoji_Screen);
        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    @Override
    public void onClick (View v){
        switch (v.getId()){
            case R.id.hint_2_Yes_Emoji_Screen:
                dismiss();
                Emoji_Game_Screen emoji_game_screen =(Emoji_Game_Screen) activity;
                emoji_game_screen.emoji_hint2();

                break;

            case R.id.hint_2_Cancel_Emoji_Screen: dismiss(); break;
        }
    }

}
