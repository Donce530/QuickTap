package com.example.quickTap.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.transition.TransitionManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.quickTap.*
import com.example.quickTap.adapters.HistoryAdapter
import com.example.quickTap.enums.GameClickMode
import com.example.quickTap.interfaces.GameStatesListener
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random
import android.util.TypedValue
import com.example.quickTap.models.TimeHistoryEntry


class GameFragment : Fragment(), View.OnClickListener {

    private var viewScale: Float = 0.0f
    private var countDownTimer: CountDownTimer? = null
    private var primaryThemeColor = 0

    lateinit var gameEndingListener: GameStatesListener
    lateinit var gameMessageLabel: TextView
    lateinit var layout: ConstraintLayout

    private lateinit var card: MaterialCardView

    private val Rounds = 3
    private var roundPenalty = false

    private var roundsFinished = 0
    private var numberToGuess = 0
    private var clickMode = GameClickMode.SINGLE
    private var clicksCount = 0
    private var cardDispayStartTimestamp: Long = 0
    private var lastClickTimestamp: Long = 0
    private var roundLength: Long = 2000
    private lateinit var baseMessage: String
    private lateinit var highMessage: String
    private lateinit var lowMessage: String

    private lateinit var timesHistory : MutableList<TimeHistoryEntry>

    private lateinit var timesView: ListView
    private lateinit var timesAdapter: HistoryAdapter

    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        numberToGuess = Random.nextInt(1,100)

        val typedValue = TypedValue()
        activity!!.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        primaryThemeColor = typedValue.data

        var view = inflater.inflate(R.layout.fragment_game, container, false)

        gameMessageLabel = view.findViewById(R.id.game_message)
        layout = view.findViewById(R.id.game_fragment)
        viewScale = context!!.resources.displayMetrics.density

        baseMessage = getString(R.string.game_your_guess)
        highMessage = getString(R.string.high)
        lowMessage = getString(R.string.low)

        countDownTimer = object: CountDownTimer(2000, 500) {
            override fun onFinish() {
            }

            override fun onTick(milisecondsUntilFinished: Long) {
                val numberMessage = (milisecondsUntilFinished / 500).toString()
                if (numberMessage == "0") {
                    gameMessageLabel.text = getString(R.string.go)
                    moveMessage()
                    startNextRound()
                } else {
                    gameMessageLabel.text = numberMessage
                }

            }
        }.start()

        createCardAndTimeList()

