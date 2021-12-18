package com.laputa.zeej.std_0010_kotlin.function

import com.laputa.zeej.std_0010_kotlin.function.KtListFunctor.map

// Monoid

// 定义Typeclass
interface Monoid<A> {
    fun zero(): A
    fun A.append(b: A): A
}

// String
internal object StringMonoid : Monoid<String> {
    override fun zero(): String = ""

    override fun String.append(b: String): String = this + b
}

// 为之前的KtList扩展sum操作
fun <A> KtList<A>.sum(ma: Monoid<A>): A {
    val fa = this
    return ListFoldable.run {
        fa.fold(ma.zero()).invoke { acc, cur ->
            ma.run {
                acc.append(cur)
            }
        }
    }
}

// 那么就可以使用sum了

// OptionKind
sealed class OptionKind<out A> : Kind<OptionKind.K, A> {
    object K // 这个就代表类型即F[_]
}

// EffectKind
sealed class EffectKind<out A> : Kind<EffectKind.K, A> {
    object K // 这个就代表类型即F[_]
}

// ParsertKind
sealed class ParsertKind<out A> : Kind<ParsertKind.K, A> {
    object K // 这个就代表类型即F[_]
}

object OptionFunctor : Functor<OptionKind.K> {
    override fun <A, B> Kind<OptionKind.K, A>.map(f: (A) -> B): Kind<OptionKind.K, B> {
        TODO("Not yet implemented")
    }
}

object EffectFunctor : Functor<EffectKind.K> {
    override fun <A, B> Kind<EffectKind.K, A>.map(f: (A) -> B): Kind<EffectKind.K, B> {
        TODO("Not yet implemented")
    }
}

object ParserFunctor : Functor<ParsertKind.K> {
    override fun <A, B> Kind<ParsertKind.K, A>.map(f: (A) -> B): Kind<ParsertKind.K, B> {
        TODO("Not yet implemented")
    }
}

// 函子定律
// 同一律
internal fun <A> identity(a: A) = a

internal fun test00001() {
    println(KtListFunctor.run {
        Cons(1, Nil).map {
            identity(it)
        }
    })
}

// 结合律
internal fun f(a: Int) = a + 1
internal fun g(a: Int) = a * 1
internal fun test00002() {
    KtListFunctor.run {
        val s1 = Cons(1, Nil).map(::f).map(::g)
        val s2 = Cons(1, Nil).map { f(g(it)) }
        println(s1 == s2)
    }
}

interface UnKtList<F> {

    fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B>
    fun <A, B, C> map2(fa: Kind<F, A>, fb: Kind<F, B>, f: (A, B) -> C): Kind<F, C>//比较局限
}


// 伪代码 scala
interface UnFunctor<F> {
    // 也叫做unit（haskell里为return flatmap对应bind）
    // 将A类型的参数，转化为Kind<F,A>类型
    fun <A> pure(a: A): Kind<F, A>

    // 因为map2比较局限，有副作用？
    // 推迟执行
    // 所以扩展一个flatmap，替代不合理的map2
    // 是map和flatten两个操作之和
    // 比如我们可能得到一个一个嵌套容器，Kind<F,A>进行map操作，返回一个Kind<F,B>的函数，得到的结果将是
    // Kind<F,Kin<F,B>>,需要一个flatten操作
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>
    fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> {
        return flatMap(fa) {
            pure(f(it))
        }
    }

    fun <A, B, C> map2(fa: Kind<F, A>, fb: Kind<F, B>, f: (A, B) -> C): Kind<F, C> {
        return flatMap(fa) { a ->
            flatMap(fb) { b ->
                pure(f(a, b))
            }
        }
    }

    fun <A, B, C> map3(fa: Kind<F, A>, fb: Kind<F, B>, f: (A, B) -> C): Kind<F, C> {
        return flatMap(fa) { a ->
            map(fb) { b ->
                f(a, b)
            }
        }
    }

    /* fun <A, B> Kind<F, A>.flatMap1(f: (A) -> Kind<F, B>): Kind<F, B>
     fun <A, B> map1(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> {
         return fa.run {
             flatMap1 {
                 pure(f(it))
             }
         }
     }*/
}

// Monoid & Monoid Typeclass 区别
// Monoid 是满足Monoid法则的最小集的实现,可以被成为单子.
// Monoid Typeclass 是Monoid的实现,不唯一,有多种实现方式
// 我们自己实现一种Monoid Typeclass：同时包含了pure和flatMap操
interface FirstMonad<F> : Functor<F> { // 替代了以前的函子Factor；后面的理解：直接继承
    fun <A> pure(a: A): Kind<F, A>
    fun <A, B> Kind<F, A>.flatMap(f: (A) -> Kind<F, B>): Kind<F, B>

    // 这样默认的map就可以根据上面两个原操作实现了 **** 1.放在类里 **** 哪一种合理呢
    override fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B> {
        val fa = this
        return fa.flatMap {
            pure(f(it))
        }
    }

