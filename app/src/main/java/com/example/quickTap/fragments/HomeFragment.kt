package com.example.quickTap.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.quickTap.R
import com.example.quickTap.activities.PlayActivity

class HomeFragment : Fragment(), OnClickListener {

    var canAccessLocation = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val currentView = inflater.inflate(R.layout.fragment_home, container, false)!!

        val startButton = currentView.findViewById<ImageButton>(R.id.home_start_button)!!
        startButton.setOnClickListener(this)

        personalizeMessage(currentView)

        managePermissions()

        return currentView
    }

    private fun managePermissions() {
        if (activity!!.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        } else {
            canAccessLocation = true
        }
    }

    //WRONG. SHOULD OVERRIDE ACTIVITY METHOD
//    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: Array<Int>) {
//        when (requestCode) {
//            0 ->  canAccessLocation = (results.isNotEmpty() && results[0] == PackageManager.PERMISSION_GRANTED)
//        }
//    }

    private fun personalizeMessage(currentView: View) {
        val sharedPreferences = activity?.getSharedPreferences(
            "PlayerSettings",
            Context.MODE_PRIVATE
        )
        val playerName = sharedPreferences?.getString("PlayerName", "Player 1")
        val unformattedGrretingsString = activity?.getString(R.string.home_message) ?: "Hello %s"
        val personalMessage = String.format(unformattedGrretingsString, playerName)
        val greetingsLabel = currentView.findViewById<TextView>(R.id.home_welcome_message)
        greetingsLabel?.text = personalMessage
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.home_start_button -> startButtonClick()
        }
    }

    private fun startButtonClick() {
        val intent = Intent(activity, PlayActivity::class.java)
        activity!!.startActivity(intent)
    }
}