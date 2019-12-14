package com.example.quickTap.interfaces

import com.example.quickTap.models.TimeHistoryEntry

interface GameStatesListener {
    fun onGameFinished(results: List<TimeHistoryEntry>)
    fun onGameStarted()
}