package com.example.e_commerce.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.extension.Status
import com.example.e_commerce.extension.gone
import com.example.e_commerce.extension.visible
import com.example.e_commerce.presentation.adapter.CartAdapter
import com.example.e_commerce.presentation.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding ?: throw IllegalStateException("Binding is not initialized")
    private lateinit var cartAdapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setAdapter()
        observeData()
    }

    private fun initView() {
        binding.toolbar.apply {
            toolbarTitle.text = getString(R.string.e_market)
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            backButton.gone()
        }
        binding.tvPrice.text = viewModel.calculateCartAmount().toString()
    }

    private fun setAdapter() {
        cartAdapter =
            CartAdapter(
                onIncreaseClick = { product ->
                    viewModel.addProduct(product)
                },
                onDecreaseClick = { product ->
                    viewModel.removeProduct(product)
                }
            )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = cartAdapter
    }

    private fun observeData() {
        viewModel.getProductsFromLocalDatabase.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.recyclerView.visible()
                    cartAdapter.submitList(resource.data)
                    binding.tvPrice.text = viewModel.calculateCartAmount().toString()
                }

                Status.ERROR -> {
                    binding.progressBar.gone()
                    binding.recyclerView.gone()
                    Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {
                    binding.recyclerView.gone()
                    binding.progressBar.visible()
                }
            }
        }

        viewModel.addProductStatus.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, "Product Added To Cart!", Toast.LENGTH_SHORT).show()
                }

                Status.ERROR -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, resource.message ?: "Some Error", Toast.LENGTH_SHORT)
                        .show()
                }

                Status.LOADING -> {
                    binding.progressBar.visible()
                }
            }
        }

        viewModel.removeProductStatus.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, "Product Delete Success", Toast.LENGTH_SHORT).show()
                }

                Status.ERROR -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, resource.message ?: "Some Error", Toast.LENGTH_SHORT)
                        .show()
                }

                Status.LOADING -> {
                    binding.progressBar.visible()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProductsByType()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}