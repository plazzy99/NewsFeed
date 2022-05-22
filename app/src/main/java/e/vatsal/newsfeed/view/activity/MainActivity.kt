package e.vatsal.newsfeed.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.databinding.ActivityMainBinding
import e.vatsal.newsfeed.view.fragment.HomeFeedFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
    }

    private fun setUpUI() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<HomeFeedFragment>(R.id.fragment_container_view)
        }
    }
}