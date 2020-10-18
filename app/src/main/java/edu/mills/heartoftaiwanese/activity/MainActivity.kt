package edu.mills.heartoftaiwanese.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.mills.heartoftaiwanese.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getViewBinding()
        initializeClickListeners()
    }

    abstract fun getViewBinding()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item) {
            binding.root.bottomNavigationBar[1] -> launchHome()
            binding.root.bottomNavigationBar[2] -> launchRecent()
            binding.root.bottomNavigationBar[3] -> launchFavorites()
            else -> return false
        }
        return true
    }

    protected open fun initializeClickListeners() {
        binding.root.bottomNavigationBar.setOnNavigationItemSelectedListener(this)
    }

    private fun launchHome() {
        Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show()
    }

    private fun launchFavorites() {
        Toast.makeText(this, "FAV", Toast.LENGTH_SHORT).show()
    }

    private fun launchRecent() {
        Toast.makeText(this, "RECENT", Toast.LENGTH_SHORT).show()
    }

    protected data class LanguageContainer internal constructor(
        internal val text: String,
        internal val language: Int
    )
}
