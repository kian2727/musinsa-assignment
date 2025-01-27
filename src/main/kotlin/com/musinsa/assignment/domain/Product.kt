package com.musinsa.assignment.domain

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(
    name="products",
    uniqueConstraints = arrayOf(
        UniqueConstraint(name = "UniqueBrandAndCategory", columnNames = ["brand", "category"])
    )
)
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var brand:String,
    @Enumerated(EnumType.STRING)
    var category:Category,
    var price:Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var modifiedAt: LocalDateTime?= LocalDateTime.now(),
){

    enum class Category {
        TOP, //  상의
        OUTER, // 아우터
        PANTS, // 바지
        SNEAKERS, // 스니커즈
        BAG, // 가방
        HAT, // 모자
        SOCKS, // 양말
        ACCESSORY // 악세서리
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}