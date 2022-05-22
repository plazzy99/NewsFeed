package e.vatsal.newsfeed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import e.vatsal.newsfeed.databinding.ProfileFragmentBinding
import e.vatsal.newsfeed.utils.extensions.startActivity
import e.vatsal.newsfeed.view.activity.LoginActivity
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileFragmentBinding

    private val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        setUpUI()
        fetchProfile()
        return binding.root
    }

    private fun setUpUI() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            "SharedPrefForTheme",
            AppCompatActivity.MODE_PRIVATE
        )

        binding.darkMode.isChecked = sharedPreferences.getBoolean("darkMode", false)

        binding.header.apply {
            titleText.text = "Profile"
            rightIcon.setOnClickListener {
                signOut()
            }
            leftIcon.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        binding.darkMode.setOnClickListener {
            Timber.d(binding.darkMode.isChecked.toString())
            when (binding.darkMode.isChecked) {
                true -> {
                    sharedPreferences.edit().putBoolean("darkMode", true).commit()
                }
                false -> {
                    sharedPreferences.edit().putBoolean("darkMode", false).commit()
                }
            }
//            requireActivity().supportFragmentManager.popBackStackImmediate(
//                null,
//                FragmentManager.POP_BACK_STACK_INCLUSIVE
//            )
            requireActivity().startActivity<LoginActivity>()
        }
    }

    private fun fetchProfile() {
        binding.etNameEdit.setText(user?.displayName)
        binding.etEmailEdit.setText(user?.email)
        binding.imProfileEdit.load(user?.photoUrl) {
            transformations(CircleCropTransformation())
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        requireActivity().startActivity<LoginActivity>()
    }
}