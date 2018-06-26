package com.goodwill.filmyquiz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodwill.dialod_classes.Alert_Dialog;
import com.goodwill.dialod_classes.Answer_Dialog;
import com.goodwill.dialod_classes.Get_Hints;
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
import java.util.Calendar;
import java.util.Locale;


public class Game_Screen extends AppCompatActivity implements RewardedVideoAdListener {
    private WrapperClass refer;
    private int storage_permission_code = 1;
    private Button optionA, optionB, optionC, optionD;
    private TextView display;
    private TextView heading;
    private TextView coin;
    private String category1;

    private Question question;
    private int currntLevel;
    private InterstitialAd interstitial;
    private MediaPlayer buttonClickSound;
    MediaPlayer playRightWrongSound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        refer = WrapperClass.getWrapperclass(this);
        currntLevel = refer.getsharedpreference(this, "CURRENT", 1);
        category1 = getIntent().getStringExtra("CATEGORY");
        addOnClickListener();
        initiateGameType();
        myAdd();
        rewardedVideoAdd();
        Intent intent = getIntent();
        int gameType = intent.getIntExtra("GameType", 0);
        Log.e("GameType", "" + gameType);
        intrestial();
    }


    private void playClickSound() {
        int clickSound = refer.getsharedpreference(this, "sound", 1);
        if (clickSound > 0) {

            try {
                if (buttonClickSound != null) {
                    buttonClickSound.stop();
                    buttonClickSound.reset();
                    buttonClickSound.release();

                }
            } catch (Exception ex) {

            }
            buttonClickSound = null;

            try {
                buttonClickSound = MediaPlayer.create(this, R.raw.on_click_sound);
            //    buttonClickSound.prepare();
                buttonClickSound.start();
            } catch (Exception ex) {
            }
        }
    }

    RewardedVideoAd mAd;

    private void rewardedVideoAdd() {

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd.loadAd("ca-app-pub-2556962058997611/4883247868", new AdRequest.Builder()
                .build());
    }

    private void myAdd() {


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-2556962058997611/2687495211");
        LinearLayout layout = (LinearLayout) findViewById(R.id.gameBannerAd);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("8B959D60BD22CFC945BA1DE5DD4121F9")
                .build();
        adView.loadAd(adRequest);
    }

    private void initiateGameType() {
        int gameType = getIntent().getIntExtra("GameType", 0);

        if (gameType == 1) {
            content_Bolly_dialouges();
            coinUpdate();
        } else if (gameType == 2) {
            content_Bolly_Pics();
            coinUpdate();
        } else {
            finish();
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
                }
            } catch (Exception ex) {
                Log.e("MediaPlayRight0", ex.toString());
            }
            playRightWrongSound = null;
            try {
                playRightWrongSound = MediaPlayer.create(this, rawFileName);
               // playRightWrongSound.prepare();
                playRightWrongSound.start();
            } catch (Exception ex) {
                Log.e("MediaPlayRight1", ex.toString());
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        int sound = refer.getsharedpreference(this, "music", 1);
        if (sound > 0) {

            refer.startSound(Game_Screen.this);
        } else {

            refer.stopSound(Game_Screen.this);

        }

        int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 0));
        Log.e("Coin value", "" + coin);
        if (coinValue > 0) {
            optionA.setClickable(true);
            optionB.setClickable(true);
            optionC.setClickable(true);
            optionD.setClickable(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        int sound = refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.music), 1);
        Log.e("sound at pause", "" + sound);
        if (sound > 0) {

            refer.stopSound(Game_Screen.this);
        }
    }

    private void addOnClickListener() {
        heading = (TextView) findViewById(R.id.heading);
        coin = (TextView) findViewById(R.id.coin);
        display = (TextView) findViewById(R.id.display);
        optionA = (Button) findViewById(R.id.optionA);
        optionB = (Button) findViewById(R.id.optionB);
        optionC = (Button) findViewById(R.id.optionC);
        optionD = (Button) findViewById(R.id.optionD);
        Button back = (Button) findViewById(R.id.back);
        display.setMovementMethod(new ScrollingMovementMethod());
        TextView get_hint_button = (TextView) findViewById(R.id.get_hint_button);

        get_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                new Get_Hints(Game_Screen.this).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                if (interstitial.isLoaded()) interstitial.show();

                Game_Screen.this.finish();
            }
        });


        int coinsValue = refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30);
        optionA.setOnClickListener(optionlistener);
        optionB.setOnClickListener(optionlistener);
        optionC.setOnClickListener(optionlistener);
        optionD.setOnClickListener(optionlistener);
        if (coinsValue == 0) {

            new Alert_Dialog(Game_Screen.this, category1).show();
            optionA.setClickable(false);
            optionB.setClickable(false);
            optionC.setClickable(false);
            optionD.setClickable(false);

        }

    }


    View.OnClickListener optionlistener = new View.OnClickListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View v) {
            Button opButton = (Button) v;
            int sound2 = refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.sound), 1);
            if ((opButton.getText()).equals(question.getAnswer())) {
                optionA.setClickable(false);
                optionB.setClickable(false);
                optionC.setClickable(false);
                optionD.setClickable(false);
                Toast.makeText(Game_Screen.this, String.valueOf("GREAT CORRECT ANSWER +10"), Toast.LENGTH_SHORT).show();
                Log.e("sound value", String.valueOf(sound2));
                playRightWrongSound(R.raw.beep_right);
                /*if (sound2 > 0) {
                if(rightMp!=null){
                    rightMp.stop();
                    rightMp.release();
                    rightMp=null;
                }

                    rightMp = MediaPlayer.create(Game_Screen.this, R.raw.beep_right);


                    rightMp.start();
                }*/

                Intent intent = getIntent();
                int gameType = intent.getIntExtra("GameType", 0);
                Log.e("GameTyp]e", "" + gameType);
                if (gameType == 1) {
                    refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.dialouge) + currntLevel), 1);
                }
                if (gameType == 2) {

                    refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.pic) + currntLevel), 1);

                }

                opButton.setBackgroundResource(R.drawable.green_button);
                refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.star)), refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.star)), 0) + 1);
                refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) + 10);
                int starValue = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.star)), 0);
                int levelValue = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.currentLevel)), 3);
                if (starValue == (levelValue * 2 + 1)) {
                    refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.currentLevel)), refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.currentLevel)), 3) + 1);
                    Toast.makeText(Game_Screen.this, String.valueOf(" NEW LEVEL UNLOCK"), Toast.LENGTH_SHORT).show();
                }
                int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coinValue);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Game_Screen.this.finish();
                    }
                }, 1000);
            } else {

                playRightWrongSound(R.raw.beep_wrong);

                //wrongMp.start();

                Intent intent = getIntent();
                int gameType = intent.getIntExtra("GameType", 0);
                Log.e("GameTyp]e", "" + gameType);
                if (gameType == 1) {
                    refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.dialouge) + currntLevel), 0);
                }
                if (gameType == 2) {

                    refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.pic) + currntLevel), 0);

                }
                Toast.makeText(Game_Screen.this, String.valueOf("oops TRY AGAIN! -10"), Toast.LENGTH_SHORT).show();

                refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) - 10);
                int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coinValue);
                opButton.setBackgroundResource(R.drawable.red_button);

                if (coinValue < 10) {
                    refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
                    coin.setText("0");
                    Log.e("the value of coinDec", String.valueOf(coinValue));
                    optionA.setClickable(false);
                    optionB.setClickable(false);
                    optionC.setClickable(false);
                    optionD.setClickable(false);
                    new Alert_Dialog(Game_Screen.this, category1).show();
                } else {
                    optionA.setClickable(true);
                    optionB.setClickable(true);
                    optionC.setClickable(true);
                    optionD.setClickable(true);
                }

                //Toast.makeText(Game_Screen.this, String.valueOf("WRONG ANSWER"), Toast.LENGTH_SHORT).show();
            }
        }

    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if (interstitial.isLoaded()) interstitial.show();
        this.finish();
    }

    private void intrestial() {

        interstitial = new InterstitialAd(this);

        interstitial.setAdUnitId("ca-app-pub-2556962058997611/6962616293");

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);


    }

    public void coinUpdate() {
        int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
        coin.setText("" + coinValue);

    }

    public void content_Bolly_dialouges() {
                question = ReadFromDatabase.readFromDatabaseQuotes(currntLevel, this, category1 + ("quote_db.txt"));
        Log.e("currentlevel inPicture", String.valueOf(currntLevel));
        if (question != null) {
            optionA.setText(question.getOpA());
            optionB.setText(question.getOpB());
            optionC.setText(question.getOpC());
            optionD.setText(question.getOpD());

            optionA.setTag(question.getOpA());
            optionB.setTag(question.getOpB());
            optionC.setTag(question.getOpC());
            optionD.setTag(question.getOpD());

            display.setText(question.getDisplay());
            heading.setText(question.getQuestion());

        }


        int dialouge = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.dialouge) + currntLevel), 0);

        if (dialouge > 0) {
            optionA.setOnClickListener(null);
            optionB.setOnClickListener(null);
            optionC.setOnClickListener(null);
            optionD.setOnClickListener(null);

            LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
            View v2 = mainlayout.findViewWithTag(question.getAnswer());
            v2.setBackgroundResource(R.drawable.green_button);
            Log.e("tagv2", v2.getTag().toString());
        }
    }


    public void content_Bolly_Pics() {

        question = ReadFromDatabase.readFromDatabasePicture(currntLevel, this, category1 + ("pics_db.txt"));
        int id = getResources().getIdentifier(question.getPicname(), "drawable", getPackageName());

        if (question != null) {
            heading.setText(question.getQuestion());
            display.setBackgroundResource(id);
            optionA.setText(question.getOpA());
            optionB.setText(question.getOpB());
            optionC.setText(question.getOpC());
            optionD.setText(question.getOpD());
            optionA.setTag(question.getOpA());
            optionB.setTag(question.getOpB());
            optionC.setTag(question.getOpC());
            optionD.setTag(question.getOpD());

            int picture = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.pic) + currntLevel), 0);
            Log.e("pic value", String.valueOf(picture));
            if (picture > 0) {
                optionA.setOnClickListener(null);
                optionB.setOnClickListener(null);
                optionC.setOnClickListener(null);
                optionD.setOnClickListener(null);

                LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
                View v3 = mainlayout.findViewWithTag(question.getAnswer());
                v3.setBackgroundResource(R.drawable.green_button);

            }


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
        playClickSound();
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
                emailIntent1.putExtra(Intent.EXTRA_TEXT, "Hey, \n I Stuck On This Question While Playing Filmy Quiz \n Can you Please Help Me in Answer This Question Given Above!?\n \n::Download Filmy Quiz \nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(emailIntent1, "Share  To...."));


            } catch (Exception e) {
                Log.e("Image Exception", e.toString());
            }
        }

    }

    public void hint1() {
        playClickSound();
        int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
        if (coinValue >= 10) {
            refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) - 10);
            int coin_Value = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coin_Value);
            new Answer_Dialog(Game_Screen.this, question.getHint1()).show();
        }

        if (coinValue < 10) {
           /* refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
            coin.setText("0");*/
            new Alert_Dialog(Game_Screen.this, category1).show();
        }
        if (coinValue < 4) {
            refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) * 0);
            coin.setText("0");
        }

    }

    public void hint2() {
        playClickSound();
        int coinsValue = refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30);
        Log.e("coinValueee", String.valueOf(coinsValue));
        if (coinsValue < 30) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Alert_Dialog(Game_Screen.this, category1).show();
                }
            }, 200);
        } else {
            refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) - 30);
            int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coinValue);

            String a = String.valueOf(optionA.getText());
            String b = String.valueOf(optionB.getText());
            String c = String.valueOf(optionC.getText());
            String d = String.valueOf(optionD.getText());

            if (a.equals(question.getAnswer())) {
                optionB.setVisibility(View.INVISIBLE);
                optionC.setVisibility(View.INVISIBLE);
            }
            if (b.equals(question.getAnswer())) {
                optionA.setVisibility(View.INVISIBLE);
                optionD.setVisibility(View.INVISIBLE);
            }
            if (c.equals(question.getAnswer())) {
                optionA.setVisibility(View.INVISIBLE);
                optionD.setVisibility(View.INVISIBLE);
            }
            if (d.equals(question.getAnswer())) {
                optionB.setVisibility(View.INVISIBLE);
                optionC.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void show_Answer() {
        playClickSound();
        int coinsValue = refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30);
        if (coinsValue < 50) {

            new Alert_Dialog(Game_Screen.this, category1).show();

        } else {
            refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) - 50);
            int coinValue = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coinValue);
            new Answer_Dialog(Game_Screen.this, question.getAnswer()).show();
        }
    }

    int RATE_REQUEST_CODE = 12;

    public void rateUS() {
        playClickSound();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivityForResult(intent, RATE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RATE_REQUEST_CODE) {
            refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
            int coin_Value = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
            coin.setText("" + coin_Value);

            refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.clicked)), 1);
            int coinValue = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.clicked)), 0);
            Log.e("the value of rate us", String.valueOf(coinValue));
            if (coinValue > 0) {
                Log.e("the value of rate us af", String.valueOf(coinValue));
                findViewById(R.id.rate_us_layout).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void like_over_facebook() {
        playClickSound();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.facebook.com/goodwilltechnologyservices/"));
        startActivity(intent);
        refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int coin_Value = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coin_Value);
            }
        }, 2000);
        refer.setsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.clicked1)), 1);
        int coinValue = refer.getsharedpreference(Game_Screen.this, category1 + (getResources().getString(R.string.clicked1)), 0);
        Log.e("the value facebook like", String.valueOf(coinValue));
        if (coinValue > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.facebook_like_layout).setVisibility(View.GONE);
                }
            }, 3000);
        }
    }

    public void share_to_friend() {
        playClickSound();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, \n I Am Playing This Very Challenging \n 3 in 1 Filmy Quiz its Really AMAZING!\n Play For Free on   \n https://play.google.com/store/apps/details?id=com.example.lenovo.quotes");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this site!");
        startActivity(Intent.createChooser(intent, "Share"));
        refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) + 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int coin_Value = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
                coin.setText("" + coin_Value);
            }
        }, 5000);
    }

    public void watch_video() {

        playClickSound();
        if (mAd.isLoaded()) {
            mAd.show();
        } else {
            Toast.makeText(this, "Unable to load Video", Toast.LENGTH_SHORT).show();
        }
    }

    public void permission_Granted() {

        if (!(ContextCompat.checkSelfPermission(Game_Screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
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
                            ActivityCompat.requestPermissions(Game_Screen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storage_permission_code);
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
        refer.setsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30) + 30);
        int coin_Value = (refer.getsharedpreference(Game_Screen.this, getResources().getString(R.string.coin), 30));
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






