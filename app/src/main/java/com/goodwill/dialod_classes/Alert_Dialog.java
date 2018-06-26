package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.goodwill.filmyquiz.Game_Screen;
import com.goodwill.filmyquiz.R;

public class    Alert_Dialog extends Dialog implements OnClickListener {

    public Activity activity;
    private String category;

    public Alert_Dialog(Activity activity, String category) {
        super(activity);
        this.activity = activity;
        this.category = category;
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
        switch (v.getId()) {
            case R.id.popupYes: {
             //   Game_Screen game_screen = (Game_Screen) activity;
                //game_screen//.rate_us_hide();
                new Get_Points(activity,category).show();
                dismiss();
                break;
            }
            case R.id.popupCancel: {
                Game_Screen game_screen = (Game_Screen) activity;
                game_screen.finish();
                dismiss();
                break;
            }
        }
    }
}