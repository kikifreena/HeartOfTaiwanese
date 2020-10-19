package edu.mills.heartoftaiwanese.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.bottomNavigationBar

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
        initializeClickListeners()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_Home -> {
                return launchHome(this)
            }
            R.id.navigation_favorites -> {
                return launchFavorites(this)
            }
            R.id.navigation_recents -> {
                return launchRecent(this)
            }
        }
        return false
    }

    private fun launchHome(context: Context): Boolean {
        Toast.makeText(context, "HOME", Toast.LENGTH_SHORT).show()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            HomeFragment()
        ).commit()
        return true
    }

    private fun launchFavorites(context: Context): Boolean {
        Toast.makeText(context, "FAV", Toast.LENGTH_SHORT).show()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            FavoritesFragment()
        ).commit()
        return true
    }

    private fun launchRecent(context: Context): Boolean {
        Toast.makeText(context, "RECENT", Toast.LENGTH_SHORT).show()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            RecentFragment()
        ).commit()
        return true
    }

    private fun initializeClickListeners() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
    }
}
