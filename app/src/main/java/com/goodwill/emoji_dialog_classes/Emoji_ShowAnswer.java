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

public class Emoji_ShowAnswer extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Emoji_ShowAnswer(Activity activity) {
        super(activity);
        this.activity=activity;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_hint3);
        setCanceledOnTouchOutside(false);
        Button yes = findViewById(R.id.hint_3_Yes);
        Button cancel = findViewById(R.id.hint_3_Cancel);
        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hint_3_Yes:{
                dismiss();
                Emoji_Game_Screen emoji_game_screen =(Emoji_Game_Screen) activity;
            emoji_game_screen.emoji_showAnswer();break;}
            case R.id.hint_3_Cancel:{ dismiss(); break;}
        }
    }
}
