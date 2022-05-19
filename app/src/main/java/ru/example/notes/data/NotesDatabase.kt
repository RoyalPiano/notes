package ru.example.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.example.notes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): NotesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(NotesDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        private fun populateDatabase(noteDao: NotesDao) {
            noteDao.insert(Note("Description 1", "BaseNote", "date"))
        }
    }

    private class NotesDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { noteDatabase ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(noteDatabase.notesDao())
                }
            }
        }
    }
}