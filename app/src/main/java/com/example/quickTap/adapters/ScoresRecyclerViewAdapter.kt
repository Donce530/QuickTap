package com.example.quickTap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickTap.R
import com.example.quickTap.database.entities.Score
import java.time.format.DateTimeFormatter.ofPattern

class ScoresRecyclerViewAdapter(val context: Context, private val scores: List<Score>) : RecyclerView.Adapter<ScoresRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.layout_score_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = scores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = scores[position]
        holder.apply {
            nameView.text = score.userName
            ageView.text = score.userAge.toString()
            dateView.text = score.dateTime.format(ofPattern("dd.MM.yyyy HH:mm"))
            reactionView.text = score.result.toString()
        }
    }

    class ViewHolder(viewToHold: View) : RecyclerView.ViewHolder(viewToHold)  {
        val nameView: TextView = viewToHold.findViewById(R.id.score_player_name)
        val ageView: TextView = viewToHold.findViewById(R.id.score_player_age)
        val dateView: TextView = viewToHold.findViewById(R.id.score_date)
        val reactionView: TextView = viewToHold.findViewById(R.id.score_player_score)
    }
}