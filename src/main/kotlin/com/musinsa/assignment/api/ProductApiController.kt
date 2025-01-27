package com.musinsa.assignment.api

import com.musinsa.assignment.domain.Product
import com.musinsa.assignment.service.ProductService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ProductApiController(
    @Autowired
    val productService: ProductService
) {

    @PostMapping("/products")
    fun registerProduct(@RequestBody @Valid request:RegisterProductRequest):RegisterProductResponse{
        val productId = productService.register(request.toDomain())
        return RegisterProductResponse(productId)
    }

    data class RegisterProductRequest(
        val brand:String,
        val category:String,
        val price:Int,
    ){
        fun toDomain() = Product(
            brand = brand,
            category = Product.Category.valueOf(category),
            price = price
        )
    }
    
    data class RegisterProductResponse(
        val id:Long,
    )
}