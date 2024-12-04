import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrollviewui.ProjectAdapter
import com.up.projectmanager.*
import kotlinx.serialization.json.Json

class ProjectsFragment: Fragment() {

    var projectsList = mutableListOf(
        Project(
            id = "1",
            name = "Project A",
            description = "Description of Project A",
            createdOn = "2024-01-01",
            deadline = "2024-06-01",
            members = listOf(
                listOf("lead@example.com", "lead"),
                listOf("member1@example.com", "member")
            ),
            tasks = listOf(
                Task(
                    name = "Task 1",
                    description = "Complete the first task",
                    createdOn = "2024-01-02",
                    deadline = "2024-03-01",
                    done = false,
                ),
                Task(
                    name = "Task 2",
                    description = "Complete the second task",
                    createdOn = "2024-01-05",
                    deadline = "2024-04-01",
                    done = true,
                )
            )
        ),
        Project(
            id = "2",
            name = "Project B",
            description = "Description of Project B",
            createdOn = "2024-02-01",
            deadline = "2024-07-01",
            members = listOf(
                listOf("lead2@example.com", "lead"),
                listOf("member2@example.com", "member")
            ),
            tasks = listOf(
                Task(
                    name = "Task 1",
                    description = "Complete the first task",
                    createdOn = "2024-02-02",
                    deadline = "2024-04-01",
                    done = true,
                ),
                Task(
                    name = "Task 2",
                    description = "Complete the second task",
                    createdOn = "2024-02-03",
                    deadline = "2024-05-01",
                    done = false,
                )
            )
        ),
        Project(
            id = "3",
            name = "Project C",
            description = "Description of Project C",
            createdOn = "2024-03-01",
            deadline = "2024-08-01",
            members = listOf(
                listOf("lead3@example.com", "lead"),
                listOf("member3@example.com", "member")
            ),
            tasks = listOf(
                Task(
                    name = "Task 1",
                    description = "Complete the first task",
                    createdOn = "2024-03-01",
                    deadline = "2024-05-01",
                    done = true,
                ),
                Task(
                    name = "Task 2",
                    description = "Complete the second task",
                    createdOn = "2024-03-03",
                    deadline = "2024-06-01",
                    done = true,
                )
            )
        ),
        Project(
            id = "4",
            name = "Project D",
            description = "Description of Project D",
            createdOn = "2024-04-01",
            deadline = "2024-09-01",
            members = listOf(
                listOf("lead4@example.com", "lead"),
                listOf("member4@example.com", "member")
            ),
            tasks = listOf(
                Task(
                    name = "Task 1",
                    description = "Complete the first task",
                    createdOn = "2024-04-02",
                    deadline = "2024-06-01",
                    done = false,
                ),
                Task(
                    name = "Task 2",
                    description = "Complete the second task",
                    createdOn = "2024-04-03",
                    deadline = "2024-07-01",
                    done = false,
                )
            )
        ),
        Project(
            id = "5",
            name = "Project E",
            description = "Description of Project E",
            createdOn = "2024-05-01",
            deadline = "2024-10-01",
            members = listOf(
                listOf("lead5@example.com", "lead"),
                listOf("member5@example.com", "member")
            ),
            tasks = listOf(
                Task(
                    name = "Task 1",
                    description = "Complete the first task",
                    createdOn = "2024-05-01",
                    deadline = "2024-07-01",
                    done = true,
                ),
                Task(
                    name = "Task 2",
                    description = "Complete the second task",
                    createdOn = "2024-05-02",
                    deadline = "2024-08-01",
                    done = false,
                )
            )
        )
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_projects, container, false)
        val preferences = context!!.getSharedPreferences("pref", 0)
        if (preferences.getString("createdProject", "")?.isNotEmpty() == true) {
            val createdProject = preferences.getString("createdProject", "")?.let { Json.decodeFromString<Project>(it) }
            if (createdProject != null) {
                projectsList.add(createdProject)
            }
            var editor = preferences.edit()
            editor.putString("createdProject", "")
            editor.apply()
        }
        val addProjectButton = view.findViewById<Button>(R.id.add_project_button)

        val projectAdapter = ProjectAdapter(projectsList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = projectAdapter

        return view
    }
}