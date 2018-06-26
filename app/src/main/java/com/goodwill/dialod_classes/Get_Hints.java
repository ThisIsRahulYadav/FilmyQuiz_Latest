package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.Game_Screen;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/5/2018.
 */

    public class Get_Hints extends Dialog implements View.OnClickListener
{

    public Activity activity;
    public Get_Hints(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
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
          new Ask_friends(activity).show();
          dismiss();
       break;

        case R.id.get_hint_1:
            new Hint1(activity).show();
            dismiss();
        break;

        case R.id.get_hint_2:
            new Hint2_Gamescreen(activity).show();
            dismiss();
            break;

        case R.id.get_hint_3:
            new Show_Answer(activity).show();
            dismiss();
            break;
        case R.id.cross:
            dismiss();
            break;
    }

    }
}
