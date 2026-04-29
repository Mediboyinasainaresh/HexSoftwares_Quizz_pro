package com.example.quizz.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizz.R
import com.example.quizz.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tvCategoryEmoji)
        val tvName: TextView = view.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.tvName.text = category.name
        holder.tvEmoji.text = category.emoji
        
        holder.itemView.setOnClickListener {
            // Animation for click
            it.animate().translationX(2f).translationY(2f).setDuration(50).withEndAction {
                it.animate().translationX(0f).translationY(0f).setDuration(50).withEndAction {
                    onCategoryClick(category)
                }
            }
        }
    }

    override fun getItemCount() = categories.size
}