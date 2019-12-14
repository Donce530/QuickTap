package com.example.quickTap.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.quickTap.R
import com.example.quickTap.adapters.ScoresRecyclerViewAdapter
import com.example.quickTap.database.AppDatabase
import kotlinx.coroutines.*

class ScoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_scores, container, false)

        runBlocking {
            getScores(view)
        }

        return  view
    }

    private suspend fun getScores(view: View) {
        val database = AppDatabase.getInstance(context!!)
        var scores = database.scoreDao().getAll()

        val manager = LinearLayoutManager(activity)
        val scoresAdapter = ScoresRecyclerViewAdapter(activity!!, scores)
        val scoresList = view.findViewById<RecyclerView>(R.id.top_scores_list)
        scoresList.apply {
            layoutManager = manager
            adapter = scoresAdapter
        }
        scoresAdapter.notifyDataSetChanged()
    }

}
