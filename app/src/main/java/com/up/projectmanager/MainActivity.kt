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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var user: User
    lateinit var bottomMenu : BottomNavigationView
    lateinit var addProject : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(
            "ProjectManager",
            Context.MODE_PRIVATE)
        if (prefs.getBoolean("signedIn", false)) {
            val userSerialized = prefs.getString("user", "null")
            user = userSerialized?.let { Json.decodeFromString(it) }!!
            Log.d(TAG, userSerialized)
        } else if (!prefs.getBoolean("signedUp", false)) {
            goToSignUpPage()
        } else if (!prefs.getBoolean("staySignedIn", false)) {
            goToSignInPage()
        } else {
            goToSignInPage(true)
        }

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
                    loadFragment(SettingsFragment(user))
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

    fun goToSignInPage(autoSignIn: Boolean = false) {
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("AUTO_SIGN_IN", autoSignIn)
        startActivity(intent)
    }

    fun goToSignUpPage() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun goToCreateProject() {
        val intent = Intent(this, CreateProjectActivity::class.java)
        startActivity(intent)
    }
}