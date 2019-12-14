package com.example.quickTap.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.quickTap.fragments.TutorialFragment
import com.example.quickTap.fragments.HomeFragment
import com.example.quickTap.R
import com.example.quickTap.fragments.ScoresFragment
import com.example.quickTap.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BottomNavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container,
            HomeFragment()
        ).commit()
    }

    override fun onNavigationItemSelected(item : MenuItem): Boolean {
        var selectedFragment = Fragment()

        with(supportFragmentManager) {
            when (item.itemId) {
                R.id.menu_home_button -> selectedFragment =
                    HomeFragment()
                R.id.menu_about_button -> selectedFragment =
                    TutorialFragment()
                R.id.menu_settings_button -> selectedFragment =
                    SettingsFragment()
                R.id.menu_scores_button -> selectedFragment =
                    ScoresFragment()
            }

            val existingSelectedTypeFragment = findFragmentByTag(selectedFragment.javaClass.name)

            if (existingSelectedTypeFragment == null || !existingSelectedTypeFragment.isVisible){
                beginTransaction().setCustomAnimations(
                    R.anim.enter,
                    R.anim.exit,
                    R.anim.pop_enter,
                    R.anim.pop_exit
                ).replace(R.id.main_fragment_container, selectedFragment, selectedFragment.javaClass.name).commit()
            }
        }

        return true
    }
}
