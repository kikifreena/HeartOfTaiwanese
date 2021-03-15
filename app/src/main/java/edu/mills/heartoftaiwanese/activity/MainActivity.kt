package edu.mills.heartoftaiwanese.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.activity.favorites.FavoritesFragment
import edu.mills.heartoftaiwanese.activity.home.HomeFragment
import edu.mills.heartoftaiwanese.activity.recent.RecentFragment
import edu.mills.heartoftaiwanese.databinding.ActivityMainBinding

class MainActivity :
    AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            HomeFragment()
        )
            .commit()
        initializeClickListeners()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_Home -> {
                return launchFragment(this, HomeFragment())
            }
            R.id.navigation_favorites -> {
                return launchFragment(this, FavoritesFragment())
            }
            R.id.navigation_recents -> {
                return launchFragment(this, RecentFragment())
            }
        }
        return false
    }

    private fun launchFragment(context: Context, fragment: BaseFragment): Boolean {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            fragment
        )
        return true
    }

    private fun initializeClickListeners() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener(this)
    }
}
