package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.Game_Screen;
import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/5/2018.
 */

public class Ask_friends extends Dialog implements View.OnClickListener{

    public Activity activity;
   // WrapperClass wrap = new WrapperClass(activity);

    public Ask_friends(Activity activity) {
        super(activity);
        this.activity=activity;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ask_frnd);
        setCanceledOnTouchOutside(false);
        Button yes = findViewById(R.id.ask_Frnd_Yes);
        Button cancel = findViewById(R.id.ask_Frnd_Cancel);
           yes.setOnClickListener(this);
           cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.ask_Frnd_Yes:{dismiss();
             Game_Screen game_screen=(Game_Screen)activity;
             game_screen.permission_Granted();
         game_screen.ask_To_Friend();
         }break;
         case R.id.ask_Frnd_Cancel:{ dismiss(); }
         break;
     }
    }
}
