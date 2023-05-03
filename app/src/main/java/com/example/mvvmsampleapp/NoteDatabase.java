package com.example.mvvmsampleapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1) // new version number = fallbackToDestructiveMigration
public abstract class NoteDatabase extends RoomDatabase { // NoteDatabase will recieve RoomDatabase tools

    private static NoteDatabase instance; // Only this instance(class with resources) on the app = singleton

    public abstract NoteDao noteDao();
    // synchronized is only operation at time
    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback =  new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db){
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Tittle 1", "Description 1", 1));
            noteDao.insert(new Note("Tittle 2", "Description 2", 2));
            noteDao.insert(new Note("Tittle 3", "Description 3", 3));
            return null;
        }
    }
}
