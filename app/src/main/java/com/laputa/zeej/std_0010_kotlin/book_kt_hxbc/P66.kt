package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

private interface Flyer {
    fun fly()
    fun kind() = "[Flayer] flying "
}

private interface Animal {
    val name: String
    fun eat()
    fun kind() = "[Animal] eating"
}

private class Birds(override val name: String) : Flyer, Animal {
    override fun fly() {
    }

    override fun eat() {
    }

    override fun kind(): String {
        return super<Animal>.kind() +
                super<Flyer>.kind()
    }
}

private class DelegateBird(val animal: Animal,val flyer: Flyer):Animal by animal,Flyer by flyer{
    override fun kind(): String {
        return animal.kind()+flyer.kind()
    }
}

fun main() {
    val birds = Birds("xx")
    println(birds.kind())

    val delegateBird = DelegateBird(object :Animal{
        override val name: String
            get() = "zzzz"

        override fun eat() {

        }
    },object :Flyer{
        override fun fly() {
        }
    })
    println(delegateBird.kind())
}

