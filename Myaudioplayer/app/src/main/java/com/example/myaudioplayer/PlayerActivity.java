package com.example.myaudioplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.myaudioplayer.AlbumDetailsAdapter.albumFiles;
import static com.example.myaudioplayer.MainActivity.musicFiles;
import static com.example.myaudioplayer.MainActivity.repeatBoolean;
import static com.example.myaudioplayer.MainActivity.shuffleBoolean;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView songName, songArtist, durationPlayed, songDuration;
    ImageView coverArt, nextBtn, prevBtn, shuffleBtn, repeatBtn, backBtn;
    FloatingActionButton playBtn;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<MusicFiles> songsList;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, nextThread, prevThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        songName.setText(songsList.get(position).getTitle());
        songArtist.setText(songsList.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean)
                {
                    shuffleBoolean=false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_off);

                }
                else
                {
                    shuffleBoolean=true;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_on);
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatBoolean)
                {
                    repeatBoolean=false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_off);
                }
                else
                {
                    repeatBoolean=true;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_on);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean&&!repeatBoolean)
            {
                position=getRandom(songsList.size()-1);
            }

            else if(!shuffleBoolean&&!repeatBoolean){
                position = ((position - 1) < 0 ? (songsList.size()-1):(position - 1));
            }

            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            songArtist.setText(songsList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()  / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
        } else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean&&!repeatBoolean)
            {
                position=getRandom(songsList.size()-1);
            }

            else if(!shuffleBoolean&&!repeatBoolean) {
                position = ((position - 1) < 0 ? (songsList.size() - 1) : (position - 1));
            }
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            songArtist.setText(songsList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()  / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_baseline_play);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean&&!repeatBoolean)
            {
                position=getRandom(songsList.size()-1);
            }

            else if(!shuffleBoolean&&!repeatBoolean){
                position = ((position + 1) % songsList.size());
            }
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            songArtist.setText(songsList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()  / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
        } else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean&&!repeatBoolean)
            {
                position=getRandom(songsList.size()-1);
            }

            else if(!shuffleBoolean&&!repeatBoolean){
                position = ((position + 1) % songsList.size());
            }

            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            songArtist.setText(songsList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()  / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_baseline_play);
        }
    }

    private int getRandom(int i) {
        Random random=new Random();

        return random.nextInt(i+1);
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playBtnClicked() {
         if(mediaPlayer.isPlaying()){
             playBtn.setImageResource(R.drawable.ic_baseline_play);
             mediaPlayer.pause();
             seekBar.setMax(mediaPlayer.getDuration()  / 1000);
             PlayerActivity.this.runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     if(mediaPlayer != null){
                         int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                         seekBar.setProgress(mCurrentPosition);
                     }
                     handler.postDelayed(this, 1000);
                 }
             });
         } else{
             playBtn.setImageResource(R.drawable.ic_baseline_pause);
             mediaPlayer.start();
             seekBar.setMax(mediaPlayer.getDuration() / 1000);
             PlayerActivity.this.runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     if(mediaPlayer != null){
                         int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                         seekBar.setProgress(mCurrentPosition);
                         durationPlayed.setText(formattedTime(mCurrentPosition));
                     }
                     handler.postDelayed(this, 1000);
                 }
             });

         }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalNew = minutes + ":0" + seconds;
        totalOut = minutes + ":" + seconds;
        if(seconds.length() == 1){
            return totalNew;
        } else{
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("positon",-1);
        songsList = musicFiles;
        String sender=getIntent().getStringExtra("sender");
        if(sender!=null&&sender.equals("albumDetails"))
        {
            songsList=albumFiles;

        }
        else
        {
            songsList=musicFiles;
        }
        if(songsList != null){
            playBtn.setImageResource(R.drawable.ic_baseline_pause);
            uri = Uri.parse(songsList.get(position).getPath());
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        } else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void initViews() {
        songName = findViewById(R.id.songTitle);
        songArtist = findViewById(R.id.songArtist);
        songDuration = findViewById(R.id.songDuration);
        durationPlayed = findViewById(R.id.durationPlayed);

        coverArt = findViewById(R.id.coverArt);
        nextBtn = findViewById(R.id.skipNxtBtn);
        prevBtn = findViewById(R.id.skipPrevBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        repeatBtn = findViewById(R.id.repeatBtn);
        backBtn = findViewById(R.id.backBtn);

        playBtn = findViewById(R.id.playBtn);
        seekBar = findViewById(R.id.seekBar);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(songsList.get(position).getDuration()) / 1000;
        songDuration.setText(formattedTime(durationTotal));
        byte[] art =retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art!=null) {
            Glide.with(this).asBitmap().load(R.drawable.music_icon).into(coverArt);
            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
                    if(swatch !=null)
                    {
                      ImageView gredient =findViewById(R.id.coverGradient);
                        RelativeLayout mContainer =findViewById(R.id.mContainer);
                        gredient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP ,
                                new int[]{swatch.getRgb(),0x00000000});
                        gredient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP ,
                                new int[]{swatch.getRgb(),swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBg);
                        songName.setTextColor(swatch.getTitleTextColor());
                        songArtist.setTextColor(swatch.getBodyTextColor());

                    }
                    else
                    {
                        ImageView gredient =findViewById(R.id.coverGradient);
                        RelativeLayout mContainer =findViewById(R.id.mContainer);
                        gredient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP ,
                                new int[]{0xff000000,0x00000000});
                        gredient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP ,
                                new int[]{0xff000000,0xff000000});
                        mContainer.setBackground(gradientDrawableBg);
                        songName.setTextColor(Color.WHITE);
                        songArtist.setTextColor(Color.DKGRAY);
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if(mediaPlayer != null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}