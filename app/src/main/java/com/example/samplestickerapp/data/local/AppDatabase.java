package com.example.samplestickerapp.data.local;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.samplestickerapp.data.local.dao.StickerDao;
import com.example.samplestickerapp.data.local.entities.Sticker;
import com.example.samplestickerapp.data.local.entities.StickerPack;

/**
 * Created by vinoth on 5-1-19
 */
@Database(entities = {StickerPack.class, Sticker.class
        },
        version = 5, exportSchema = false)
@TypeConverters(value = {LanguageConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "sticker.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                Context appContext = context.getApplicationContext();
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract StickerDao subjectDao();



}