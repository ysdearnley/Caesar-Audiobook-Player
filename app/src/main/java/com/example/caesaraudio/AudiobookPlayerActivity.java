package com.example.caesaraudio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caesaraudio.MediaPlayerManager;

public class AudiobookPlayerActivity extends AppCompatActivity {

    private TextView tvAudiobookTitle;
    private TextView tvAudiobookAuthor;
    private Button btnPlayPause, btnStop;
    private SeekBar seekBar;
    private MediaPlayerManager mediaPlayerManager;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiobook_player);

        tvAudiobookTitle = findViewById(R.id.tvAudiobookTitle);
        tvAudiobookAuthor = findViewById(R.id.tvAudiobookAuthor);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        seekBar = findViewById(R.id.seekBar);

        mediaPlayerManager = MediaPlayerManager.getInstance(this);


        // Retrieve the audiobook title from the intent
        String audiobookTitle = getIntent().getStringExtra("AudiobookTitle");
        if (audiobookTitle != null && !audiobookTitle.isEmpty()) {
            tvAudiobookTitle.setText(audiobookTitle);
        } else {
            tvAudiobookTitle.setText("Default Title"); // Set a default title if none is provided
        }

        // Retrieve the audiobook author from the intent
        String audiobookAuthor = getIntent().getStringExtra("AudiobookAuthor");
        if (audiobookAuthor != null && !audiobookAuthor.isEmpty()) {
            tvAudiobookAuthor.setText(audiobookAuthor);
        } else {
            tvAudiobookAuthor.setText("Default Author"); // Set a default title if none is provided
        }

        // Retrieve the audiobook file path and start playing
        String audiobookFilePath = getIntent().getStringExtra("AudiobookFilePath");
        if (audiobookFilePath != null) {
            mediaPlayerManager.stopMedia();
            mediaPlayerManager.playMedia(audiobookFilePath);
            isPlaying = true;
            btnPlayPause.setText("Pause");
        }


        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayerManager.pauseMedia();
                    btnPlayPause.setText("Play");
                } else {
                    mediaPlayerManager.resumeMedia();
                    btnPlayPause.setText("Pause");
                }
                isPlaying = !isPlaying;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.stopMedia();
                btnPlayPause.setText("Play");
                isPlaying = false;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerManager != null) {
            mediaPlayerManager.stopMedia(); // Ensures MediaPlayer is stopped before cleanup
            mediaPlayerManager.cleanup();
        }
    }
}