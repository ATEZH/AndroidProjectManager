package com.up.projectmanager

import ProjectsFragment
import SettingsFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import android.util.Log
import android.content.ContentValues.TAG
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.up.projectmanager.signin.SignInActivity
import com.up.projectmanager.signup.SignUpActivity

class MainActivity : AppCompatActivity() {

    lateinit var user: User
    lateinit var bottomMenu : BottomNavigationView
    lateinit var addProject : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        loadFragment(ProjectsFragment())
        addProject = findViewById(R.id.add_project_button)
        addProject.setOnClickListener {
            goToCreateProject()
        }
        bottomMenu = findViewById(R.id.bottom_menu)
        bottomMenu.itemIconTintList = null
        bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.projects -> {
                    loadFragment(ProjectsFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> {
                    loadFragment(ProjectsFragment())
                    true
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    fun goToCreateProject() {
        val intent = Intent(this, CreateProjectActivity::class.java)
        startActivity(intent)
    }
}