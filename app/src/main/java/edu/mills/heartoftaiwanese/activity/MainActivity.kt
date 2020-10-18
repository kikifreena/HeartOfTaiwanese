package edu.mills.heartoftaiwanese.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    abstract override fun onNavigationItemSelected(item: MenuItem): Boolean

    protected open fun initializeClickListeners() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
    }

    private fun launchHome(context: Context): Boolean {
        Toast.makeText(context, "HOME", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun launchFavorites(context: Context): Boolean {
        Toast.makeText(context, "FAV", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun launchRecent(context: Context): Boolean {
        Toast.makeText(context, "RECENT", Toast.LENGTH_SHORT).show()
        return true
    }

    protected data class LanguageContainer internal constructor(
        internal val text: String,
        internal val language: Int
    )
}
