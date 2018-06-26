package com.goodwill.emoji_dialog_classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.goodwill.dialod_classes.Get_Points;
import com.goodwill.filmyquiz.Emoji_Game_Screen;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Emoji_Alert_Dialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    String category;
    public Emoji_Alert_Dialog(Activity activity,String category) {
        super(activity);
        this.activity=activity;
        this.category=category;
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        setCanceledOnTouchOutside(false);
        Button yes = findViewById(R.id.popupYes);
        Button cancel = findViewById(R.id.popupCancel);

        // add button listener
        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popupYes:{
                new Emoji_GetPoints(activity,category).show();
                dismiss();
                break;}
            case R.id.popupCancel:{ dismiss();
                Emoji_Game_Screen emoji_game_screen=(Emoji_Game_Screen)activity;
                   emoji_game_screen.finish();
                break;}
        }
    }
}
