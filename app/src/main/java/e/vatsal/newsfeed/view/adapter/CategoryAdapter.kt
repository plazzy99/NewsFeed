package e.vatsal.newsfeed.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import coil.load
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.PairModel
import e.vatsal.newsfeed.databinding.ItemCategoryBinding
import e.vatsal.newsfeed.view.fragment.NewsFeedFragment

class CategoryAdapter(
    private val list: ArrayList<PairModel>,
    private val openedFrom: Int,
    val click: (name: String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.ViewHolder =
        ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            when (openedFrom) {
                0 -> {
                    // home
                    root.layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                }
                1 -> {
                    // inner class
                    root.layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                }
            }

            image.load(list[position].image)
            tvCategoryName.text = list[position].name
        }

        holder.itemView.setOnClickListener {
            click.invoke(list[position].name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}