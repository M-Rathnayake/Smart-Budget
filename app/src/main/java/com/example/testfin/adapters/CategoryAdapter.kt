package com.example.testfin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R
import com.example.testfin.data.CategoryItem

class CategoryAdapter(
    private val categoryData: List<CategoryItem>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvAmount: TextView = itemView.findViewById(R.id.tvCategoryAmount)
        val tvPercentage: TextView = itemView.findViewById(R.id.tvCategoryPercentage)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryData[position]

        holder.tvName.text = category.category
        holder.tvAmount.text = "Rs. %.2f".format(category.amount)
        holder.tvPercentage.text = "%.1f%%".format(category.percentage)

        // You can use actual icons later, here we’re setting a default
        holder.ivIcon.setImageResource(R.drawable.category_default)

        // Progress bar assumes percentage is 0–100
        holder.progressBar.progress = category.percentage.toInt()
    }

    override fun getItemCount(): Int = categoryData.size
}
