package edu.mills.heartoftaiwanese.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.mills.heartoftaiwanese.WordRetriever
import edu.mills.heartoftaiwanese.data.Language
import edu.mills.heartoftaiwanese.data.LanguageContainer
import edu.mills.heartoftaiwanese.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    companion object {
        const val LANGUAGE_ENGLISH = 1
        const val LANGUAGE_CHINESE = 0
        private const val kSavedChineseText = "SavedChineseText"
        private const val kSavedEnglishText = "SavedEnglishText"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        initializeClickListeners()
        return binding.root
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
}