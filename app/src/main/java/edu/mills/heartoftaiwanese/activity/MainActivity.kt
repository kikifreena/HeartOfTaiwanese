package edu.mills.heartoftaiwanese.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.mills.heartoftaiwanese.R
import kotlinx.android.synthetic.main.activity_main.bottomNavigationBar

abstract class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewBinding()
        initializeClickListeners()
    }

    protected abstract fun getViewBinding()

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
        return true
    }

    protected open fun initializeClickListeners() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
    }

    private fun launchHome(context: Context): Boolean {
        Toast.makeText(context, "HOME", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun launchFavorites(context: Context): Boolean {
        Toast.makeText(context, "FAV", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, FavoritesActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun launchRecent(context: Context): Boolean {
        Toast.makeText(context, "RECENT", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, RecentActivity::class.java)
        startActivity(intent)
        return true
    }

    protected data class LanguageContainer internal constructor(
        internal val text: String,
        internal val language: Int
    )
}
