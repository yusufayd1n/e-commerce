package com.example.e_commerce.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
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
import com.google.android.material.internal.ViewUtils.hideKeyboard
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
        setupSearchView()
        observeViewModel()
    }

    private fun setAdapter() {
        productsAdapter = ProductAdapter(
            onFavoriteClick = {
                viewModel.addProduct(it, ProductType.FAVORITE)
            },
            onProductClick = { product ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
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
                    if (!binding.rvProducts.isLoading && lastVisibleItem >= totalItemCount - 1 && binding.etSearch.text.isNullOrEmpty()) {
                        binding.rvProducts.isLoading = true
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun setupSearchView() {
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.etSearch.text.toString()
                viewModel.searchOrLoadProducts(query)
                //hideKeyboard(binding,true)  // Klavyeyi kapat
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getProducts.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.rvProducts.isLoading = false
                    resource.data?.let { products ->
                        productsAdapter.submitList(products)
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

        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    resource.data?.let { products ->
                        productsAdapter.submitList(products)
                    }
                }

                Status.ERROR -> {
                    binding.progressBar.gone()
                    binding.tvError.visible()
                    binding.tvError.text = resource.message
                }

                Status.LOADING -> {
                    binding.progressBar.visible()
                }
            }
        }
    }

   /* override fun onResume() {
        super.onResume()
        // Sayfa numarasını sadece gerekli olduğunda artırın
        if (viewModel.page == 1) {
            viewModel.getProducts()
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}