package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView duration1, duration2;
    TextView textView;
    ImageView btMedia, btBw, btFw;
    ImageView btNext, btPrevious;
    SeekBar seekBar;

    ArrayList songs;
    int position;
    String textContent;
    Thread updateSeek;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);
        duration1 = findViewById(R.id.duration1);
        duration2 = findViewById(R.id.duration2);
        btMedia = findViewById(R.id.bt_media);
        btBw = findViewById(R.id.bt_bw);
        btFw = findViewById(R.id.bt_fw);
        btNext = findViewById(R.id.bt_next);
        btPrevious = findViewById(R.id.bt_previous);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        position = intent.getIntExtra("position", 0);

        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        duration2.setText(sDuration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        btMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    btMedia.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else {
                    btMedia.setImageResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        btFw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    duration1.setText(convertFormat(currentPosition));
                    duration2.setText(convertFormat(duration));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btBw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    duration1.setText(convertFormat(currentPosition));
                    duration2.setText(convertFormat(duration));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                textContent = songs.get(position).toString();
                textView.setText(textContent);
                mediaPlayer.start();
                btMedia.setImageResource(R.drawable.ic_pause);
            }
        });

        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position != 0){
                    position = position - 1;
                }else{
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                btMedia.setImageResource(R.drawable.ic_pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).toString();
                textView.setText(textContent);
            }
        });

    }
    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration)  {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

}