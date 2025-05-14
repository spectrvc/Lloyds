package com.art.data.store

import com.art.domain.store.ProductDto
import kotlinx.serialization.Serializable

@Serializable
data class ProductItem(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
)

fun ProductItem.toDto() = ProductDto(
    id = this.id,
    title = this.title,
    price = this.price,
    category = this.category,
    image = this.image
)