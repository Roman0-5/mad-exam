package at.ac.hcw.procrastinot.data.source.local

import androidx.room.TypeConverter
import at.ac.hcw.procrastinot.data.TaskPriority

class TaskTypeConverters {
    @TypeConverter
    fun fromPriority(priority: TaskPriority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): TaskPriority = TaskPriority.valueOf(value)
}