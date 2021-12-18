package com.laputa.zeej.std_0010_kotlin.function

// TypeClass

interface XIterable<T> {
    fun filter(p: (T) -> Boolean): Iterable<T>
    fun remove(p: (T) -> Boolean): Iterable<T> = filter { x ->
        !p(x)
    }
}

// 一维函数
//interface XList<T>:XIterable<T>{
//    override fun filter(p: (T) -> Boolean): XList<T>
//    override fun remove(p: (T) -> Boolean): XList<T> = filter { x ->
//        !p(x)
//    }
//}
//
//interface XSet<T>:XIterable<T>{
//    override fun filter(p: (T) -> Boolean): XSet<T>
//    override fun remove(p: (T) -> Boolean): XSet<T> = filter { x ->
//        !p(x)
//    }
//}

// 高阶函数
// 值构造器 value constructor
// 传入一个值，构造一个新的值
val valueConstructor: (x: Int) -> Int = { x -> x }

// 类型构造器 type constructor
// 传入一个类型，构造一个新的类型
val typeConstructor: (x: Int) -> List<Int> = { x -> listOf(x)}

val typeClassConstructor: (x: Int) -> (x: Int) -> Int = { x -> { x * x } }

//interface Container<B>
//interface XXIterable<A,Container<B>>{
//    fun filter(p:(A)->Boolean):XXIterable<A,Container<B>>
//    fun remove(p:(A)->Boolean)::XXIterable<A,Container<B>> = fiter{x->!p(x)}
//}
//interface XXList<T>:XXIterable<T,XXList>
//interface XXSet>:XXIterable<T,XXSet>

