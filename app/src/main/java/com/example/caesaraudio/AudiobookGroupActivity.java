package com.example.caesaraudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class AudiobookGroupActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private AudiobookAdapter audiobookAdapter;
    private List<Audiobook> audiobookGroup;
    private MediaPlayerManager mediaPlayerMangager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiobook_group);

        recyclerView = findViewById(R.id.chapterList);
        mediaPlayerMangager = MediaPlayerManager.getInstance(this);


        // Retrieve the audiobook group data
        audiobookGroup = (List<Audiobook>) getIntent().getSerializableExtra("audiobookGroup");

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        IndividualAudiobookAdapter individualAudiobookAdapter = new IndividualAudiobookAdapter(this, audiobookGroup);
        recyclerView.setAdapter(individualAudiobookAdapter);
    }
}
