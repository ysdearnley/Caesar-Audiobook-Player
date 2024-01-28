package com.example.caesaraudio;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudiobookDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "audiobook.db";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_BOOK = "book";
    public static final String TABLE_NAME = "audiobooks";
    private SQLiteDatabase audiobookDatabase;

    public AudiobookDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        audiobookDatabase = getWritableDatabase(); // Initialize the database here


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_AUTHOR + " TEXT, " + COLUMN_FILE_PATH + " TEXT, " + COLUMN_DURATION + " INTEGER, " + COLUMN_BOOK + " TEXT);";
        db.execSQL(createTableStatement);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void openDatabase() {
        audiobookDatabase = getWritableDatabase();
    }

    public long insertAudiobook(Audiobook audiobook){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, audiobook.getTitle());
        cv.put(COLUMN_AUTHOR, audiobook.getAuthor());
        cv.put(COLUMN_FILE_PATH, audiobook.getFilepath());
        cv.put(COLUMN_DURATION, audiobook.getDurationSeconds());
        cv.put(COLUMN_BOOK, audiobook.getBook());


        return audiobookDatabase.insert(TABLE_NAME, null, cv);

    }

    public List<Audiobook> getAllAudiobooks(){
        List < Audiobook> audiobooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsArray = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_AUTHOR, COLUMN_FILE_PATH, COLUMN_DURATION, COLUMN_BOOK};
        String title = "", author = "", filepath = "", book="";
        int durationSeconds = 0;
        long id= 0;
        int idIndex = -1, titleIndex = -1, bookIndex = -1, authorIndex= -1, durationSecondsIndex = -1, filepathIndex = -1;

        Cursor cursor = audiobookDatabase.query(TABLE_NAME, columnsArray, null, null, null, null, null);
        Audiobook audiobook;



        if (cursor != null && cursor.moveToFirst()) {
            do {
                idIndex = cursor.getColumnIndex(COLUMN_ID);
                titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                authorIndex = cursor.getColumnIndex(COLUMN_AUTHOR);
                filepathIndex = cursor.getColumnIndex(COLUMN_FILE_PATH);
                durationSecondsIndex = cursor.getColumnIndex(COLUMN_DURATION);
                bookIndex = cursor.getColumnIndex(COLUMN_BOOK);

                if(idIndex != -1){
                    id = cursor.getLong(idIndex);
                }
                if(titleIndex != -1){
                    title = cursor.getString(titleIndex);
                }
                if(authorIndex != -1){
                    author = cursor.getString(authorIndex);
                }
                if(filepathIndex != -1){
                    filepath = cursor.getString(filepathIndex);
                }
                if(durationSecondsIndex != -1){
                    durationSeconds = cursor.getInt(durationSecondsIndex);
                }
                if(bookIndex != -1){
                    book = cursor.getString(bookIndex);
                }

                audiobook = new Audiobook(title, author, filepath, durationSeconds, book);
                audiobook.setId(id);
                audiobooks.add(audiobook);

            } while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return audiobooks;

    }


    private Audiobook cursorToAudiobook(Cursor cursor){
        Audiobook audiobook = new Audiobook();

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        if (columnIndexId != -1) {
            audiobook.setId(cursor.getLong(columnIndexId));
        }

        int columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE);
        if (columnIndexTitle != -1) {
            audiobook.setTitle(cursor.getString(columnIndexTitle));
        }

        int columnIndexAuthor = cursor.getColumnIndex(COLUMN_AUTHOR);
        if (columnIndexAuthor != -1) {
            audiobook.setAuthor(cursor.getString(columnIndexAuthor));
        }

        int columnIndexFilePath = cursor.getColumnIndex(COLUMN_FILE_PATH);
        if (columnIndexFilePath != -1) {
            audiobook.setFilepath(cursor.getString(columnIndexFilePath));
        }

        int columnIndexDuration = cursor.getColumnIndex(COLUMN_DURATION);
        if (columnIndexDuration != -1) {
            audiobook.setDurationSeconds(cursor.getInt(columnIndexDuration));
        }

        int columnIndexBook = cursor.getColumnIndex(COLUMN_BOOK);
        if (columnIndexBook != -1) {
            audiobook.setBook(cursor.getString(columnIndexBook));
        }

        return audiobook;
    }


    public void closeDatabase() {
        if (audiobookDatabase != null && audiobookDatabase.isOpen()) {
            audiobookDatabase.close();
        }
    }

    public Boolean isAudiobookInLibrary(String filePath){
        if(audiobookDatabase == null){
            return false;
        }
        String[] selectionCriteria = {filePath};
        Cursor cursor = audiobookDatabase.query(TABLE_NAME, null, COLUMN_FILE_PATH + "= ?", selectionCriteria, null, null, null);

        boolean isInLibrary = (cursor != null && cursor.getCount() > 0);
        if(cursor != null){
            cursor.close();
        }
        return  isInLibrary;
    }

}
