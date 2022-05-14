package e.vatsal.newsfeed.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import e.vatsal.newsfeed.data.model.Article
import e.vatsal.newsfeed.data.model.NewsModel
import e.vatsal.newsfeed.databinding.ItemHeadlinesBinding

class TopHeadLinesAdapter(
    private val list: NewsModel,
    val click: (Article) -> Unit
) : RecyclerView.Adapter<TopHeadLinesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHeadlinesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopHeadLinesAdapter.ViewHolder =
        ViewHolder(
            ItemHeadlinesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TopHeadLinesAdapter.ViewHolder, position: Int) {
        if (list.articles?.get(position)?.title == null) {
            holder.binding.root.layoutParams.height = 0
        } else {
            holder.binding.root.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        holder.binding.apply {
            imNewsImage.load(list.articles?.get(position)?.urlToImage)
            tvNewsTitle.text = list.articles?.get(position)?.title
            tvNewsAuthor.text =
                list.articles?.get(position)?.author ?: list.articles?.get(position)?.publishedAt
        }

        holder.itemView.setOnClickListener {
            list.articles?.get(position)?.let { it1 ->
                click.invoke(it1)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.articles?.size ?: 0
    }
}