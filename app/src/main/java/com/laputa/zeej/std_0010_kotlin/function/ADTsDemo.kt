package com.laputa.zeej.std_0010_kotlin.function

// 优惠卷

internal class NotGoodCoupon {
    val id: Long = 0
    val type: String = ""

    // 代金卷使用满足leastCost减少reduceCost
    val leastCost: Long? = null
    val reduceCost: Long? = null

    // 折扣劵使用
    val discount: Int? = null

    // 礼品劵使用
    val gift: String? = null

    companion object {
        const val CashType = "CASH"
        const val DiscountType = "DISCOUNT"
        const val GiftType = "GIFT"
    }
}

// use adt
sealed class NotGood2Coupon(val id: Long, val type: String) {

    // 是否领取
    fun fetched(c: NotGood2Coupon, user: String): Boolean {
        return false
    }

    // 是否使用
    fun used(c: NotGood2Coupon, user: String): Boolean {
        return false
    }

    // 是否过期
    fun isExpired(c: NotGood2Coupon): Boolean {
        return false
    }

    // 时候失效
    fun isUnAviable(c: NotGood2Coupon): Boolean {
        return false
    }

    companion object {
        const val CashType = "CASH"
        const val DiscountType = "DISCOUNT"
        const val GiftType = "GIFT"

        const val NotFetched = 1
        const val Fetched = 2
        const val Used = 3
        const val Expired = 4
        const val UnAviable = 5
    }

    // bad
    fun getCouponStatus(coupon: NotGood2Coupon, user: String): Int {
        return when {
            isUnAviable(coupon) -> UnAviable
            isExpired(coupon) -> Expired
            used(coupon, user) -> Used
            fetched(coupon, user) -> Fetched
            else -> NotFetched
        }
    }
}

class CashCoupon2(id: Long, type: String, val leastCont: Long, val reduceCost: Long) :
    NotGood2Coupon(id, type)

class DiscountCoupon2(id: Long, type: String, discount: String) : NotGood2Coupon(id, type)
class GiftCoupon2(id: Long, type: String, gift: String) : NotGood2Coupon(id, type)

//
sealed class Coupon(val id: Long, val type: String){

    // 是否领取
    fun fetched(c: Coupon, user: String): Boolean {
        return false
    }

    // 是否使用
    fun used(c: Coupon, user: String): Boolean {
        return false
    }

    // 是否过期
    fun isExpired(c: Coupon): Boolean {
        return false
    }

    // 时候失效
    fun isUnAviable(c: Coupon): Boolean {
        return false
    }
    companion object{
        const val NotFetched = 1
        const val Fetched = 2
        const val Used = 3
        const val Expired = 4
        const val UnAviable = 5
    }

    fun getCouponStatus(coupon: Coupon,user: String):CouponStatus = when{
        isUnAviable(coupon) -> UnAviableStatus(coupon)
        isExpired(coupon) -> ExpiredStatus(coupon)
        used(coupon, user) -> UsedStatus(coupon,user)
        fetched(coupon, user) -> FetchedStatus(coupon,user)
        else -> NotFetchStatus(coupon)
    }

}
sealed class CouponStatus

// 优惠卷
class CashCoupon(id: Long, type: String, val leastCont: Long, val reduceCost: Long) :
    Coupon(id, type)

class DiscountCoupon(id: Long, type: String, discount: String) : Coupon(id, type)
class GiftCoupon(id: Long, type: String, gift: String) : Coupon(id, type)

// 优惠卷状态
data class NotFetchStatus(val coupon: Coupon) : CouponStatus()
data class FetchedStatus(val coupon: Coupon, val user: String) : CouponStatus()
data class UsedStatus(val coupon: Coupon, val user: String) : CouponStatus()
data class ExpiredStatus(val coupon: Coupon) : CouponStatus()
data class UnAviableStatus(val coupon: Coupon) : CouponStatus()



