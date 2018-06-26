package com.goodwill.filmyquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.goodwill.helper.WrapperClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class HomeScreen extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private WrapperClass refer;
    private int storage_permission_code = 1;
    private View setting_popup;
    boolean isExist = false;
    private MediaPlayer buttonClickSound;

    private InterstitialAd interstitial;

    boolean isPopUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);
        refer = WrapperClass.getWrapperclass(this);
        addListenerOnButton();
    }

    private void interstitial() {

        interstitial = new InterstitialAd(this);

        interstitial.setAdUnitId("ca-app-pub-2556962058997611/2093432992");
        AdRequest adRequest = new AdRequest.Builder().build();

        interstitial.loadAd(adRequest);
    }

    private void myAdd() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-2556962058997611/5521132999");
        LinearLayout layout = (LinearLayout) findViewById(R.id.homeBannerAd);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

    }


    @Override
    protected void onResume() {
        super.onResume();

        permission_Granted();
        Switch sound_switch = (Switch) findViewById(R.id.sound_switch);
        Switch music_switch = (Switch) findViewById(R.id.music_switch);

        sound_switch.setOnCheckedChangeListener(HomeScreen.this);
        music_switch.setOnCheckedChangeListener(HomeScreen.this);


        myAdd();
        interstitial();


        int sound = refer.getsharedpreference(this, "music", 1);

        Log.e("sound at resume", "" + sound);

        if (sound > 0) {
            music_switch.setChecked(true);

            refer.startSound(HomeScreen.this);
        } else {
            music_switch.setChecked(false);
            refer.stopSound(HomeScreen.this);
        }


        int clickSound = refer.getsharedpreference(this, "sound", 1);
      //  buttonClickSound = MediaPlayer.create(this, R.raw.on_click_sound);
        if (clickSound > 0) {
            refer.setsharedpreference(this, "sound", 1);
            sound_switch.setChecked(true);

        } else {
            sound_switch.setChecked(false);

        }


    }


    private void playClickSound(){
        int clickSound = refer.getsharedpreference(this, "sound", 1);
        if(clickSound>0){
            try{
                if(buttonClickSound!=null){
                    buttonClickSound.stop();
                    buttonClickSound.reset();
                    buttonClickSound.release();
                }
            }catch (Exception ex){

            }
            buttonClickSound=null;

            buttonClickSound = MediaPlayer.create(this, R.raw.on_click_sound);
            buttonClickSound.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        int sound = refer.getsharedpreference(HomeScreen.this, getResources().getString(R.string.music), 1);
        Log.e("sound at pause", "" + sound);
        if (sound > 0) {

            refer.stopSound(HomeScreen.this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.sound_switch) {
            if (isChecked) {
                refer.setsharedpreference(HomeScreen.this, "sound", 1);

            } else {
                refer.setsharedpreference(HomeScreen.this, "sound", 0);

            }
        }

        if (buttonView.getId() == R.id.music_switch) {
            Log.e("Switch State=", "" + isChecked);
            if (isChecked) {
                refer.setsharedpreference(HomeScreen.this, "music", 1);
                refer.startSound(HomeScreen.this);
            } else {
                refer.setsharedpreference(HomeScreen.this, "music", 0);
                refer.stopSound(HomeScreen.this);
                //game_bg = null;
            }
        }
    }


   /* private void startSound() {

        Intent svc = new Intent(this, BackgroundSoundService.class);
        startService(svc);
    }
*/

   /* private void stopSound() {


        Intent svc = new Intent(this, BackgroundSoundService.class);
        stopService(svc);
    }*/

    public void addListenerOnButton() {
        Button buttonHollywoodQuiz = (Button) findViewById(R.id.hollywoodquiz);
        Button buttonBollywoodQuiz = (Button) findViewById(R.id.bollywoodquiz);
        Button setting = (Button) findViewById(R.id.setting);
        Button cross = (Button) findViewById(R.id.cross);

        Button facebook_like = (Button) findViewById(R.id.facebook_like);
        Button rate_us = (Button) findViewById(R.id.rate_us);
        Button share = (Button) findViewById(R.id.share);
        Button help = (Button) findViewById(R.id.help);
        Button help_cross=(Button)findViewById(R.id.helpCross);
        setting_popup = findViewById(R.id.setting_popup);

        buttonHollywoodQuiz.setOnClickListener(myListener);
        buttonBollywoodQuiz.setOnClickListener(myListener);
        setting.setOnClickListener(myListener);
        cross.setOnClickListener(myListener);
        share.setOnClickListener(myListener);
        rate_us.setOnClickListener(myListener);
        facebook_like.setOnClickListener(myListener);
        help.setOnClickListener(myListener);
        help_cross.setOnClickListener(myListener);

    }

    View.OnClickListener  myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playClickSound();
            switch (v.getId()) {
                case R.id.hollywoodquiz: {

                    if(isPopUp)break;
                    Intent myIntent = new Intent(HomeScreen.this,
                            Levels.class);
                    myIntent.putExtra("CATEGORY", "holly_");
                    startActivity(myIntent);
                }
                break;
                case R.id.bollywoodquiz: {
                    if(isPopUp)break;
                    Intent myIntent = new Intent(HomeScreen.this,
                            Levels.class);
                    myIntent.putExtra("CATEGORY", "bolly_");
                    startActivity(myIntent);

                }
                break;

                case R.id.setting: {
                    setting_popup.setVisibility(View.VISIBLE);
                    isPopUp=true;
                }
                break;
                case R.id.cross: {
                    setting_popup.setVisibility(View.GONE);
                    isPopUp=false;
                }
                break;
                case R.id.facebook_like: {
                    setting_popup.setVisibility(View.GONE);
                    isPopUp=false;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.facebook.com/goodwilltechnologyservices/"));
                    startActivity(intent);
                }
                break;
                case R.id.share: {
                    setting_popup.setVisibility(View.GONE);
                    isPopUp=false;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey, \n I Am Playing This Very Challenging \n 3 in 1 Filmy Quiz its Really AMAZING!\n Play For Free on   \n https://play.google.com/store/apps/details?id=" + getPackageName());
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this site!");
                    startActivity(Intent.createChooser(intent, "Share"));

                }
                break;
                case R.id.help: {
                    isPopUp=false;
                    setting_popup.setVisibility(View.GONE);
                    //               setting_popup.setVisibility(View.GONE);
                               View  help_popup=(View)findViewById(R.id.help_popup);
                                 help_popup.setVisibility(View.VISIBLE);

                    WebView view=(WebView)findViewById(R.id.pic_view);
                    String text;
                    text ="<html><body style='background-color:#1F73CE;opacity:.95;'><p style='text-align:justify;color:#ff0;'>";
                    text+="In this category, you will get the picture of the most popular movie scene and related question to that movie. You get four different options to answer from which you have to select correct answer for that question. For correct answer, you will get 10 points and 10 points will be deducted if the selected answer is not correct.";
                    text+="</p></body></html>";
                    view.loadData(text,"text/html","utf-8");

                    WebView view1=(WebView)findViewById(R.id.quote_view);
                    String text1;
                    text1 ="<html><body style='background-color:#1F73CE;opacity:.95;'><p style='text-align:justify;color:#ff0;'>";
                    text1+="In this category, you will get the famous quote of the Actor/Actress from the movie and related question to that movie. You will get four different options to answer from which you have to select correct answer for that question. For correct answer, you will get 10 points and 10 points will be deducted if the selected answer is not correct.";
                    text1+="</p></body></html>";
                    view1.loadData(text1,"text/html","utf-8");

                    WebView view2=(WebView)findViewById(R.id.emoji_view);
                    String text2;
                    text2 ="<html><body style='background-color:#1F73CE;opacity:.95;'><p style='text-align:justify;color:#ff0;'>";
                    text2+="In this category, you will get the famous quote of the Actor/Actress from the movie and related question to that movie. You will get four different options to answer from which you have to select correct answer for that question. For correct answer, you will get 10 points and 10 points will be deducted if the selected answer is not correct.";
                    text2+="</p></body></html>";
                    view2.loadData(text,"text/html","utf-8");
                }
                break;
                case R.id.helpCross: {
                    isPopUp=false;
                    View  help_popup=(View)findViewById(R.id.help_popup);
                    help_popup.setVisibility(View.GONE);

                }
                break;
                case R.id.rate_us: {
                    setting_popup.setVisibility(View.GONE);
                    isPopUp=false;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(intent);

                }
                break;

            }
        }
    };


    @Override
    public void onBackPressed() {
        if(isPopUp){
            setting_popup.setVisibility(View.GONE);
            isPopUp=false;
            return;
        }

        if (!isExist) {
            Toast.makeText(HomeScreen.this, String.valueOf("Do You Really Want To Exit"), Toast.LENGTH_LONG).show();
            isExist = true;
        } else {
            // super.onBackPressed();
            if (interstitial.isLoaded()) {
                interstitial.show();
            }
            HomeScreen.this.finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isExist = false;
            }
        }, 4000);
    }

    public void permission_Granted() {

        if (!(ContextCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
    protected void onDestroy() {
        super.onDestroy();
        try{

            if(buttonClickSound!=null){
                buttonClickSound.stop();
                buttonClickSound.reset();
                buttonClickSound.release();
                buttonClickSound=null;
            }
        }catch (Exception e){

        }
    }
}




