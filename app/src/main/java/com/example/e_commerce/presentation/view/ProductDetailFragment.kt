package com.example.e_commerce.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.databinding.FragmentProductDetailBinding
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.extension.Status
import com.example.e_commerce.extension.gone
import com.example.e_commerce.extension.toDaoModel
import com.example.e_commerce.extension.visible
import com.example.e_commerce.presentation.viewmodel.HomeViewModel
import com.example.e_commerce.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private var product: Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        product = arguments?.getParcelable("product")

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() {
        binding.apply {
            product?.let { argsProduct ->
                productData = argsProduct
                toolbar.toolbarTitle.text = argsProduct.name
            }
            toolbar.backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.backButton.visible()

            product?.let { argsProduct ->
                btnAddCart.setOnClickListener {
                    viewModel.addProduct(argsProduct.toDaoModel(ProductType.CART))
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.addProductStatus.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, "Product Added To Cart!", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, resource.message ?: "Some Error", Toast.LENGTH_SHORT).show()
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