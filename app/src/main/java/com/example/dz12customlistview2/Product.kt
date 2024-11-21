package com.example.dz12customlistview2

import java.io.Serializable


class Product(
    val name: String?,
    val price: String?,
    val image: String?) : Serializable {

    override fun toString(): String {
        return "Продукт $name, цена $price"
    }
}