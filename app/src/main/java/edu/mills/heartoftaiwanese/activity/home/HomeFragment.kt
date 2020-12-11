package edu.mills.heartoftaiwanese.activity.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.databinding.FragmentHomeBinding
import edu.mills.heartoftaiwanese.network.WebResultCode
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), HomeContract.HomeView {
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
    private lateinit var viewModel: HomeContract.HomeViewModel

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putString(kSavedChineseText, binding.editTextCh.text.toString())
        state.putString(kSavedEnglishText, binding.editTextEng.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        viewModel.configure(this)
        initializeClickListeners()
        // TODO: There is a bug when you expand/close the keyboard, part of the screen disappears
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
            binding.editTextCh.setText("")
            binding.editTextEng.setText("")
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
        }
    }

    private fun hideForSubmit() {
        binding.twResult.visibility = View.GONE
        binding.result.visibility = View.GONE
        binding.progressBarLoading.visibility = View.VISIBLE
        binding.submitCh.visibility = View.GONE
        binding.submitEn.visibility = View.INVISIBLE
        binding.clearButton.visibility = View.INVISIBLE
    }

    private fun showAfterSubmit() {
        binding.progressBarLoading.visibility = View.GONE
        binding.twResult.visibility = View.VISIBLE
        binding.result.visibility = View.VISIBLE
        binding.submitCh.visibility = View.VISIBLE
        binding.editTextCh.visibility = View.VISIBLE
        binding.submitEn.visibility = View.VISIBLE
        binding.editTextEng.visibility = View.VISIBLE
        binding.clearButton.visibility = View.VISIBLE
    }

    override fun onChineseFetched(chinese: String) {
        binding.result.text = chinese
//        viewModel.fetchTaiwanese(chinese)
        activity?.runOnUiThread {
            showAfterSubmit()
        }
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
}