package edu.mills.heartoftaiwanese.activity.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.data.Word
import edu.mills.heartoftaiwanese.databinding.LayoutWordItemBinding
import java.text.DateFormat

class WordListAdapter :
    ListAdapter<DatabaseWord, WordListAdapter.WordListViewHolder>(WordsCallback) {

    var favoriteButtonListener: FavoriteButtonListener? = null

    interface FavoriteButtonListener {
        /**
         * Interface function that determines what can happen if the favorite is clicked.
         * The view adapter takes care of hiding/showing the favorite button.
         *
         * @return the new value of the favorite
         */
        fun onFavoriteClicked(word: DatabaseWord): Boolean
    }

    inner class WordListViewHolder(private val binding: LayoutWordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = DateFormat.getDateInstance(DateFormat.LONG)

        private var isFavorite: Boolean = false
            set(value) {
                field = value
                if (value) {
                    // Display the favorite button as colorPrimary
                    binding.favoriteSelector.imageTintList = ColorStateList.valueOf(
                        binding.root.resources.getColor(R.color.colorPrimaryDark, null)
                    )
                } else {
                    // Unfavorite; clear the favorite button.
                    binding.favoriteSelector.imageTintList = ColorStateList.valueOf(
                        binding.root.resources.getColor(R.color.buttonUnselectedGray, null)
                    )
                }
            }

        fun bind(databaseWord: DatabaseWord) {
            displayWord(databaseWord.word)
            isFavorite = databaseWord.favorite
            binding.tvLastAccessed.text = dateFormat.format(databaseWord.accessTime)
            binding.favoriteSelector.setOnClickListener {
                favoriteButtonListener?.let {
                    isFavorite = it.onFavoriteClicked(databaseWord)
                }
            }
        }

        private fun displayWord(word: Word) {
            binding.tvRightText.text = word.taiwanese
            binding.tvWordTitle.text = word.taiwanese

            binding.tvCenterText.text = word.chinese
            binding.tvLeftText.text = word.english
        }
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListViewHolder {
        return WordListViewHolder(
            LayoutWordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: WordListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
