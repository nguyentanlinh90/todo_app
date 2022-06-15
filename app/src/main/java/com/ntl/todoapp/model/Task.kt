package com.ntl.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class Task(
    @ColumnInfo(name = "is_complete") var isComplete: Boolean, var name: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun toString(): String {
        return "Task{" +
                "id=" + id +
                ", isComplete='" + isComplete + '\'' +
                ", name='" + name + '\'' +
                '}'
    }
}