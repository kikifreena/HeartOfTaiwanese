package edu.mills.heartoftaiwanese.activity.adapter

import androidx.recyclerview.widget.DiffUtil
import edu.mills.heartoftaiwanese.data.DatabaseWord

object WordsCallback : DiffUtil.ItemCallback<DatabaseWord>() {
    override fun areItemsTheSame(oldWord: DatabaseWord, newWord: DatabaseWord) =
        // User properties may have changed if reloaded from the DB, but ID is fixed
        oldWord.id == newWord.id

    override fun areContentsTheSame(oldWord: DatabaseWord, newWord: DatabaseWord) =
        oldWord == newWord
}
