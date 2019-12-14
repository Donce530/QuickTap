package com.example.quickTap.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.quickTap.R
import com.example.quickTap.interfaces.GameStatesListener

class GameResultFragment(private val averageReactionTime: Int) : Fragment(), OnClickListener {

    lateinit var gameStatesListener: GameStatesListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_game_result, container, false)

        val nanosecondsView = view.findViewById<TextView>(R.id.result_avg_time)
        nanosecondsView.text = "$averageReactionTime\nmiliseconds!"

        val retryButton =  view.findViewById<ImageButton>(R.id.result_retry_button)
        retryButton.setOnClickListener(this)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameStatesListener = context as GameStatesListener
    }

    override fun onClick(view: View?) {
        gameStatesListener.onGameStarted()
    }

}
