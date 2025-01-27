package com.musinsa.assignment.service

import com.musinsa.assignment.domain.Product
import com.musinsa.assignment.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ProductService(
    @Autowired
    private val productRepository: ProductRepository,
) {

    @Transactional
    fun register(product: Product):Long {
        validateDuplicateBrandAndCategory(product.brand, product.category)
        productRepository.save(product)
        return product.id!!
    }

    @Transactional
    fun update(id:Long, brand:String, category: Product.Category, price:Int): Product {
        return productRepository.findOne(id)?.let {
            if( it.brand != brand || it.category != category) {
                validateDuplicateBrandAndCategory(brand, category)
            }
            it.brand = brand
            it.category = category
            it.price = price
            it.modifiedAt = LocalDateTime.now()
            it
        } ?: throw NullPointerException("해당 물품이 존재 하지 않습니다. [id=$id]")
    }

    fun findOne(productId:Long):Product? = productRepository.findOne(productId)

    fun deleteById(id: Long) = productRepository.findOne(id)?.let { productToDelete ->
            productRepository.delete(productToDelete)
    }?:throw NullPointerException("해당 물품이 존재 하지 않습니다. [id=$id]")

    /**
     * 같은 brand & category 가 존재하는지 체크하는 함수
     */
    private fun validateDuplicateBrandAndCategory(brand: String, category: Product.Category) {
        val findProduct = productRepository.findByBrandAndCategory(
            brand = brand, category = category
        )

        if( findProduct != null ) {
            throw IllegalStateException("이미 존재하는 brand , category 입니다. [brand = $brand, category = $category]")
        }
    }

}