package edu.mills.heartoftaiwanese.activity.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.hideKeyboard
import edu.mills.heartoftaiwanese.databinding.FragmentHomeBinding
import edu.mills.heartoftaiwanese.network.WebResultCode
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : HomeContract.HomeView, BaseFragment(), TabLayout.OnTabSelectedListener {
    companion object {
        private const val kSavedChineseText = "SavedChineseText"
        private const val kSavedEnglishText = "SavedEnglishText"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var currentTab: Int? = 0

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putString(kSavedChineseText, binding.editTextCh.text.toString())
        state.putString(kSavedEnglishText, binding.editTextEng.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        savedInstanceState?.let {
            binding.editTextCh.setText(it.getString(kSavedChineseText))
            binding.editTextEng.setText(it.getString(kSavedEnglishText))
        }
        val currentHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvTranslateExplanation.text = getString(
            R.string.translationFragmentExplanation,
            isMorningAfternoonEvening(currentHour)
        )
        viewModel = HomeViewModel()
        viewModel.configure(this, requireActivity().applicationContext)
        initializeClickListeners()

        binding.editTextCh.visibility = View.GONE
        binding.submitCh.visibility = View.GONE
        return binding.root
    }

    private fun isMorningAfternoonEvening(hour: Int): String {
        return when (hour) {
            in 4..12 -> resources.getStringArray(R.array.timeOfDay)[0] // morning
            in 12..17 -> resources.getStringArray(R.array.timeOfDay)[1] // afternoon
            else -> resources.getStringArray(R.array.timeOfDay)[2] // evening
        }
    }

    private fun initializeClickListeners() {
        binding.submitCh.setOnClickListener {
            hideForSubmit()
            binding.editTextEng.visibility = View.GONE
            viewModel.fetchTaiwanese(
                binding.editTextCh.text.toString()
            )
        }
        binding.submitEn.setOnClickListener {
            hideForSubmit()
            binding.editTextCh.visibility = View.GONE
            viewModel.fetchChinese(
                binding.editTextEng.text.toString()
            )
        }
        binding.clearButton.setOnClickListener {
            if (currentTab == 0) {
                binding.editTextEng.setText("")
            }
            if (currentTab == 1) {
                binding.editTextCh.setText("")
            }
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
        }
        binding.tabsEnglishChinese.addOnTabSelectedListener(this)
    }

    private fun hideForSubmit() {
        binding.twResult.visibility = View.GONE
        binding.result.visibility = View.GONE
        binding.progressBarLoading.visibility = View.VISIBLE
        binding.submitCh.visibility = View.GONE
        binding.submitEn.visibility = View.GONE
        binding.clearButton.visibility = View.INVISIBLE
        hideKeyboard()
    }

    private fun showAfterSubmit() {
        binding.progressBarLoading.visibility = View.GONE
        binding.twResult.visibility = View.VISIBLE
        binding.result.visibility = View.VISIBLE
        if (currentTab == 1) {
            binding.submitCh.visibility = View.VISIBLE
            binding.editTextCh.visibility = View.VISIBLE
        }
        if (currentTab == 0) {
            binding.submitEn.visibility = View.VISIBLE
            binding.editTextEng.visibility = View.VISIBLE
        }
        binding.clearButton.visibility = View.VISIBLE
    }

    override fun onChineseFetched(chinese: String) {
        viewModel.fetchTaiwanese(chinese)
    }

    override fun onTaiwaneseFetched(taiwanese: String) {
        binding.result.text = taiwanese
        activity?.runOnUiThread {
            showAfterSubmit()
        }
    }

    override fun onNetworkError(error: WebResultCode) {
        Log.e("HomeFragment", error.toString())
        activity?.runOnUiThread {
            showAfterSubmit()
            when (error) {
                WebResultCode.RATE_LIMITED -> Toast.makeText(
                    activity,
                    getText(R.string.too_many_requests),
                    Toast.LENGTH_LONG
                ).show()
                WebResultCode.INVALID_NOT_FOUND -> Toast.makeText(
                    activity,
                    getText(R.string.errorNotFound),
                    Toast.LENGTH_LONG
                ).show()
                WebResultCode.UNKNOWN_ERROR -> Toast.makeText(
                    activity,
                    getText(R.string.error),
                    Toast.LENGTH_LONG
                ).show()
                // Bad state
                WebResultCode.RESULT_OK -> throw IllegalStateException("Do not call error when there's no error")
            }
        }
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     */
    override fun onTabSelected(tab: TabLayout.Tab?) {
        currentTab = tab?.position
        if (currentTab == 1) {
            binding.editTextEng.visibility = View.GONE
            binding.submitEn.visibility = View.GONE
        }
        if (currentTab == 0) {
            binding.editTextCh.visibility = View.GONE
            binding.submitCh.visibility = View.GONE
        }
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     */
    override fun onTabUnselected(tab: TabLayout.Tab?) {
        if (currentTab == 1) {
            binding.editTextEng.visibility = View.VISIBLE
            binding.submitEn.visibility = View.VISIBLE
        }
        if (currentTab == 0) {
            binding.editTextCh.visibility = View.VISIBLE
            binding.submitCh.visibility = View.VISIBLE
        }
    }

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications may
     * use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     */
    override fun onTabReselected(tab: TabLayout.Tab?) {
        // Nothing
    }
}
