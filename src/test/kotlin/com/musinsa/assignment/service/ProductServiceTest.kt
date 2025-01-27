package com.musinsa.assignment.service

import com.musinsa.assignment.domain.Product
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.random.Random

@SpringBootTest
@Transactional
class ProductServiceTest(
    @Autowired
    private val productService:ProductService
):FreeSpec({

    "물품 등록 " - {
        "성공" {
            // given
            val product = Product(
                brand = UUID.randomUUID().toString(),
                category = Product.Category.TOP,
                price = Random.nextInt(0,100000)
            )

            // when
            val productId = productService.register(product)

            // then
            val productTest = productService.findOne(productId)
            productTest.shouldNotBeNull()
            product.shouldBe(productTest)
        }
        "에러" - {
            "brand & category 중복" {

                val testCategory = Product.Category.entries.random()
                val testBrand = UUID.randomUUID().toString()

                val product = Product(
                    brand = testBrand,
                    category = testCategory,
                    price = Random.nextInt(0,100000)
                )

                productService.register(product)

                val duplicatedProduct = Product(
                    brand = testBrand,
                    category = testCategory,
                    price = Random.nextInt(0,100000)
                )

                shouldThrowExactly<IllegalStateException> {
                    productService.register(duplicatedProduct)
                }
            }
        }

    }

    "물품 업데이트" - {
        "성공" {
            // Given
            val product = Product(
                brand = UUID.randomUUID().toString(),
                category = Product.Category.TOP,
                price = Random.nextInt(0, 100000)
            )

            val productId = productService.register(product)

            // when
            val updatedPrice= Random.nextInt(0, 100000)
            val updatedBrand = UUID.randomUUID().toString()
            val updatedCategory = Product.Category.entries.random()

            productService.update(
                id = productId,
                brand = updatedBrand,
                category = updatedCategory,
                price = updatedPrice
            )

            // then
            productService.findOne(productId)
                .shouldNotBeNull()
                .let {
                    it.brand.shouldBe(updatedBrand)
                    it.category.shouldBe(updatedCategory)
                    it.price.shouldBe(updatedPrice)
                }
        }
        "에러" - {
            "물품이 존재하지 않느경우" {

                val updatedPrice= Random.nextInt(0, 100000)
                val updatedBrand = UUID.randomUUID().toString()
                val updatedCategory = Product.Category.entries.random()

                shouldThrowExactly<NullPointerException> {
                    productService.update(
                        id = 0,
                        brand = updatedBrand,
                        category = updatedCategory,
                        price = updatedPrice
                    )
                }
            }
        }
    }

    "삭제" - {
        "성공" {
            // given
            val product = Product(
                brand = UUID.randomUUID().toString(),
                category = Product.Category.TOP,
                price = Random.nextInt(0,100000)
            )
            val productId = productService.register(product)

            // when & then
            productService.deleteById(productId)

        }
        "에러" - {
            "존재하지 않느경우" {
                shouldThrowExactly<NullPointerException> {
                    productService.deleteById(id = 0)
                }
            }
        }
    }
})