    companion object {
        // 这样默认的map就可以根据上面两个原操作实现了 **** 2. 伴生对象里的 扩展函数 **** 哪一种合理呢
        // 后面的理解 这个可以扩展其他的通用的操作符
        fun <A, B, F> FirstMonad<F>.mapOut(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> {
            return fa.flatMap {
                pure(f(it))
            }
        }
    }
}

object ListMonad : FirstMonad<KtList.K> {

    // 链表的append
    private fun <A> append(fa: Kind<KtList.K, A>, fb: Kind<KtList.K, A>): Kind<KtList.K, A> {
        return if (fa is Cons) {
            Cons(fa.head, append(fa.tail, fb).unwrap())
        } else {
            fb
        }
    }

    override fun <A> pure(a: A): Kind<KtList.K, A> {
        return Cons(a, Nil)
    }

    /**
     * 先map，再展开
     */
    override fun <A, B> Kind<KtList.K, A>.flatMap(f: (A) -> Kind<KtList.K, B>): Kind<KtList.K, B> {
        val fa = this
        val empty: Kind<KtList.K, B> = Nil
        // 返回的是一个函数类型，是一个延迟操作；需要再次使用这个函数才能得到B
        return ListFoldable.run {
            //3.展开
            fa.fold(empty).invoke { acc, cur ->
                // 2.然后List的append操作，形成新的List
                append(acc, /*1.这里是map，将数据A->B*/f(cur))
            }
        }
    }
}

//// 实现map 不用单独实现map了
//fun <A, B> Kind<KtList.K, A>.map(f: (A) -> B): Kind<KtList.K, B> {
//    val fa = this
//    return ListMonad.run {
//        fa.flatMap {
//            pure(f(it))
//        }
//    }
//}


// Functor -> Applicative -> Monad
interface Applicative<F> : Functor<F> {
    fun <A> pure(a: A): Kind<F, A>
    fun <A, B> Kind<F, A>.ap(f: (Kind<F, (A) -> B>)): Kind<F, B> // 这一层是介于Functor和Monad之间

    override fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B> {
        return ap(pure(f))
    }
}

// 重新定义Monad
interface Monad<F> : Applicative<F> { // 替代了以前的函子Factor；后面的理解：直接继承
    fun <A, B> Kind<F, A>.flatMap(f: (A) -> Kind<F, B>): Kind<F, B>

    override fun <A, B> Kind<F, A>.ap(f: Kind<F, (A) -> B>): Kind<F, B> {
        return f.flatMap {
            this.flatMap { a ->
                pure(it(a))
            }
        }
    }
}

object NewListMonad : Monad<KtList.K> {
    // 链表的append
    private fun <A> append(fa: Kind<KtList.K, A>, fb: Kind<KtList.K, A>): Kind<KtList.K, A> {
        return if (fa is Cons) {
            Cons(fa.head, append(fa.tail, fb).unwrap())
        } else {
            fb
        }
    }

    override fun <A> pure(a: A): Kind<KtList.K, A> {
        return Cons(a, Nil)
    }

    /**
     * 先map，再展开
     */
    override fun <A, B> Kind<KtList.K, A>.flatMap(f: (A) -> Kind<KtList.K, B>): Kind<KtList.K, B> {
        val fa = this
        val empty: Kind<KtList.K, B> = Nil
        // 返回的是一个函数类型，是一个延迟操作；需要再次使用这个函数才能得到B
        return ListFoldable.run {
            //3.展开
            fa.fold(empty).invoke { acc, cur ->
                // 2.然后List的append操作，形成新的List
                append(acc, /*1.这里是map，将数据A->B*/f(cur))
            }
        }
    }

}

fun main() {
    println("*********************** StringMonoid")
    println(StringMonoid.run {
        "kotlin".append(" hello world")
    })
    println("*********************** sum")
    println(
        Cons("hello", Cons("_laputa", Nil)).sum(StringMonoid)
    )
    println("*********************** 同一律")
    test00001()

    println("*********************** 结合律")
    test00002()

    println("*********************** Monad")
    val c1 = Cons("1", Nil)
    val c2 = Cons("2", c1)
    val result = ListMonad.run {
        c2.map {
            it.toInt() * 3
        }
    }
    println(result)
    println("*********************** Monad 2")

    val result2 =
        FirstMonad.run {
            c2.map {
                it.toInt() * 3
            }
        }
    println(result2)

    println("*********************** Applicative")
    val result3 =
        NewListMonad.run {
            c2.map {
                it.toInt() * 3
            }
        }
    println(result3)
    println("*********************** Applicative 自定义ap")
    val result4 =NewListMonad.run {
        val kind: Kind<KtList.K, (String) -> Int> = pure {
            it.toInt() * 4 //自定义pure
        }
        c2.ap(kind)
    }
    println(result4)
}
