package com.laputa.zeej.std_0010_kotlin.function

import com.laputa.zeej.std_0010_kotlin.function.KtListFunctor.map

// kt 模拟 Typeclass

// 表示类型构造器F应用参数A产生的类型
// F:类型构造器
// A:类型参数
interface Kind<out F, out A>
interface Functor<F> {
    fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B>
}

// KtList
sealed class KtList<out A> : Kind<KtList.K, A> {
    object K // 这个就代表类型即F[_]
}

object Nil : KtList<Nothing>()
data class Cons<A>(val head: A, val tail: KtList<A>) : KtList<A>()

@Suppress("NOTHING_TO_INLINE")
inline fun <A> Kind<KtList.K, A>.unwrap(): KtList<A> = this as KtList<A>

// 实现函子
object KtListFunctor : Functor<KtList.K> {
    override fun <A, B> Kind<KtList.K, A>.map(f: (A) -> B): Kind<KtList.K, B> {
        return when (this) {
            is Cons<A> -> {
                Cons(f(this.head), this.tail.map(f).unwrap())
            }
//            is Nil -> Nil // 为什么这样不行
            else -> Nil
        }
    }
}

 fun <A, B> Kind<KtList.K, A>.map2(f: (A) -> B): Kind<KtList.K, B> {
    return when (this) {
        is Cons<A> -> {
            Cons(f(this.head), this.tail.map2(f).unwrap())
        }
//            is Nil -> Nil // 为什么这样不行
        else -> Nil
    }
}

/**
 * 实现Eq Typeclass
 */
// 只要为一个类型定义一个Eq的Typeclass实例，就可以在实例的run函数中对该类型的对象或值进行判等操作。
interface Eq<F> {
    fun F.eq(that: F): Boolean
}

// Int实现
object IntEq : Eq<Int> {
    override fun Int.eq(that: Int): Boolean {
        return this == that
    }
}

object StringEq : Eq<String> {
    override fun String.eq(that: String): Boolean {
        return this == that
    }
}

// List实现（高阶的实现）
abstract class ListEq<A>(private val a: Eq<A>) : Eq<Kind<KtList.K, A>> {
    override fun Kind<KtList.K, A>.eq(that: Kind<KtList.K, A>): Boolean {
        val curr = this
        return if (curr is Cons && that is Cons) {
            val headEq = a.run {
                curr.head.eq(that.head)
            }
            return headEq && curr.tail.eq(that.tail)
        } else curr is Nil && that is Nil
    }
}

object IntListEq : ListEq<Int>(IntEq) //
object StringListEq : ListEq<String>(StringEq) //

/**
 * Show
 */
interface Show<F> {
    fun F.show(): String
}

data class Book(val name: String, val author: String)

object BookShow : Show<Book> {
    override fun Book.show(): String {
        return "BookShow->${this}"
    }
}

// 实现ListShow 需要讲元素的结果都拼装起来，需要对KtList封装一个类似fold的操作
interface Foldable<F> {

    fun <A, B> Kind<F, A>.fold(/*初始值*/init: B): /*返回一个函数，结果是B*/((B, A) -> B) -> B
}

object ListFoldable : Foldable<KtList.K> {
    override fun <A, B> Kind<KtList.K, A>.fold(init: B): ((B, A) -> B) -> B {
        return {
            fun fold0(lt: KtList<A>, b: B): B {
                // 怕段KtList
                return when (lt) {
                    // 当Cons 继续展开：把tail放入lt，执行fold后的结果放入b，开始新的一轮展开
                    is Cons -> fold0(lt.tail, it(b, lt.head))
                    // 直到是Nil停止
                    else -> b
                }
            }
            // 开启展开 B初始值init
            fold0(this.unwrap(), init)
        }
    }
}

// 继续ListShow 和 BookShow一样实现Show
abstract class ListShow<A>(private val a: Show<A>) : Show<Kind<KtList.K, A>> {
    override fun Kind<KtList.K, A>.show(): String {
        val fa = this
        return "[" + (ListFoldable.run {
            // 初始值为listOf() B 为List<String> A为任意比如：Book 代入fold
            fa.fold(listOf<String>()).invoke { r, s ->
                // r 是List<String>
                // s 是a
                // a.run{}结果为B,即String
                r + a.run {
                    s.show()
                } // 这里注意+号是List<String> + String的一个操作符重载写法 正常写法为r.add(a.run{})
                // 所以整个结果为List<String>
            }
        }).joinToString() + "]"
    }
}

private fun test() {
    val list = listOf("1", "2") + "3"
    println(list)
}

// 然后 BookListShow
object BookListShow : ListShow<Book>(BookShow)

data class Student(val name: String)
object StudentShow : Show<Student> {
    override fun Student.show(): String {
        return this.toString()
    }
}

object StudentListShow : ListShow<Student>(StudentShow)

fun testStudent() {
    val a = Student("a")
    val b = Student("b")
    println(StudentShow.run {
        a.show()
    })
    println(StudentListShow.run {
        Cons(a, Cons(b, Nil)).show()
    })
}

fun main() {
    println("********************************KtListFunctor")
    KtListFunctor.run {
        val old = Cons(listOf(1, 2), Nil)
        println(old)
        val result = old.map {
            "map 后的东西 $it"
        }
        println(result)
    }
//    val old = Cons(listOf(1, 2), Nil)
//    println(old)
//    val result = old.map2 {
//        "map2 后的东西 $it"
//    }
//    println(result)
    println("********************************IntEq")
    IntEq.run {
        val a = 1
        println(a.eq(2))
        println(a.eq(1))
    }
    println("********************************IntListEq")
    val cons = Cons(1, Cons(2, Nil))
    IntListEq.run {
        println(cons.eq(Cons(1, Cons(2, Nil))))
        println(cons.eq(Cons(1, Nil)))
    }
    val cons2 = Cons("x", Cons("xy", Nil))
    StringListEq.run {
        println(cons2.eq(Cons("x", Cons("xy", Nil))))
        println(cons2.eq(Cons("x", Nil)))
    }
    println("********************************BookShow")
    BookShow.run {
        val book = Book("java", "x")
        println(book.show())
    }
    println("********************************BookListShow")
    BookListShow.run {
        val cons = Cons(Book("java", "x"), Cons(Book("kotlin", "k"), Nil))
        println(cons.show())
    }
    println("********************************BookListShow")
    testStudent()
}