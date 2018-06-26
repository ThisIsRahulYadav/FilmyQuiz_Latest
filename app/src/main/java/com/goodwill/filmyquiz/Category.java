package com.goodwill.filmyquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodwill.helper.Question;
import com.goodwill.helper.ReadFromDatabase;
import com.goodwill.helper.WrapperClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jackandphantom.blurimage.BlurImage;

import static android.view.Gravity.CENTER;

public class Category extends AppCompatActivity {

    private WrapperClass refer;

    private Button star_pic;
    private Button star_quote;
    private Button star_emoji;
//    private MediaPlayer rightMp;
    private ImageView halfbtn;
    private String category1;
    int currntLevel = 1;
    private MediaPlayer buttonClickSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);
        refer = WrapperClass.getWrapperclass(this);
        category1 = getIntent().getStringExtra("CATEGORY");
        addOnClickListener();
        level();
        star_count();
        // refer.setsharedpreference(Levels.this,"CURRENT",Integer.parseInt(v.getTag().toString()));
        currntLevel = refer.getsharedpreference(this, "CURRENT", 1);
        Question question = ReadFromDatabase.readFromDatabaseEmoji(currntLevel, this, category1 + ("emoji_db.txt"));
        if (question != null) {

            String pic = question.getPicname();
            Log.e("pic name", pic);
            String[] r = pic.split(",");
            emoji_Button(r);
        }
        myAdd();
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

    private void myAdd() {


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-2556962058997611/2798766730");
        LinearLayout layout = (LinearLayout) findViewById(R.id.categoryBannerAd);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        int sound = refer.getsharedpreference(this, "music", 1);
        if (sound > 0) {
            refer.startSound(Category.this);
        } else {

            refer.stopSound(Category.this);

        }



        halfbtn.setVisibility(View.VISIBLE);
        star_pic.setBackgroundResource(R.drawable.star_silver);
//        rightMp = MediaPlayer.create(this, R.raw.on_click_sound);

        int picValue = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.pic) + currntLevel), 0);


        Log.e("picValue", "" + picValue);
        if (picValue == 1) {
            halfbtn.setVisibility(View.GONE);
            star_pic.setBackgroundResource(R.drawable.star_golden);
        } else {
            halfbtn.setVisibility(View.VISIBLE);
        }

        star_quote.setBackgroundResource(R.drawable.star_silver);
        int quote_value = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.dialouge) + currntLevel), 0);

        Log.e("quote_value", "" + quote_value);
        if (quote_value == 1) {
            star_quote.setBackgroundResource(R.drawable.star_golden);
        }

        star_emoji.setBackgroundResource(R.drawable.star_silver);
        int emoji_value = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.emoji) + currntLevel), 0);
        if (emoji_value == 1) {
            star_emoji.setBackgroundResource(R.drawable.star_golden);
        }


        level();
        star_count();
        category_Updater();
        findViewById(R.id.nxtbutton).setClickable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int sound = refer.getsharedpreference(Category.this, getResources().getString(R.string.music), 1);
        Log.e("sound at pause", "" + sound);
        if (sound > 0) {

            refer.stopSound(Category.this);
        }
    }

    private void addOnClickListener() {
        TextView picbutton = (TextView) findViewById(R.id.picbutton);
        Button dialougebutton = (Button) findViewById(R.id.dialougebutton);
        LinearLayout emojibutton = (LinearLayout) findViewById(R.id.emojibutton);
        Button prvbutton = (Button) findViewById(R.id.prvbutton);
        Button nxtbutton = (Button) findViewById(R.id.nxtbutton);
        Button back = (Button) findViewById(R.id.back);
        halfbtn = (ImageView) findViewById(R.id.halfbtn);
        star_pic = (Button) findViewById(R.id.star_pic);
        star_quote = (Button) findViewById(R.id.star_quote);
        star_emoji = (Button) findViewById(R.id.star_emoji);


        picbutton.setOnClickListener(categorylistener);
        dialougebutton.setOnClickListener(categorylistener);
        emojibutton.setOnClickListener(categorylistener);
        prvbutton.setOnClickListener(categorylistener);
        nxtbutton.setOnClickListener(categorylistener);
        back.setOnClickListener(categorylistener);
        halfbtn.setOnClickListener(categorylistener);

    }


    View.OnClickListener categorylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button nxt = (Button) findViewById(R.id.nxtbutton);

            playClickSound();
            switch (v.getId()) {

                case R.id.picbutton: {
                    Intent myIntent = new Intent(Category.this, Game_Screen.class);
                    myIntent.putExtra("CATEGORY", category1);
                    myIntent.putExtra("GameType", 2);
                    startActivity(myIntent);


                }
                break;
                case R.id.dialougebutton: {
                    Intent myIntent = new Intent(Category.this, Game_Screen.class);
                    myIntent.putExtra("CATEGORY", category1);
                    myIntent.putExtra("GameType", 1);
                    startActivity(myIntent);

                }
                break;
                case R.id.emojibutton: {
                    Intent myIntent = new Intent(Category.this, Emoji_Game_Screen.class);
                    myIntent.putExtra("CATEGORY", category1);
                    startActivity(myIntent);

                }
                break;
                case R.id.prvbutton: {
                    nxt.setClickable(true);
                    int i = currntLevel - 1;

                    if (i < 1) {
                        Toast.makeText(Category.this, String.valueOf("NO MORE PREVIOUS LEVEL"), Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        currntLevel -= 1;
                    }
                    refer.setsharedpreference(Category.this, "CURRENT", i);
                    //   refer.setCurrentlevel(i);
                    // blur_image();
                    level();
                    category_Updater();
                }
                break;
                case R.id.nxtbutton: {
                    int i = currntLevel + 1;

                    if (i > 200){
                        Toast.makeText(Category.this,"No more Level Left",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        currntLevel += 1;
                    }
                    refer.setsharedpreference(Category.this, "CURRENT", i);

                    level();
                    category_Updater();
                    int currentLevel = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.currentLevel)), 3);
                    if (i == currentLevel) {
//                        nxt.setClickable(false);
                        Toast.makeText(Category.this, String.valueOf("LEVEL IS LOCKED"), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case R.id.back: {
                    Intent intent = new Intent(Category.this, Levels.class);
                    intent.putExtra("CATEGORY", category1);
                    startActivity(intent);
                    Category.this.finish();
                }
                break;
                case R.id.halfbtn: {
                    Intent myIntent = new Intent(Category.this, Game_Screen.class);
                    myIntent.putExtra("CATEGORY", category1);
                    myIntent.putExtra("GameType", 2);
                    startActivity(myIntent);
                }
                break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Category.this, Levels.class);
        intent.putExtra("CATEGORY", category1);
        startActivity(intent);
        this.finish();
    }

    @SuppressLint("SetTextI18n")
    public void star_count() {
        int star = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.star)), 0);
        TextView starCount = (TextView) findViewById(R.id.starCount_cat);
        starCount.setText(star + "/900");
        Log.e("star value in category", String.valueOf(star));
    }

    @SuppressLint("SetTextI18n")
    public void level() {
        TextView level = (TextView) findViewById(R.id.level);
        level.setText("LEVEL " + currntLevel);
        Log.e("current level", "" + currntLevel);
        Question question = ReadFromDatabase.readFromDatabaseQuotes(currntLevel, this, category1 + ("quote_db.txt"));
        if (question != null) {
//            assert question != null;
            Button dialougebutton = (Button) findViewById(R.id.dialougebutton);
            dialougebutton.setText(question.getDisplay());
            question = ReadFromDatabase.readFromDatabasePicture(currntLevel, this, category1 + ("pics_db.txt"));
            int pic_bg = getResources().getIdentifier(question.getPicname(), "drawable", getPackageName());
            findViewById(R.id.picbutton).setBackgroundResource(pic_bg);
            Log.e("pic nameee", question.getPicname());
            blur_image(halfbtn, question);
        }
    }

    public void emoji_Button(final String[] pic_name) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout emojiButton = (LinearLayout) findViewById(R.id.emojibutton);
                int cellWidth = getResources().getDisplayMetrics().widthPixels / 10;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cellWidth, cellWidth);
                emojiButton.removeAllViews();
                layoutParams.setMargins(1, 1, 1, 1);
                for (int i = 0; i < pic_name.length; i++) {
                    TextView view = new TextView(Category.this);
                    view.setGravity(CENTER);
                    view.setLayoutParams(layoutParams);
                    int resourceId = Category.this.getResources().getIdentifier(pic_name[i], "drawable", getPackageName());
                    view.setBackgroundResource(resourceId);
                    emojiButton.addView(view);
                }
            }
        }, 300);
    }

    public void blur_image(ImageView view, Question question) {
        BlurImage.with(getApplicationContext()).load(getResources().getIdentifier(question.getPicname(), "drawable", getPackageName())).intensity(20).Async(true).into(view);
    }

    public void category_Updater() {
        halfbtn.setVisibility(View.VISIBLE);
        star_pic.setBackgroundResource(R.drawable.star_silver);
        int picValue = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.pic) + currntLevel), 0);
        if (picValue > 0) {
            halfbtn.setVisibility(View.GONE);
            star_pic.setBackgroundResource(R.drawable.star_golden);
        }

        star_quote.setBackgroundResource(R.drawable.star_silver);
        int quote_value = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.dialouge) + currntLevel), 0);
        if (quote_value == 1) {
            star_quote.setBackgroundResource(R.drawable.star_golden);
        }

        star_emoji.setBackgroundResource(R.drawable.star_silver);
        int emoji_value = refer.getsharedpreference(Category.this, category1 + (getResources().getString(R.string.emoji) + currntLevel), 0);
        if (emoji_value == 1) {
            star_emoji.setBackgroundResource(R.drawable.star_golden);
        }
        Question question = ReadFromDatabase.readFromDatabaseEmoji(currntLevel, Category.this, category1 + ("emoji_db.txt"));
        String pic = question.getPicname();
        String[] r = pic.split(",");
        emoji_Button(r);
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

