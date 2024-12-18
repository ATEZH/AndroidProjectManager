import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.up.projectmanager.MainViewModel
import com.up.projectmanager.R
import com.up.projectmanager.data.user.User
import com.up.projectmanager.entryviews.SignInActivity


class ProfileFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var email: TextView
    private lateinit var idField: TextView
    private lateinit var signout: Button
    private lateinit var loadingSpinner: CircularProgressIndicator
    private lateinit var container: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(
            R.layout.fragment_profile,
            container,
            false)

        initializeUI(view)
        observeViewModel()

        return view
    }

    private fun initializeUI(view: View) {
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        container = view.findViewById(R.id.container)
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
        email = view.findViewById(R.id.email)
        idField = view.findViewById(R.id.id)
        idField.setTextIsSelectable(true)
        signout = view.findViewById(R.id.signout)
        signout.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            updateUser(user)
        }
        viewModel.userLoading.observe(viewLifecycleOwner) { userLoading ->
            setLoading(userLoading)
        }
    }

    private fun updateUser(user: User) {
        firstName.text = user.firstName
        lastName.text = user.lastName
        email.text = user.email
        idField.text = user.id
    }

    private fun setLoading(status: Boolean) {
        loadingSpinner.visibility = if (status) View.VISIBLE else View.INVISIBLE
        container.visibility = if (status) View.INVISIBLE else View.VISIBLE
    }
}