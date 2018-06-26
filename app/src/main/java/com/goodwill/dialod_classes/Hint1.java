package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.goodwill.filmyquiz.Game_Screen;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/5/2018.
 */

public class Hint1 extends Dialog implements View.OnClickListener {

    public Activity activity;
    public  TextView coin;
    //WrapperClass wrap = new WrapperClass(activity);

    public Hint1(@NonNull Activity activity) {
        super(activity);
        this.activity=activity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_hint);
        setCanceledOnTouchOutside(false);
        Button yes = findViewById(R.id.hint_1_Yes);
        Button cancel = findViewById(R.id.hint_1_Cancel);
        coin =(TextView)activity.findViewById(R.id.coin);
        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hint_1_Yes:{
                dismiss();
                Game_Screen game_screen =(Game_Screen)activity;
                game_screen.hint1();
                break;
            }

            case R.id.hint_1_Cancel:{ dismiss(); break;}
        }
    }
}
