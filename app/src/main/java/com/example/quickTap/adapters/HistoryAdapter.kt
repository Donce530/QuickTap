package com.example.quickTap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.quickTap.R
import com.example.quickTap.models.TimeHistoryEntry

class HistoryAdapter : BaseAdapter {

    var mContext : Context
    var mData : MutableList<TimeHistoryEntry>

    constructor(context : Context, data: MutableList<TimeHistoryEntry>) : super() {
        mContext = context
        mData = data
    }

    override fun getCount() : Int {
        return mData.count()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View? {

        var mutableView = view
        if (view == null) {
            val inflater = LayoutInflater.from(mContext)
            mutableView = inflater.inflate(R.layout.layout_simple_history_item, null)
        }

        val resultView = mutableView!!.findViewById<TextView>(R.id.simple_history_value)
        var entry = mData[position]
        var message  = "Round: ${entry.roundNumber} \n Mode: ${entry.clickMode.name.toLowerCase().capitalize()} \n Click: ${entry.clickNumber} \n Time: ${entry.nanosecondsElapsed} miliseconds"

        resultView.text = message

        //inflate timeView
        //set data to views

        return mutableView
    }

    override fun getItemId(position: Int): Long {
        return 5
    }

    override fun getItem(position : Int) : Any = mData[position]
}