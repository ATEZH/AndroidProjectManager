package com.up.projectmanager.projectviews

import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.up.projectmanager.MainViewModel
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.R
import kotlinx.serialization.json.Json


class ProjectDetailsActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_overview)

    }
}