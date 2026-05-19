package at.ac.hcw.procrastinot.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LocalTask::class], version = 2, exportSchema = false)
@TypeConverters(TaskTypeConverters::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE task ADD COLUMN priority TEXT NOT NULL DEFAULT 'MEDIUM'"
                )
            }
        }
    }
}