package com.example.e_commerce.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.databinding.ItemCartBinding

class CartAdapter(
    private val products: MutableList<ProductDaoModel>,
    private val onIncreaseClick: (ProductDaoModel) -> Unit,
    private val onDecreaseClick: (ProductDaoModel) -> Unit
) : ListAdapter<ProductDaoModel, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onIncreaseClick, onDecreaseClick)
    }

    class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDaoModel, onIncreaseClick: (ProductDaoModel) -> Unit, onDecreaseClick: (ProductDaoModel) -> Unit) {
            binding.product = product
            binding.btnIncrease.setOnClickListener { onIncreaseClick(product) }
            binding.btnDecrease.setOnClickListener { onDecreaseClick(product) }
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