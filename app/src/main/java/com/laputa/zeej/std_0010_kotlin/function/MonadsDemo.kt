package com.laputa.zeej.std_0010_kotlin.function

// 使用高阶函数实现 IO操作

// 这个高阶类型只有一个实现，就是StdIO<A>. / 自己的解释
@Suppress("NOTHING_TO_INLINE")
inline fun <A> Kind<StdIO.K, A>.unwrap(): StdIO<A> = this as StdIO<A>

// 输入输出的类型
sealed class StdIO<A> : Kind<StdIO.K, A> {
    object K
    companion object {
        fun read(): StdIO<String> {
            return ReadLine
        }

        fun write(line: String): WriteLine {
            return WriteLine(line)
        }

        fun <A> pure(a: A): StdIO<A> {
            return Pure(a)
        }
    }
}

// 读操作
object ReadLine : StdIO<String>()

// 写操作
data class WriteLine(val line: String) : StdIO<Unit>()

// 数据类
data class Pure<A>(val a: A) : StdIO<A>()


// flatmap
data class FlatMap<A, B>(val fa: StdIO<A>, val f: (A) -> StdIO<B>) : StdIO<B>()

// 实现一个Monad
object StdIOMonad : Monad<StdIO.K> {
    override fun <A> pure(a: A): Kind<StdIO.K, A> {
        return Pure(a)
    }

    // 晕了晕了
    override fun <A, B> Kind<StdIO.K, A>.flatMap(f: (A) -> Kind<StdIO.K, B>): Kind<StdIO.K, B> {
        return FlatMap(this.unwrap()) {
            f(it).unwrap()
        }
    }
}

fun <A> perform(stdIO: StdIO<A>): A {
    return StdIOMonad.run {

        fun <C, D> runFlatMap(fm: FlatMap<C, D>) {
            perform(fm.f(perform(fm.fa))) // ????
        }
        when (stdIO) {
            ReadLine -> readA() as A
//            ReadLine -> readLine() as A
            is Pure -> stdIO.a
            is FlatMap<*, A> -> runFlatMap(stdIO) as A
            is WriteLine -> println(stdIO.line) as A
        }
    }
}

var aa = "11"
fun readA(): String {
    println("read a ing ...")
    Thread.sleep(3000)
    aa += 4
    println("   read :$aa")
    return aa
}

fun main() {
    val io = StdIOMonad.run {
        StdIO.read().flatMap { a ->
            StdIO.read().flatMap { b ->
                StdIO.write((a.toInt() + b.toInt()).toString())
            }
        }
    }
    perform(io.unwrap())
}



