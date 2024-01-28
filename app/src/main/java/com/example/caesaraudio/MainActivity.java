package com.example.caesaraudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String AUDIOS_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/audiobooks";

    public static final int PERMISSION_REQ_CODE = 7;

    private static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    private AudiobookDBHelper audiobookDatabaseHelper;

    private List<Audiobook> audiobooks;
    private RecyclerView recyclerView;
    private AudiobookAdapter audiobookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        audiobookDatabaseHelper = new AudiobookDBHelper(this);
        audiobookDatabaseHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.libraryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the adapter with an empty list and set it to the RecyclerView
        audiobookAdapter = new AudiobookAdapter(this);
        recyclerView.setAdapter(audiobookAdapter);



        requestRuntimePermissions();


    }

    private void requestRuntimePermissions() {
        if (ActivityCompat.checkSelfPermission(this, PERMISSION_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission has been granted!", Toast.LENGTH_SHORT).show();

            File audiobooksDirectory = new File(AUDIOS_DIRECTORY);
            if (!audiobooksDirectory.exists()) {
                audiobooksDirectory.mkdirs(); // Create the directory if it doesn't exist
            }
            scanAudiobooks();
            loadAudiobooks();
        }

        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires READ_EXTERNAL_STORAGE permission to work")
                    .setTitle("Permission Require").setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                File audiobooksDirectory = new File(AUDIOS_DIRECTORY);

                if (!audiobooksDirectory.exists()) {
                    audiobooksDirectory.mkdirs(); // Create the directory directories if it  doesn't exist
                }

                scanAudiobooks();
                loadAudiobooks();

            }
        }
        //else{
        //
        //}
    }


    private void scanAudiobooks() {
        String filepath = null;

        File audiobooksDirectory = new File(AUDIOS_DIRECTORY);
        Audiobook processedAudiobookObject = null;

        if (audiobooksDirectory.exists() && audiobooksDirectory.isDirectory()) {
            File[] audiobookFiles = audiobooksDirectory.listFiles();

            if (audiobookFiles != null && audiobookFiles.length > 0) {
                for (File audiobook : audiobookFiles) {
                    filepath = audiobook.getAbsolutePath();

                    processedAudiobookObject = processAudiobook(audiobook);
                    if(processedAudiobookObject != null && !audiobookDatabaseHelper.isAudiobookInLibrary(processedAudiobookObject.getFilepath())){
                        audiobookDatabaseHelper.insertAudiobook(processedAudiobookObject);
                    }

                }
            }
        } else {
            Toast.makeText(this, "Audiobooks directory not found  is not a directory", Toast.LENGTH_SHORT).show();
        }
    }

    private Audiobook processAudiobook(File audiobookFile) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(audiobookFile.getAbsolutePath());

            // Extracting metadata
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String book = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

            // Handle missing or null values
            if (title == null) {
                title = "Unknown Title";
            }

            if (author == null) {
                author = "Unknown Author";
            }
            if(book == null){
                book = "Unknown Book";
            }

            int durationSeconds = 0;
            if (duration != null) {
                try {
                    durationSeconds = Integer.parseInt(duration) / 1000;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            retriever.release();

            return new Audiobook(title, author, audiobookFile.getAbsolutePath(), durationSeconds, book);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private void loadAudiobooks() {
        Map<String, List<Audiobook>> audiobookMap = new HashMap<>();
        audiobooks = audiobookDatabaseHelper.getAllAudiobooks();

        for (Audiobook audiobook : audiobooks) {
            String key = audiobook.getAuthor() + "-" + audiobook.getBook();
            audiobookMap.computeIfAbsent(key, k -> new ArrayList<>()).add(audiobook);
        }

        List<List<Audiobook>> groupedAudiobooks = new ArrayList<>(audiobookMap.values());
        audiobookAdapter.updateData(groupedAudiobooks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audiobookDatabaseHelper != null) {
            audiobookDatabaseHelper.closeDatabase(); // Ensure this method properly closes the database
        }
    }




}