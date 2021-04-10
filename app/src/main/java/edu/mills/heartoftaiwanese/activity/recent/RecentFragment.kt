package edu.mills.heartoftaiwanese.activity.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.adapter.WordListAdapter
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.databinding.FragmentRecentBinding

class RecentFragment :
    BaseFragment(),
    RecentContract.RecentView,
    WordListAdapter.FavoriteButtonListener {
    private lateinit var binding: FragmentRecentBinding
    private lateinit var viewModel: RecentViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter: WordListAdapter by lazy {
        WordListAdapter().apply {
            favoriteButtonListener = this@RecentFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerViewRecent
        viewModel = RecentViewModel()
        viewModel.configure(this, requireContext())
        return binding.root
    }

    override fun onConfigurationSuccess() {
        recyclerView.adapter = adapter
        viewModel.updateWordList()
    }

    override fun onWordListChanged(newWordList: List<DatabaseWord>) {
        activity?.runOnUiThread {
            if (newWordList.isEmpty()) {
                binding.favoritesZero.visibility = View.VISIBLE
                binding.recyclerViewRecent.visibility = View.GONE
            } else {
                binding.favoritesZero.visibility = View.GONE
                binding.recyclerViewRecent.visibility = View.VISIBLE
                adapter.submitList(newWordList)
            }
        }
    }

    /**
     * Interface function that determines what can happen if the favorite is clicked.
     * The view adapter takes care of hiding/showing the favorite button.
     *
     * @return the new value of the favorite
     */
    override fun onFavoriteClicked(word: DatabaseWord): Boolean {
        viewModel.favoriteWord(word)
        return !word.favorite
    }
}
