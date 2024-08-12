package com.example.e_commerce.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.databinding.ItemCartBinding
import com.example.e_commerce.databinding.ItemFavoriteBinding
import com.example.e_commerce.databinding.ItemProductBinding

class FavoritesAdapter(
    private val onFavoriteEvent: (ProductDaoModel) -> Unit
) : ListAdapter<ProductDaoModel, FavoritesAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onFavoriteEvent)
    }

    class CartViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDaoModel, onFavoriteEvent: (ProductDaoModel) -> Unit) {
            binding.product = product
            binding.ivFavorite.setOnClickListener { onFavoriteEvent(product) }
            binding.executePendingBindings()
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<ProductDaoModel>() {
        override fun areItemsTheSame(oldItem: ProductDaoModel, newItem: ProductDaoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductDaoModel, newItem: ProductDaoModel): Boolean {
            return oldItem == newItem
        }
    }
}