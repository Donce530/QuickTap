package com.example.quickTap.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.quickTap.fragments.GameFragment
import com.example.quickTap.fragments.GameResultFragment
import com.example.quickTap.R
import com.example.quickTap.models.TimeHistoryEntry
import com.example.quickTap.constants.SharedPreferencesConstants
import com.example.quickTap.database.AppDatabase
import com.example.quickTap.database.entities.Score
import com.example.quickTap.interfaces.GameStatesListener
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import kotlin.math.roundToInt

class PlayActivity : AppCompatActivity(), GameStatesListener {

    private var database: AppDatabase? = null
    private var playerName: String? = null
    private var playerAge = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        onGameStarted()
    }

    override fun onGameFinished(results: List<TimeHistoryEntry>) {
        val averageReactiontime = calculateAverageReactionTime(results)
        changeFragmentTo(GameResultFragment(averageReactiontime))

        saveGameResult(averageReactiontime)
    }

    override fun onGameStarted() {
        changeFragmentTo(GameFragment())
    }

    private fun saveGameResult(reactionTime: Int) {
        ensureParameters()
        val scoreDao = database!!.scoreDao()
        val newScore = Score(playerName!!, playerAge, LocalDateTime.now(), reactionTime)
        runBlocking {
            scoreDao.insertScore(newScore)
        }
    }

    private fun ensureParameters() {
        if (database == null) {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "app-db"
            ).build()
        }

        if (playerName == null) {
            val sharedPreferences = getSharedPreferences(
                SharedPreferencesConstants.PreferencesFile,
                Context.MODE_PRIVATE
            )
            playerName =
                sharedPreferences.getString(SharedPreferencesConstants.PlayerNameKey, "Player 1")
            playerAge = sharedPreferences.getInt(SharedPreferencesConstants.PlayerAgeKey, 18)
        }
    }

    private fun calculateAverageReactionTime(results: List<TimeHistoryEntry>) : Int {
        var firstClickTimes = mutableListOf<Long>()
        results.groupBy { r -> r.roundNumber }.forEach { entry ->
            firstClickTimes.add(entry.value.sortedBy { r -> r.clickNumber }[0].nanosecondsElapsed)
        }

        return firstClickTimes.average().roundToInt()
    }

    private fun changeFragmentTo(fragment: Fragment) {
        with(supportFragmentManager) {
            beginTransaction().setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            ).replace(
                R.id.play_fragment_container,
                fragment
            ).commit()
        }
    }
}
