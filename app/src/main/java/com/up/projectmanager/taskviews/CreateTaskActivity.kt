package com.up.projectmanager.taskviews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.up.projectmanager.MainActivity
import com.up.projectmanager.R
import com.up.projectmanager.databinding.CreateProjectBinding
import com.up.projectmanager.databinding.CreateTaskBinding
import com.up.projectmanager.projectviews.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    private val viewModel: TaskViewModel by viewModels()

    private lateinit var binding: CreateTaskBinding
    private var memberLayouts = mutableListOf<TextInputEditText>()
    private lateinit var members: List<String>
    private lateinit var projectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getStringExtra("projectId")!!
        viewModel.getProject(projectId)
        binding = CreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.findViewById<Button>(R.id.create_task_button).setOnClickListener {
            createProject()
        }

        binding.root.findViewById<Button>(R.id.add_assignee_button).setOnClickListener {
            addMemberLayout()
        }
        addMemberLayout()

        binding.taskDeadlineInput.setOnClickListener {
            showDatePicker()
        }
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Deadline")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        binding.taskDeadlineInput.setOnClickListener {
            datePicker.show(supportFragmentManager, "datePicker")
        }
        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = sdf.format(Date(selection))
            binding.taskDeadlineInput.setText(selectedDate)
        }
        binding.taskDeadlineInput.isFocusable = false
        binding.taskDeadlineInput.isClickable = true
        observeViewModel()
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Deadline")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = formatter.format(Date(selection))
            binding.taskDeadlineInput.setText(formattedDate)
        }
    }

    private fun addMemberLayout() {
        val inflater = LayoutInflater.from(this)
        val memberLayout = inflater.inflate(R.layout.assignee_item, binding.assigneeContainer, false) as LinearLayout
        val memberIdInput = memberLayout.findViewById<TextInputEditText>(R.id.assignee_input)

        binding.assigneeContainer.addView(memberLayout)
        memberLayouts.add(memberIdInput)
    }

    private fun observeViewModel() {
        viewModel.taskCreated.observe(this) { taskId ->
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("taskId", taskId)
            startActivity(intent)
            finish()
        }
        viewModel.project.observe(this) { project ->
            this.members = project.members
        }
    }

    private fun createProject() {
        val taskName = binding.taskNameInput.text.toString()
        val taskDescription = binding.taskDescriptionInput.text.toString()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val createdOn = formatter.parse(formatter.format(Date()))
        val deadline = formatter.parse(binding.taskDeadlineInput.text.toString())
        val members = mutableListOf<String>()
        for (member in memberLayouts) {
            val memberId = member.text.toString().trim()
            if (memberId.isNotEmpty() && memberId !in members && memberId in this.members) {
                members.add(memberId)
            }
        }
        val taskData: HashMap<String, Any> = hashMapOf(
            "name" to taskName,
            "description" to taskDescription,
            "deadline" to deadline,
            "createdOn" to createdOn,
            "completed" to false,
            "assignees" to members,
            "projectId" to projectId
        )
        viewModel.createTask(taskData)
    }
}