package com.example.quickTap.database.dataAccessObjects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quickTap.database.entities.Score

@Dao
interface ScoreDao {
    @Query("SELECT * from score ORDER BY result ASC")
    suspend fun getAll(): List<Score>

    @Insert
    suspend fun insertScore(score: Score) : Long
}