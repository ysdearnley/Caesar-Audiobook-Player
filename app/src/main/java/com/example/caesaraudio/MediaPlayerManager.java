package com.example.caesaraudio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerManager {
    private static MediaPlayerManager mediaPlayerManager;
    private MediaPlayer mediaPlayer;
    private Context context;

    private String currentFilepath = null;

    private MediaPlayerManager(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
    public static MediaPlayerManager getInstance(Context context){
        if(mediaPlayerManager == null){
            mediaPlayerManager = new MediaPlayerManager(context);
        }
        return mediaPlayerManager;
    }
    public void playMedia(String filepath){
        Log.i("CHECK", "playMedia " + filepath);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.e("MediaPlayer Error", "Error playing media: " + filepath + ex);
        }
    }
    public void pauseMedia(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    public void resumeMedia(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

    }

    public void cleanup() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        releasePlayer();
    }


    public void releasePlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }
}
