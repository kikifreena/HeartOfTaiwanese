package edu.mills.heartoftaiwanese.activity.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import edu.mills.heartoftaiwanese.activity.adapter.WordListAdapter
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.databinding.FragmentRecentBinding

class RecentFragment : Fragment(), RecentContract.RecentView,
    WordListAdapter.FavoriteButtonListener {
    private lateinit var binding: FragmentRecentBinding
    private lateinit var viewModel: RecentContract.RecentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordListAdapter

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
        adapter = WordListAdapter().apply {
            favoriteButtonListener = this@RecentFragment
        }
        recyclerView.adapter = adapter
    }

    /**
     * Interface function that determines what can happen if the favorite is clicked.
     * The view adapter takes care of hiding/showing the favorite button.
     *
     * @return the new value of the favorite
     */
    override fun onFavoriteClicked(word: DatabaseWord): Boolean {
        viewModel.favoriteWord(word)
        TODO("Not yet implemented")
    }

    override fun onWordListChanged(newWordList: List<DatabaseWord>) {
        adapter.submitList(newWordList)
    }
}
