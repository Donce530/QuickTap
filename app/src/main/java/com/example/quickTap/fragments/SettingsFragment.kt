package com.example.quickTap.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.quickTap.R
import com.example.quickTap.constants.SharedPreferencesConstants.Companion.PlayerAgeKey
import com.example.quickTap.constants.SharedPreferencesConstants.Companion.PlayerNameKey
import com.example.quickTap.constants.SharedPreferencesConstants.Companion.PreferencesFile
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment(), OnClickListener {

    lateinit var currentView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        currentView = inflater.inflate(R.layout.fragment_settings, container, false)

        setInitialFieldData()

        setupClickListeners()

        return currentView
    }

    private fun setInitialFieldData() {
        val nameInput = currentView.findViewById<EditText>(R.id.settings_name_input)
        val ageInput = currentView.findViewById<EditText>(R.id.settings_age_input)

        val sharedPreferences = activity!!.getSharedPreferences(PreferencesFile, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(PlayerNameKey, "Player 1")
        val age = sharedPreferences.getInt(PlayerAgeKey, 18)

        nameInput.setText(name)
        ageInput.setText(age.toString())
    }

    private fun setupClickListeners() {
        val saveButton = currentView.findViewById<ImageButton>(R.id.settings_save_button)!!
        saveButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.settings_save_button -> saveButtonClick()
        }
    }

    private fun saveButtonClick() {
        val name = activity!!.findViewById<EditText>(R.id.settings_name_input)?.text.toString()
        val age = activity!!.findViewById<EditText>(R.id.settings_age_input)?.text.toString().toInt()

        val sharedPreferences = activity!!.getSharedPreferences(PreferencesFile, Context.MODE_PRIVATE)
        val preferencesEditor = sharedPreferences!!.edit()

        preferencesEditor.putString(PlayerNameKey, name)
        preferencesEditor.putInt(PlayerAgeKey, age)

        preferencesEditor.apply()

        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentView.windowToken, 0)

        val snackbarCoordinator = activity!!.findViewById<CoordinatorLayout>(R.id.settings_snackbar_coordinator)
        Snackbar.make(snackbarCoordinator,
            R.string.saved_successfully, Snackbar.LENGTH_SHORT).show()
    }
}
