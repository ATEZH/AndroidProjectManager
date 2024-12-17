package com.up.projectmanager

import ProfileFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.up.projectmanager.projectviews.CreateProjectActivity
import com.up.projectmanager.projectviews.ProjectsFragment
import com.up.projectmanager.taskviews.TasksFragment

class MainActivity : AppCompatActivity() {

    private lateinit var loadingSpinner: CircularProgressIndicator
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomMenu: BottomNavigationView
    private lateinit var addButton: FloatingActionButton
    private var currentFragment: String = ProjectsFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeUI()
    }

    private fun initializeUI() {
        bottomMenu = findViewById(R.id.bottom_menu)
        fragmentContainer = findViewById(R.id.container)
        loadingSpinner = findViewById(R.id.loading_spinner)
        addButton = findViewById(R.id.add_fab)

        bottomMenu.itemIconTintList = null
        bottomMenu.selectedItemId = R.id.projects
        loadFragment(ProjectsFragment())
        bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.projects -> {
                    currentFragment = ProjectsFragment::class.java.simpleName
                    addButton.visibility = View.VISIBLE
                    loadFragment(ProjectsFragment())
                    true
                }
                R.id.tasks -> {
                    currentFragment = ProjectsFragment::class.java.simpleName
                    addButton.visibility = View.VISIBLE
                    loadFragment(TasksFragment())
                    true
                }
                R.id.settings -> {
                    currentFragment = ProfileFragment::class.java.simpleName
                    addButton.visibility = View.INVISIBLE
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        addButton.setOnClickListener {
            goToCreateTaskOrProject()
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun goToCreateTaskOrProject() {
        when (currentFragment) {
            ProjectsFragment::class.java.simpleName -> {
                val intent = Intent(this, CreateProjectActivity::class.java)
                startActivity(intent)
            }
            ProfileFragment::class.java.simpleName -> {
                val intent = Intent(this, CreateProjectActivity::class.java)
                startActivity(intent)
            }
        }
    }
}