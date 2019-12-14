package com.example.quickTap.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Score (
    @ColumnInfo(name = "player_name") val userName: String,
    @ColumnInfo(name = "player_age") val userAge: Int,
    @ColumnInfo(name = "date_time") val dateTime: LocalDateTime,
    @ColumnInfo(name = "result") val result: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}