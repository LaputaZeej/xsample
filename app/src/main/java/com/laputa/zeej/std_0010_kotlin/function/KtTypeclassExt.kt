import com.laputa.zeej.std_0010_kotlin.function.*

interface Key<T>

interface KindX<out F : Key<*>, out A>

interface FunctorX<F : Key<*>> {
    fun <A, B> KindX<F, A>.map(f: (A) -> B): KindX<F, B>
}


// KtList
sealed class KtListX<out A> : KindX<KtListX.KEY, A> {
    companion object KEY : Key<KtListX<*>>
}

@Suppress("NOTHING_TO_INLINE")
inline fun <A> KindX<KtListX.KEY, A>.unwrap(): KtListX<A> = this as KtListX<A>

object NilX : KtListX<Nothing>()
data class ConsX<A>(val head: A, val tail: KtListX<A>) : KtListX<A>()

// 实现函子
object KtListXFunctor : FunctorX<KtListX.KEY> {
    override fun <A, B> KindX<KtListX.KEY, A>.map(f: (A) -> B): KindX<KtListX.KEY, B> {
        return when (this) {
            is ConsX<A> -> {
                ConsX(f(this.head), this.tail.map(f).unwrap())
            }
            else -> NilX
        }
    }


}

fun <A, B> KindX<KtListX.KEY, A>.mapExt(f: (A) -> B): KindX<KtListX.KEY, B> {
    return when (this) {
        is ConsX<A> -> {
            ConsX(f(this.head), this.tail.mapExt(f).unwrap())
        }
        else -> NilX
    }
}

fun main() {
//    KtListXFunctor.run {
//        val old = ConsX("11", NilX)
//        println(old)
//        val result = old.map {
//            "map 后的东西 ${it.toInt() * 333}"
//        }
//        println(result)
//    }

    val old = ConsX("11", NilX)
    println(old)
    val result = old.mapExt {
        "map 后的东西 ${it.toInt() * 333}"
    }
    println(result)

}