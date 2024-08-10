package com.example.e_commerce.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.extension.Status
import com.example.e_commerce.extension.gone
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
        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
        productsAdapter = ProductAdapter(
            onFavoriteClick = {
                Log.d("YUSUFAYDIN","onFavoriteClick")
            },
            onProductClick = {
                Log.d("YUSUFAYDIN","onProductClick")
            },
            onAddToCartClick = {
                Log.d("YUSUFAYDIN","onAddToCartClick")
            }
        )

        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.height = (height / 2)
                return true
            }
        }
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.getProducts.observe(viewLifecycleOwner) { resource ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}