        return view
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        countDownTimer?.cancel()
        super.onDestroy()
    }

    fun moveMessage() {
        val set = ConstraintSet()
        set.clone(layout)
        set.clear(R.id.game_message, ConstraintSet.BOTTOM)
        TransitionManager.beginDelayedTransition(layout)
        set.applyTo(layout)
    }

    private fun createCardAndTimeList() {
        card = MaterialCardView(context)
        card.id = View.generateViewId()
        layout.addView(card, 0)
        card.layoutParams.height = (150 * viewScale + 0.5f).toInt()
        card.layoutParams.width = (150 * viewScale + 0.5f).toInt()
        card.alpha = 0f
        card.requestLayout()
        card.setOnClickListener(this)

        timesView = ListView(context)
        timesView.id = View.generateViewId()
        layout.addView(timesView, 0)
        timesHistory = mutableListOf()
        timesAdapter = HistoryAdapter(context!!, timesHistory)
        timesView.adapter = timesAdapter
        timesView.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, (250 * viewScale + 0.5f).toInt())
        timesView.requestLayout()
        timesView.isStackFromBottom = false
        timesView.transcriptMode = AbsListView.TRANSCRIPT_MODE_NORMAL

        val set = ConstraintSet()
        set.clone(layout)
        set.connect(timesView.id, ConstraintSet.TOP, layout.id, ConstraintSet.TOP, toDp(600))
        set.connect(timesView.id, ConstraintSet.START, layout.id, ConstraintSet.START)
        set.connect(timesView.id, ConstraintSet.END, layout.id, ConstraintSet.END)
        set.connect(card.id, ConstraintSet.TOP, gameMessageLabel.id, ConstraintSet.BOTTOM, toDp(24))
        set.connect(card.id, ConstraintSet.BOTTOM, timesView.id, ConstraintSet.TOP, toDp(24))
        set.connect(card.id, ConstraintSet.START, layout.id, ConstraintSet.START, toDp(24))
        set.connect(card.id, ConstraintSet.END, layout.id, ConstraintSet.END, toDp(24))
        TransitionManager.beginDelayedTransition(layout)
        set.applyTo(layout)
    }

    private fun toDp(number: Int): Int{
        return (number * viewScale + 0.5f).toInt()
    }

    private fun cardClicked() {
        if (card.alpha == 0.0f) {
            penalty()
            return
        }
        clicksCount++

        when(clickMode) {
            GameClickMode.SINGLE -> if (clicksCount > 1) penalty()
            GameClickMode.DOUBLE -> if (clicksCount > 2) penalty()
            GameClickMode.TRIPLE -> if (clicksCount > 3) penalty()
        }

        if (clicksCount == 1) {
            handler.postDelayed({

                when(clickMode) {
                    GameClickMode.SINGLE -> {
                        if (clicksCount < 1) {
                            penalty()
                        }
                    }
                    GameClickMode.DOUBLE -> {
                        if (clicksCount < 2) {
                            penalty()
                        }
                    }
                    GameClickMode.TRIPLE -> {
                        if (clicksCount < 3) {
                            penalty()
                        }
                    }
                }

                if (!roundPenalty) {
                    endRound()
                    if (roundsFinished == Rounds) {
                        endGame()
                    } else {
                        startNextRound()
                    }
                }
            }, roundLength)
        }

        lastClickTimestamp = System.nanoTime()
        timesHistory.add(
            TimeHistoryEntry(
                roundsFinished + 1, clicksCount, clickMode,
                (lastClickTimestamp - cardDispayStartTimestamp) / 1000000
            )
        )
        timesAdapter.notifyDataSetChanged()
    }

    private fun penalty() {
        roundPenalty = true
        lastClickTimestamp = System.nanoTime()
        for (x in 0 until clicksCount) {
            timesHistory.removeAt(timesHistory.size - 1)
        }
        timesHistory.add(
            TimeHistoryEntry(
                roundsFinished + 1,
                clicksCount,
                clickMode,
                5000
            )
        )
        timesAdapter.notifyDataSetChanged()
        clicksCount++
        gameMessageLabel.text = getString(R.string.penalty)
        gameMessageLabel.setTextColor(activity!!.getColor(R.color.red))
        endRound()
        handler.postDelayed({
            if (roundsFinished == Rounds) {
                endGame()
            } else {
                startNextRound()
            }
        }, 1000)
    }

    private fun endRound() {
        hideCard()
        roundsFinished++
    }

    private fun hideCard() {
        card.alpha = 0f
    }

    private fun showCard() {
        randomizeCard()
        card.alpha = 1f
        cardDispayStartTimestamp = System.nanoTime()
    }

    private fun randomizeCard() {
        var mode = Random.nextInt(0, 3)
        when (mode) {
            0 -> {
                clickMode = GameClickMode.SINGLE
                card.setCardBackgroundColor(activity!!.getColor(R.color.lime))
            }
            1 -> {
                clickMode = GameClickMode.DOUBLE
                card.setCardBackgroundColor(activity!!.getColor(R.color.yellow))
            }
            2 -> {
                clickMode = GameClickMode.TRIPLE
                card.setCardBackgroundColor(activity!!.getColor(R.color.red))
            }
        }

        val verticalBias = Random.nextFloat()
        val horizontalBias = Random.nextFloat()

        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.setVerticalBias(card.id, verticalBias)
        constraintSet.setHorizontalBias(card.id, horizontalBias)
        constraintSet.applyTo(layout)
    }

    private fun startNextRound() {
        roundPenalty = false
        clicksCount = 0
        gameMessageLabel.text = "Round ${roundsFinished + 1}"
        gameMessageLabel.setTextColor(primaryThemeColor)

        handler.postDelayed({ if (!roundPenalty) showCard()}, Random.nextLong(1500, 5000))
    }

    private fun endGame() {
        handler.removeCallbacksAndMessages(null)
        gameEndingListener.onGameFinished(timesHistory)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameEndingListener = context as GameStatesListener
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            card.id -> cardClicked()
        }
    }
}
