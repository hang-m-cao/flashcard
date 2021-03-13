package com.example.flashcard;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.flashcard.Flashcard.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract com.example.flashcard.FlashcardDao flashcardDao();
}
