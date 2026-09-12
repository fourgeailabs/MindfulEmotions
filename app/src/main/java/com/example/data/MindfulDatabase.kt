package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.EmotionCheckDao
import com.example.data.dao.JournalDao
import com.example.data.dao.ScheduleDao
import com.example.data.model.EmotionCheckLog
import com.example.data.model.EmotionSchedule
import com.example.data.model.JournalEntry

@Database(
    entities = [EmotionSchedule::class, EmotionCheckLog::class, JournalEntry::class],
    version = 3,
    exportSchema = false
)
abstract class MindfulDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun emotionCheckDao(): EmotionCheckDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: MindfulDatabase? = null

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE schedules ADD COLUMN specificYear INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE schedules ADD COLUMN specificMonth INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE schedules ADD COLUMN specificDay INTEGER DEFAULT NULL")
            }
        }

        fun getDatabase(context: Context): MindfulDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MindfulDatabase::class.java,
                    "mindful_emotions.db"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
