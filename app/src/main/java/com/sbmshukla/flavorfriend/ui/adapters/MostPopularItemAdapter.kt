package com.sbmshukla.flavorfriend.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbmshukla.flavorfriend.databinding.PopularItemsBinding
import com.sbmshukla.flavorfriend.pojo.CategoryMeals
import com.sbmshukla.flavorfriend.pojo.MealList

class MostPopularItemAdapter() :
    RecyclerView.Adapter<MostPopularItemAdapter.PopularMealViewHolder>() {
    private var mealsList = ArrayList<CategoryMeals>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsLIst: ArrayList<CategoryMeals>) {
        this.mealsList = mealsLIst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        val binding = PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularMealViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
    private var onItemClickListener:((CategoryMeals)->Unit)? = null
    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        val item=mealsList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(item) }
        }
    }

    fun setOnItemClickListener(listener: (CategoryMeals)->Unit){
        onItemClickListener=listener
    }
    class PopularMealViewHolder(private val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryMeals) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}