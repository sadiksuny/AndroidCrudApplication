package com.example.lab6;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Stock.class}, version = 2)
public abstract class StockDB extends RoomDatabase {

    private static StockDB instance;
    public abstract StockDao stockDao();

    public static synchronized  StockDB getInstance(Context context){
        if (null==instance){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    StockDB.class, "stock_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
