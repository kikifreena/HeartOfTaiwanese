package edu.mills.heartoftaiwanese.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.data.Language
import edu.mills.heartoftaiwanese.data.LanguageContainer
import edu.mills.heartoftaiwanese.databinding.FragmentHomeBinding
import edu.mills.heartoftaiwanese.network.WordRetriever
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

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putString(kSavedChineseText, binding.editTextCh.text.toString())
        state.putString(kSavedEnglishText, binding.editTextEng.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE

            WordRetriever("").ParseWordTask().execute(
                LanguageContainer(
                    binding.editTextCh.text.toString(),
                    Language.LANGUAGE_CHINESE
                )
            )
        }
        binding.submitEn.setOnClickListener {
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
            WordRetriever("").ParseWordTask().execute(
                LanguageContainer(
                    binding.editTextEng.text.toString(),
                    Language.LANGUAGE_ENGLISH
                )
            )
        }
        binding.clearButton.setOnClickListener {
            binding.editTextCh.setText("")
            binding.editTextEng.setText("")
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
        }
    }

    override fun onChineseFetched() {
        TODO("Not yet implemented")
    }

    override fun onTaiwaneseFetched() {
        TODO("Not yet implemented")
    }
}