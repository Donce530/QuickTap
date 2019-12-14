package com.example.quickTap.models

import com.example.quickTap.enums.GameClickMode

class TimeHistoryEntry(var roundNumber: Int, var clickNumber: Int, var clickMode: GameClickMode, var nanosecondsElapsed: Long) {
}