package com.example.dz12customlistview2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel:ViewModel() {
    var products: MutableList<Product> = mutableListOf()
    val listProduct: MutableLiveData<List<Product>> by lazy { MutableLiveData<List<Product>>(mutableListOf()) }

    init {
        listProduct.value = products
    }

    fun addProduct(product: Product) {
        products.add(product)

    }
}