package com.goodwill.emoji_dialog_classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.dialod_classes.Ask_friends;
import com.goodwill.dialod_classes.Hint1;
import com.goodwill.dialod_classes.Hint2_Gamescreen;
import com.goodwill.dialod_classes.Show_Answer;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Emoji_Get_Hints extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Emoji_Get_Hints(Activity activity) {
        super(activity);
        this.activity=activity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hint_popup);
        setCanceledOnTouchOutside(false);
        Button ask_frnd =findViewById(R.id.ask_friend);
        Button hint1=findViewById(R.id.get_hint_1);
        Button hint2=findViewById(R.id.get_hint_2);
        Button show_answer=findViewById(R.id.get_hint_3);
        Button cross =findViewById(R.id.cross);
        ask_frnd.setOnClickListener(this);
        hint1.setOnClickListener(this);
        hint2.setOnClickListener(this);
        show_answer.setOnClickListener(this);
        cross.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ask_friend:
                new Emoji_Ask_Friend(activity).show();
                dismiss();
                break;

            case R.id.get_hint_1:
                new Emoji_hint1(activity).show();
                dismiss();
                break;

            case R.id.get_hint_2:
                new Emoji_hint2(activity).show();
                dismiss();
                break;

            case R.id.get_hint_3:
                new Emoji_ShowAnswer(activity).show();
                dismiss();
                break;
            case R.id.cross:
                dismiss();
                break;
        }

    }
}
