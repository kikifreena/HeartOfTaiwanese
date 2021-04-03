package edu.mills.heartoftaiwanese.activity.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.adapter.WordListAdapter
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.databinding.FragmentFavoritesBinding

class FavoritesFragment :
    BaseFragment(),
    FavoritesContract.FavoritesView,
    WordListAdapter.FavoriteButtonListener {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter: WordListAdapter by lazy {
        WordListAdapter().apply {
            favoriteButtonListener = this@FavoritesFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.recyclerViewFavorites
        viewModel = FavoritesViewModel()
        viewModel.configure(this, requireContext())
        return binding.root
    }

    override fun onConfigurationSuccess() {
        recyclerView.adapter = adapter
        viewModel.getUpdatedWordList()
    }

    override fun onWordListChanged(newWordList: List<DatabaseWord>) {
        activity?.runOnUiThread {
            if (newWordList.isEmpty()) {
                binding.favoritesZero.visibility = View.VISIBLE
                binding.recyclerViewFavorites.visibility = View.GONE
            } else {
                binding.favoritesZero.visibility = View.GONE
                binding.recyclerViewFavorites.visibility = View.VISIBLE
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
        if (!word.favorite) {
            // For favorite you have to remove the one that's not favorited
            viewModel.getUpdatedWordList()
        }
        return !word.favorite
    }
}
