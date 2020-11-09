package edu.mills.heartoftaiwanese.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.mills.heartoftaiwanese.databinding.FragmentRecentBinding

class RecentFragment : Fragment() {
    private lateinit var binding: FragmentRecentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentBinding.inflate(layoutInflater)
        return binding.root
    }
    // Todo: Under construction
}
