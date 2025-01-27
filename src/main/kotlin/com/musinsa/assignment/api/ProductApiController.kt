package com.musinsa.assignment.api

import com.musinsa.assignment.domain.Product
import com.musinsa.assignment.service.ProductService
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.PermissionDeniedDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ProductApiController(
    @Autowired
    val productService: ProductService
) {

    @PostMapping("/products")
    fun registerProduct(
        @RequestHeader header: HttpHeaders,
        @RequestBody @Valid request:RegisterProductRequest):RegisterProductResponse{
        validAuthority(header)
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

    @PutMapping("/products/{id}")
    fun updateProduct(
        @RequestHeader header: HttpHeaders,
        @PathVariable("id") id: Long,
        @RequestBody @Valid request:UpdateProductRequest
    ):UpdateProductResponse{
        validAuthority(header)
        val updatedProduct = productService.update(
            id = id,
            brand = request.brand,
            category = request.category.toCategoryDomain(),
            price = request.price
        )
        return UpdateProductResponse(
            id = updatedProduct.id!!,
            brand = updatedProduct.brand,
            category = updatedProduct.category.toResponse(),
            price = updatedProduct.price,
        )
    }

    // TODO jwt token 으로 권한체크 필요
    private fun validAuthority(header: HttpHeaders){
        if( header.getFirst("isAdmin").equals("true", ignoreCase = true).not()){
            throw PermissionDeniedException("관리자 권한이 아닙니다.")
        }
    }

    data class UpdateProductRequest(
        val brand:String,
        val category:String,
        val price: Int
    )

    private fun String.toCategoryDomain() = when(this){
        "TOP" -> Product.Category.TOP
        "OUTER" -> Product.Category.OUTER
        "PANTS" -> Product.Category.PANTS
        "SNEAKERS" -> Product.Category.SNEAKERS
        "BAG" -> Product.Category.BAG
        "HAT" -> Product.Category.HAT
        "SOCKS" -> Product.Category.SOCKS
        "ACCESSORY" -> Product.Category.ACCESSORY
        else -> throw IllegalArgumentException("Unknown category type")
    }

    private fun Product.Category.toResponse() = when(this){
        Product.Category.TOP -> "TOP"
        Product.Category.OUTER -> "OUTER"
        Product.Category.PANTS -> "PANTS"
        Product.Category.SNEAKERS -> "SNEAKERS"
        Product.Category.BAG -> "BAG"
        Product.Category.HAT -> "HAT"
        Product.Category.SOCKS -> "SOCKS"
        Product.Category.ACCESSORY -> "ACCESSORY"
    }

    data class UpdateProductResponse(
        val id:Long,
        val brand:String,
        val category:String,
        val price:Int,
    )

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @RequestHeader header: HttpHeaders,
        @PathVariable("id") id: Long,
    ):ResponseEntity<Nothing>{
        validAuthority(header)
        productService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}


