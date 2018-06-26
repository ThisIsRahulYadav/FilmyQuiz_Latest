package com.goodwill.filmyquiz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodwill.emoji_dialog_classes.Emoji_Alert_Dialog;
import com.goodwill.emoji_dialog_classes.Emoji_Answer_Dialog;
import com.goodwill.emoji_dialog_classes.Emoji_Get_Hints;
import com.goodwill.helper.Question;
import com.goodwill.helper.ReadFromDatabase;
import com.goodwill.helper.WrapperClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.view.Gravity.CENTER;

public class Emoji_Game_Screen extends AppCompatActivity implements RewardedVideoAdListener {

    private int storage_permission_code = 1;
    private WrapperClass refer;
    private TextView coin;
    private Question question;
    private String category;

    private int currantLevel;
    private InterstitialAd interstitial;

    private MediaPlayer buttonClickSound;
    private MediaPlayer playRightWrongSound;

    private void already_played() {
        int coinValue = refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.emoji) + currantLevel), 0);
        if (coinValue > 0) {
            alreadyPlayed = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_game_screen);
        refer = WrapperClass.getWrapper.class(this);
        currantLevel = refer.getsharedpreference(this, "CURRENT", 1);
        category = getIntent().getStringExtra("CATEGORY");
        addOnClickListener();
        already_played();
        emoji_screen_question();
        coinUpdate();
        question = ReadFromDatabase.readFromDatabaseEmoji(currantLevel, this, category + ("emoji_db.txt"));
        String picName = question.getPicname();
        String[] str = picName.split(",");
        setEmoji(str);


        final String movieName = question.getAnswer();
        String[] r = movieName.split(" ");
        setanswerKey(r);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String moviename = question.getAnswer();

                String  movie = moviename.replaceAll(" ", "");

                int movie_lng = movie.length();

                int randomcharLen;
                if (movie_lng <= 7) {
                    randomcharLen = 10 - movie_lng;
                } else if (movie_lng <= 14) {
                    randomcharLen = 20 - movie_lng;
                } else {
                    randomcharLen = 30 - movie_lng;
                }
                if (alreadyPlayed) {
                    String str = "";
                    for (int i = 0; i < movie.length(); i++) {
                        str += " ";
                    }
                    movie = str;
                }
                String answerName = shuffle(movie.toUpperCase() + getRandomString(randomcharLen));
                setAnswerKeyboard(answerName);
            }
        }, 500);

        myAdd();
        rewardedVideoAdd();
        intrestial();
        int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
        if (coinValue < 10) {
            new Emoji_Alert_Dialog(Emoji_Game_Screen.this,category).show();
        }
    }

    private void playRightWrongSound(int rawFileName) {

        int playSound = refer.getsharedpreference(this, "sound", 1);
        if (playSound > 0) {

            try {
                if (playRightWrongSound != null) {
                    playRightWrongSound.stop();
                    playRightWrongSound.reset();
                    playRightWrongSound.release();
                    playRightWrongSound = null;
                }
            } catch (Exception ex) {
                Log.e("MediaPlayRightWrong", ex.toString());
            }
            playRightWrongSound = null;

            playRightWrongSound = MediaPlayer.create(this, rawFileName);
            playRightWrongSound.start();
        }
    }

    private void playClickSound() {
        int clickSound = refer.getsharedpreference(this, "sound", 1);
        if (clickSound > 0) {
            try {
                if (buttonClickSound != null) {
                    buttonClickSound.stop();
                    buttonClickSound.reset();
                    buttonClickSound.release();
                    buttonClickSound = null;
                }
            } catch (Exception ex) {
                Log.e("MediaPlayClickSound", ex.toString());
            }
            buttonClickSound = null;

            buttonClickSound = MediaPlayer.create(this, R.raw.on_click_sound);
            buttonClickSound.start();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        rightMp = MediaPlayer.create(this, R.raw.beep_right);
//        wrongMp = MediaPlayer.create(this, R.raw.beep_wrong);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int sound = refer.getsharedpreference(this, "music", 1);
        if (sound > 0) {


            refer.startSound(Emoji_Game_Screen.this);
        } else {

            refer.stopSound(Emoji_Game_Screen.this);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        int sound = refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.music), 1);
        Log.e("sound at pause", "" + sound);
        if (sound > 0) {

            refer.stopSound(Emoji_Game_Screen.this);
        }
    }

    private void intrestial() {

        interstitial = new InterstitialAd(this);

        interstitial.setAdUnitId("ca-app-pub-2556962058997611/6962616293");

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);


    }


    RewardedVideoAd mAd;

    private void rewardedVideoAdd() {

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd.loadAd("ca-app-pub-2556962058997611/4883247868", new AdRequest.Builder().build());

    }

    private void myAdd() {


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-2556962058997611/2687495211");
        LinearLayout layout = (LinearLayout) findViewById(R.id.gameBannerAd);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);


    }

    public String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    private String getRandomString(int strLength) {
        String str = "";
        String options = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < strLength; i++) {
            Random random = new Random();
            int ran = random.nextInt(25);
            str += options.charAt(ran);
        }
        return str;
    }


    private void addOnClickListener() {

        coin = (TextView) findViewById(R.id.coin);
        Button back_emoji = (Button) findViewById(R.id.back_emoji);
        TextView hint_button = (TextView) findViewById(R.id.hint_button);
        hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickSound();
                new Emoji_Get_Hints(Emoji_Game_Screen.this).show();
            }
        });


        back_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                if (interstitial.isLoaded()) interstitial.show();
                Emoji_Game_Screen.this.finish();

            }
        });


    }


    private void manageHint2() {
        String answer = question.getAnswer().replace(" ", "");
        Log.e("answer", answer);
        LinearLayout keysFrame = (LinearLayout) findViewById(R.id.keysFrame);
        for (int i = 0; i < answer.length(); i++) {
            int breaks = 0;
            Log.e("answerKey", "" + answer.charAt(i));
            for (int j = 0; j < keysFrame.getChildCount() && breaks == 0; j++) {
                LinearLayout layout = (LinearLayout) keysFrame.getChildAt(j);
                for (int k = 0; k < layout.getChildCount(); k++) {
                    String tag = layout.getChildAt(k).getTag().toString();
                    Log.e("tag", tag);
                    Log.e("char at i", "" + answer.charAt(i));
                    if (tag.equalsIgnoreCase("" + answer.charAt(i)) && layout.getChildAt(k).getId() != 0) {
                        Log.e("answerInnerKey", "" + answer.charAt(i));
                        layout.getChildAt(k).setBackgroundResource(R.drawable.green_key_button);
                        layout.getChildAt(k).setId(0);
                        breaks = 1;
                        break;
                    }
                }
            }
        }
    }

    private void emoji_screen_question() {

        question = ReadFromDatabase.readFromDatabaseEmoji(currantLevel, this, category + ("emoji_db.txt"));
        if (question != null) {
            TextView emoji_questions = (TextView) findViewById(R.id.emoji_questions);
            emoji_questions.setText(question.getQuestion());
        }
    }

    public void coinUpdate() {

        int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
        coin.setText("" + coinValue);
    }

    private void setEmoji(final String[] emojiNames) {

        final LinearLayout emojiFrame = (LinearLayout) findViewById(R.id.emojiFrame);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int width = emojiFrame.getWidth();
                int cellWidth = width / 6;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cellWidth, cellWidth);
                layoutParams.setMargins(cellWidth / 12, 0, 0, 0);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(cellWidth - cellWidth / 8, cellWidth - cellWidth / 8);
                for (int i = 0; i < emojiNames.length; i++) {
                    View view = new View(Emoji_Game_Screen.this);
                    LinearLayout view1 = new LinearLayout(Emoji_Game_Screen.this);
                    view1.setGravity(CENTER);
                    view1.setBackgroundResource(R.drawable.square_emoji_background);
                    view1.setLayoutParams(layoutParams);
                    view.setLayoutParams(layoutParams1);
                    view1.addView(view);
                    int resourceId = Emoji_Game_Screen.this.getResources().getIdentifier(emojiNames[i], "drawable", getPackageName());
                    view.setBackgroundResource(resourceId);
                    emojiFrame.addView(view1);
                }
            }
        }, 300);
    }


    boolean alreadyPlayed = false;


    private void setanswerKey(final String d[]) {
        final LinearLayout answerFrame = (LinearLayout) findViewById(R.id.answerFrame);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int width = answerFrame.getWidth();
                int cellWidth = width / 12;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cellWidth, cellWidth + 20);
                layoutParams.setMargins(width / 90, width / 95, 2, 0);
                for (int i = 0; i < d.length; i++) {
                    Log.e("the value of i", String.valueOf(i));
                    LinearLayout rowLayout = new LinearLayout(Emoji_Game_Screen.this);
                    rowLayout.setGravity(CENTER);
                    for (int j = 0; j < d[i].length(); j++) {
                        Log.e("the value of j", String.valueOf(j));
                        TextView view = new TextView(Emoji_Game_Screen.this);
                        view.setBackgroundResource(R.drawable.empty_button);
                        view.setText("");
                        if (alreadyPlayed) {
                            view.setBackgroundResource(R.drawable.key_button);
                            view.setText(("" + d[i].charAt(j)).toUpperCase());
                            view.setBackgroundResource(R.drawable.green_key_button);
                            //view.setOnClickListener(null);
                        } else {
                            view.setOnClickListener(emoji_answer);
                        }
                        view.setGravity(Gravity.CENTER);
                        view.setLayoutParams(layoutParams);
                        rowLayout.addView(view);
                    }
                    answerFrame.addView(rowLayout);
                }
            }
        }, 500);
    }


    private void setAnswerKeyboard(String answerword) {
        LinearLayout keysFrame = (LinearLayout) findViewById(R.id.keysFrame);
        int width = keysFrame.getWidth();
        int cellWidth = width / 12;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cellWidth, cellWidth + 20);
        layoutParams.setMargins(2, 2, 2, 2);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (count >= answerword.length()) break;
            LinearLayout rowlayout = new LinearLayout(Emoji_Game_Screen.this);
            rowlayout.setGravity(CENTER);
            for (int j = 0; j < 10; j++) {
                TextView view1 = new TextView(Emoji_Game_Screen.this);
                view1.setBackgroundResource(R.drawable.key_button);
                if (alreadyPlayed)
                    view1.setVisibility(View.INVISIBLE);
                if (!alreadyPlayed)
                    view1.setOnClickListener(keyboradlistener);
                view1.setTag("" + answerword.charAt(count));
                view1.setText("" + answerword.charAt(count));
                count++;
                view1.setTextSize(20);
                view1.setGravity(CENTER);
                view1.setLayoutParams(layoutParams);
                rowlayout.addView(view1);
                if (count > answerword.length()) break;
            }
            keysFrame.addView(rowlayout);
        }
    }


    View.OnClickListener keyboradlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playClickSound();
            int breaks = 0;
            String tag = v.getTag().toString();
            String a = "";
            a += tag;
            Log.e("value of log", tag);
            Log.e("value of log", a);
            String userFilledAnswer = "";
            int count = 0;
            //   boolean isLast = false;
            LinearLayout answerFrame = (LinearLayout) findViewById(R.id.answerFrame);
            int answerframeChild = answerFrame.getChildCount();

            for (int i = 0; i < answerframeChild; i++) {

                LinearLayout layout = (LinearLayout) answerFrame.getChildAt(i);
                if (i > 0) {
                    userFilledAnswer += " ";
                    count++;
                    }
                //   boolean islastRow = false;
                for (int j = 0; j < layout.getChildCount(); j++) {
                    Log.e("I,j", "i=" + i + ", J=" + j);

                    TextView textView = ((TextView) layout.getChildAt(j));

                    if (textView.getText().toString().isEmpty() && breaks != 1) {
                        textView.setText(tag);
                        textView.setTextColor(Color.BLACK);
                        textView.setBackgroundResource(R.drawable.key_button);
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                        textView.startAnimation(animation);
                        v.setVisibility(View.INVISIBLE);
                        /*if (j == layout.getChildCount() - 1 && i == answerframeChild - 1) {
                            isLast = true;
                        }*/
                        breaks = 1;
                    }

                    userFilledAnswer += textView.getText().toString();

                }


                Log.e("the value of aaaa loof", a);
            }

            Log.e("Count", "" + count);
            Log.e("userFilledAnswer", userFilledAnswer);
            Log.e("question.getAnswer()", question.getAnswer());
            int sound2 = refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.sound), 1);
            if (checkAllFilled())
                if (userFilledAnswer.equalsIgnoreCase(question.getAnswer())) {
                    playRightWrongSound(R.raw.beep_right);


                    Toast.makeText(Emoji_Game_Screen.this, "GREAT CORRECT ANSWER +30", Toast.LENGTH_SHORT).show();

                    refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
                    refer.setsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.star)), refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.star)), 0) + 1);
                    int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
                    coin.setText("" + coinValue);
                    int starValue = refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.star)), 0);
                    int levelValue = refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.currentLevel)), 3);
                    if (starValue == (levelValue * 2 + 1)) {
                        refer.setsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.currentLevel)), refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.currentLevel)), 3) + 1);
                        Toast.makeText(Emoji_Game_Screen.this, String.valueOf("NEW LEVEL UNLOCK"), Toast.LENGTH_SHORT).show();
                    }
                    refer.setsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.emoji) + currantLevel), 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (interstitial.isLoaded()) interstitial.show();
                            Emoji_Game_Screen.this.finish();
                        }
                    }, 2000);
                    changeButtonColor(R.drawable.green_key_button);
                } else {
                   // Toast.makeText(Emoji_Game_Screen.this, "WRONG ANSWER TRY AGAIN!", Toast.LENGTH_SHORT).show();
                    changeButtonColor(R.drawable.red_key_button);
                    refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) - 10);
                    int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
                    coin.setText("" + coinValue);
                    Toast.makeText(Emoji_Game_Screen.this, "oops TRY AGAIN! -10", Toast.LENGTH_SHORT).show();
                    playRightWrongSound(R.raw.beep_wrong);
                    if (coinValue < 10) {
                        refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
                        coin.setText("0");
                    new Emoji_Alert_Dialog(Emoji_Game_Screen.this,category).show();
                    }
                    }
        }
    };
    View.OnClickListener emoji_answer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playClickSound();
            TextView button = (TextView) v;
            int breaks = 0;
            if (!button.getText().toString().isEmpty()) {
                String text = button.getText().toString();
                LinearLayout keysFrame = (LinearLayout) findViewById(R.id.keysFrame);
                for (int i = 0; i < keysFrame.getChildCount(); i++) {
                    LinearLayout view = (LinearLayout) keysFrame.getChildAt(i);
                    for (int j = 0; j < view.getChildCount(); j++) {
                        if (view.getChildAt(j).getVisibility() == View.INVISIBLE && view.getChildAt(j).getTag().equals(text)) {
                            view.getChildAt(j).setVisibility(View.VISIBLE);
                            button.setText("");
                            button.setBackgroundResource(R.drawable.empty_button);
                            breaks = 1;
                            break;
                        }
                    }
                    if (breaks == 1) break;
                }
            }
        }
    };


    private boolean checkAllFilled() {
        LinearLayout answerFrame = (LinearLayout) findViewById(R.id.answerFrame);
        int answerframeChild = answerFrame.getChildCount();
        for (int i = 0; i < answerframeChild; i++) {

            LinearLayout layout = (LinearLayout) answerFrame.getChildAt(i);

            for (int j = 0; j < layout.getChildCount(); j++) {

                TextView textView = ((TextView) layout.getChildAt(j));

                if (textView.getText().toString().isEmpty()) {
                    return false;
                }

            }


        }
        return true;
    }

    private void changeButtonColor(int drawable) {
        LinearLayout answerFrame = (LinearLayout) findViewById(R.id.answerFrame);
        int answerframeChild = answerFrame.getChildCount();


        for (int i = 0; i < answerframeChild; i++) {

            LinearLayout layout = (LinearLayout) answerFrame.getChildAt(i);

            for (int j = 0; j < layout.getChildCount(); j++) {
                Log.e("I,j", "i=" + i + ", J=" + j);

                TextView textView = ((TextView) layout.getChildAt(j));

                textView.setBackgroundResource(drawable);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitial.isLoaded()) interstitial.show();
        Emoji_Game_Screen.this.finish();
    }

    public void emoji_hint1() {
        int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
        if (coinValue >= 10) {
            refer.setsharedpreference(Emoji_Game_Screen.this, "coin", refer.getsharedpreference(Emoji_Game_Screen.this, "coin", 30) - 10);
            int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coin_Value);
            new Emoji_Answer_Dialog(Emoji_Game_Screen.this, question.getHint1()).show();
        }
        if (coinValue < 10) {
            new Emoji_Alert_Dialog(Emoji_Game_Screen.this, category).show();
        }
        if (coinValue < 4) {
            refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
            coin.setText("0");
        }
    }

    public void emoji_hint2() {
        int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
        if (coinValue >= 30) {
            refer.setsharedpreference(Emoji_Game_Screen.this, "coin", refer.getsharedpreference(Emoji_Game_Screen.this, "coin", 30) - 30);
            int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coin_Value);
            manageHint2();
        }
        if (coinValue < 30) {
           /* refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
            coin.setText("0");*/
            new Emoji_Alert_Dialog(Emoji_Game_Screen.this, category).show();
            if (coinValue < 4) {
                refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
                coin.setText("0");
            }
        }
    }

    public void emoji_showAnswer() {
        final int coinsValue = refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30);
        Log.e("coins valueeeee", String.valueOf(coinsValue));
        if (coinsValue < 50) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //  Animation leftToRight = AnimationUtils.loadAnimation(Emoji_Game_Screen.this, R.anim.slideright);
                    new Emoji_Alert_Dialog(Emoji_Game_Screen.this, category).show();
                }
            }, 200);
        } else {
            refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) - 50);
            int coinValue = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coinValue);
            new Emoji_Answer_Dialog(Emoji_Game_Screen.this, question.getAnswer()).show();
        }

    }


    public void rate_US() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(intent);
        refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coin_Value);
            }
        }, 5000);
        refer.setsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.clicked)), 1);
        int coinValue = refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.clicked)), 0);
        Log.e("the value of rate us", String.valueOf(coinValue));
        if (coinValue > 0) {
            Log.e("the value of rate us af", String.valueOf(coinValue));
            findViewById(R.id.rate_us_layout).setVisibility(View.INVISIBLE);
        }

    }

    public void like_Over_Facebook() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.facebook.com/goodwilltechnologyservices/"));
        startActivity(intent);
        refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coin_Value);
            }
        }, 5000);
        refer.setsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.clicked1)), 1);
        int coinValue = refer.getsharedpreference(Emoji_Game_Screen.this, category + (getResources().getString(R.string.clicked1)), 0);
        Log.e("the value facebook like", String.valueOf(coinValue));
        if (coinValue > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.facebook_like_layout).setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    public void share_to_friends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, \n I Am Playing This Very Challenging \n 3 in 1 Filmy Quiz its Really AMAZING!\n Play For Free on   \n https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this site!");
        startActivity(Intent.createChooser(intent, "Share"));
        refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) + 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coin_Value);
            }
        }, 5000);
    }

    public void watch_video() {

        if (mAd.isLoaded()) {
            mAd.show();
        } else {
            Toast.makeText(this, "Unable to load Video", Toast.LENGTH_SHORT).show();
        }
    }


    public Bitmap loadBitmapFromView() {

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.myLayout);
        layout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void ask_To_Friend() {
        Bitmap bitmap = loadBitmapFromView();

        String myFormat = "dd-MM-yy-HH-mm-ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar myCalendar = Calendar.getInstance();
        String dateTime = sdf.format(myCalendar.getTime());


        File exportDir = new File(Environment.getExternalStorageDirectory() + File.separator + "FilmyQuiz", "");

        long freeBytesInternal = new File(this.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        long megAvailable = freeBytesInternal / 1048576;

        if (megAvailable < 0.1) {
            System.out.println("Please check" + megAvailable);
        } else {

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            try {
                String fileNme = "report-" + dateTime + ".jpg";
                Log.e("fileName", fileNme);
                File file = new File(exportDir, fileNme);
                file.createNewFile();

                OutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();

                Uri bmpUri = Uri.parse(exportDir + "/" + fileNme);
                final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                emailIntent1.setType("image/png");
                emailIntent1.putExtra(Intent.EXTRA_TEXT, "Hey, \n I Stuck On This Question While Playing Filmy Quiz \n Can you Please Help Me in Answer This Question Given Above?\n ::Download Filmy Quiz \nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(emailIntent1, "Share  To...."));


            } catch (Exception e) {
                Log.e("Image Exception", e.toString());
            }
        }

    }


    public void permission_Granted() {

        if (ContextCompat.checkSelfPermission(Emoji_Game_Screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(Emoji_Game_Screen.this, "You Have Already Granted This Permission!", Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("You need to allow permission to share questions to your friends else you can't!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Emoji_Game_Screen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storage_permission_code);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storage_permission_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == storage_permission_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        rewardedVideoAdd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        refer.setsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
        int coin_Value = (refer.getsharedpreference(Emoji_Game_Screen.this, getResources().getString(R.string.coin), 30));
        coin.setText("" + coin_Value);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (playRightWrongSound != null) {
                playRightWrongSound.stop();
                playRightWrongSound.reset();
                playRightWrongSound.release();
                playRightWrongSound = null;
            }
            if (buttonClickSound != null) {
                buttonClickSound.stop();
                buttonClickSound.reset();
                buttonClickSound.release();
                buttonClickSound = null;
            }
        } catch (Exception e) {

        }
    }
}

