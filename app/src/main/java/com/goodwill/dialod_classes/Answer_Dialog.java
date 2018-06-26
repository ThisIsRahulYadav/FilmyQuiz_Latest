package com.goodwill.dialod_classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.goodwill.filmyquiz.R;

/**
 * Created by lenovo on 3/6/2018.
 */

public class Answer_Dialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    String textToShow;
    public Answer_Dialog(Activity activity,String textToShow) {
        super(activity);
    this.activity=activity;
    this.textToShow=textToShow;

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hint1_answer);
        setCanceledOnTouchOutside(false);
        Button text = findViewById(R.id.hint_1_answer);
        text.setText(textToShow);
        Button cancel = findViewById(R.id.hint_1_cancel);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hint_1_cancel: {
                dismiss();
                break;
            }
        }}
}
