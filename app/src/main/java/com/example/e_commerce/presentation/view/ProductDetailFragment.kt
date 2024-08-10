package com.example.e_commerce.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentProductDetailBinding
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.extension.visible

class ProductDetailFragment : Fragment() {

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
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}