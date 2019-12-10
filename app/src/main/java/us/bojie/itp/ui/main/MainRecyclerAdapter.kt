package us.bojie.itp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.list_items.view.*
import us.bojie.itp.R
import us.bojie.itp.model.Movie

// https://gist.githubusercontent.com/mitchtabian/e194485eee68a7846feb27f1ce1067b3/raw/f5c759b3ec1d383ae3070876a3fa5222fc953a4b/ListAdapterTemplate.kt
class MainRecyclerAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_items,
                parent,
                false
            ),
            interaction,
            requestManager
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Movie>) {
        differ.submitList(list)
    }

    class MovieViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val requestManager: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            requestManager.load(item.Poster).into(itemView.movie_thumbnail)
            itemView.textView.text = item.Title
            itemView.textView2.text = item.Year
            if (adapterPosition % 2 == 0) {
                itemView.setBackgroundColor(context.getColor(android.R.color.white))
            } else {
                itemView.setBackgroundColor(context.getColor(android.R.color.holo_blue_light))
            }
            itemView.imageButton.setOnClickListener {
                interaction?.onDeleteButtonClicked(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Movie)
        fun onDeleteButtonClicked(position: Int, item: Movie)
    }
}