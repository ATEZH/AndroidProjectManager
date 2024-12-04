import androidx.fragment.app.Fragment
import android.widget.Toast
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.up.projectmanager.R
import com.up.projectmanager.User


class SettingsFragment(user: User): Fragment() {
    val user = user
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(
            R.layout.fragment_settings,
            container,
            false)
        val first_name = view.findViewById<TextView>(R.id.first_name)
        first_name.text = user.firstName
        val last_name = view.findViewById<TextView>(R.id.last_name)
        last_name.text = user.lastName
        val email = view.findViewById<TextView>(R.id.email)
        email.text = user.email
        return view
    }
}