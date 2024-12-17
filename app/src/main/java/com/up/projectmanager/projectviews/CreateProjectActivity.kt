package com.up.projectmanager.projectviews

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
import com.google.firebase.auth.FirebaseAuth
import com.up.projectmanager.MainActivity
import com.up.projectmanager.R
import com.up.projectmanager.databinding.CreateProjectBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateProjectActivity : AppCompatActivity() {

    private val viewModel: ProjectViewModel by viewModels()

    private lateinit var binding: CreateProjectBinding
    private var memberLayouts = mutableListOf<Pair<TextInputEditText, AutoCompleteTextView>>() // To track member inputs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roles = resources.getStringArray(R.array.memberRole)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)

        addMemberLayout(arrayAdapter)

        binding.root.findViewById<Button>(R.id.add_member_button).setOnClickListener {
            addMemberLayout(arrayAdapter)
        }

        binding.root.findViewById<Button>(R.id.create_project_button).setOnClickListener {
            createProject()
        }

        binding.projectDeadlineInput.setOnClickListener {
            showDatePicker()
        }
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Deadline")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        binding.projectDeadlineInput.setOnClickListener {
            datePicker.show(supportFragmentManager, "datePicker")
        }
        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = sdf.format(Date(selection))
            binding.projectDeadlineInput.setText(selectedDate)
        }
        binding.projectDeadlineInput.isFocusable = false
        binding.projectDeadlineInput.isClickable = true
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
            binding.projectDeadlineInput.setText(formattedDate)
        }
    }

    private fun addMemberLayout(arrayAdapter: ArrayAdapter<String>) {
        val inflater = LayoutInflater.from(this)
        val memberLayout = inflater.inflate(R.layout.member_role_item, binding.memberContainer, false) as LinearLayout
        val memberIdInput = memberLayout.findViewById<TextInputEditText>(R.id.member_id_input)
        val memberRoleDropdown = memberLayout.findViewById<AutoCompleteTextView>(R.id.member_role_dropdown)

        memberRoleDropdown.setAdapter(arrayAdapter)
        binding.memberContainer.addView(memberLayout)
        memberLayouts.add(Pair(memberIdInput, memberRoleDropdown))
    }

    private fun observeViewModel() {
        viewModel.projectCreated.observe(this) { projectId ->
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }
    }

    private fun createProject() {
        val projectName = binding.projectNameInput.text.toString()
        val projectDescription = binding.projectDescriptionInput.text.toString()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val createdOn = formatter.parse(formatter.format(Date()))
        val deadline = formatter.parse(binding.projectDeadlineInput.text.toString())
        val memberMap = HashMap<String, String>()
        val members = mutableListOf<String>()
        for (pair in memberLayouts) {
            val memberId = pair.first.text.toString().trim()
            val memberRole = pair.second.text.toString().trim()
            if (memberId.isNotEmpty() && memberRole.isNotEmpty()) {
                memberMap[memberId] = memberRole.lowercase()
                members.add(memberId)
            }
        }
        val projectData: HashMap<String, Any> = hashMapOf(
            "name" to projectName,
            "description" to projectDescription,
            "deadline" to deadline,
            "members" to members,
            "memberRoles" to memberMap,
            "createdOn" to createdOn,
        )
        viewModel.createProject(projectData)
    }
}