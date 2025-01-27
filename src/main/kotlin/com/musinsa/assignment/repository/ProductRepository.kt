package com.musinsa.assignment.repository

import com.musinsa.assignment.domain.Product
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    @PersistenceContext
    val em: EntityManager
) {

    fun save(product: Product) {
        if( product.id == null ) {
            em.persist(product)
        } else {
            em.merge(product)
        }
    }

    fun findOne(id:Long): Product? = em.find(Product::class.java, id)

    fun findByBrandAndCategory(brand: String, category: Product.Category):Product?=
        em.createQuery(
            "select p from Product p where p.brand = :brand and p.category = :category order by p.category", Product::class.java)
            .setParameter("brand", brand)
            .setParameter("category", category)
            .resultList.firstOrNull()

    fun delete(product: Product) = em.remove(product)
}