package com.example.e_commerce.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.extension.Status
import com.example.e_commerce.extension.gone
import com.example.e_commerce.extension.isLoading
import com.example.e_commerce.extension.visible
import com.example.e_commerce.presentation.adapter.ProductAdapter
import com.example.e_commerce.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setAdapter() {
        productsAdapter = ProductAdapter(
            onFavoriteClick = {
                viewModel.addProduct(it, ProductType.FAVORITE)
            },
            onProductClick = { product ->
                val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
                findNavController().navigate(action)
            },
            onAddToCartClick = {
                viewModel.addProduct(it, ProductType.CART)
            }
        )
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (!binding.rvProducts.isLoading && lastVisibleItem >= totalItemCount - 1) {
                        binding.rvProducts.isLoading = true
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.getProducts.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.rvProducts.isLoading = false
                    resource.data?.let { products ->
                        val currentList = productsAdapter.currentList.toMutableList()
                        currentList.addAll(products)
                        productsAdapter.submitList(currentList)
                    }
                }

                Status.ERROR -> {
                    binding.progressBar.gone()
                    binding.tvError.visible()
                    binding.rvProducts.isLoading = false
                    binding.tvError.text = resource.message
                }

                Status.LOADING -> {
                    binding.progressBar.visible()
                    binding.rvProducts.isLoading = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}