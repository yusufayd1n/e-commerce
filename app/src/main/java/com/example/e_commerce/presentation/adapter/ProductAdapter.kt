package com.example.e_commerce.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ItemProductBinding
import com.example.e_commerce.domain.model.Product

class ProductAdapter(
    private val onAddToCartClick: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit,
    private val onFavoriteClick: (Product) -> Unit,
    private var favoriteProducts: List<String>
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateFavoriteProducts(favoriteProducts: List<String>) {
        this.favoriteProducts = favoriteProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.ivFavorite.setImageResource(
                if (favoriteProducts.contains(product.name)) R.drawable.ic_star
                else R.drawable.ic_empty_star
            )
            binding.btnAddCart.setOnClickListener {
                onAddToCartClick(product)
            }
            binding.root.setOnClickListener {
                onProductClick(product)
            }
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(product)
            }
            binding.executePendingBindings()
        }
    }


    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}


