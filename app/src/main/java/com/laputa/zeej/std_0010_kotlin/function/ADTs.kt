package com.laputa.zeej.std_0010_kotlin.function

// 代数类型

// 积类型 2*1 Boolean：2 Unit：1
class BooleanProductUnit(a: Boolean, b: Unit)

val a = BooleanProductUnit(true, Unit)
val b = BooleanProductUnit(false, Unit)


// 和类型
enum class Day {
    SUN, MON, TUE, WED, THU, FRI, SAT
}

sealed class SealedDay
object SUN : SealedDay()
object MON : SealedDay()
object TUE : SealedDay()
object WED : SealedDay()
object THU : SealedDay()
object FRI : SealedDay()
object SAT : SealedDay()

// 模式匹配 + smart-cast
// 嵌套表达式
sealed class Expr {
    abstract fun isZero(): Boolean
    abstract fun isAddZero(): Boolean
    abstract fun left(): Expr
    abstract fun right(): Expr


}

data class Num(val value: Int) : Expr() {
    override fun isZero(): Boolean {
        return value == 0
    }

    override fun isAddZero(): Boolean {
        return false
    }

    override fun left(): Expr {
        throw Throwable("no left")
    }

    override fun right(): Expr {
        throw Throwable("no right")
    }
}

data class Opt(val name: String, val left: Num, val right: Num) : Expr() {
    override fun isZero(): Boolean {
        return false
    }

    override fun isAddZero(): Boolean {
        return name == "+" && (left.isZero() || right.isZero())
    }

    override fun left(): Expr {
        return this.left
    }

    override fun right(): Expr {
        return this.right
    }
}

// use if-else 换成java需要强转
fun simple(expr: Expr): Expr {
    return if (expr is Num) {
        expr
    } else if (expr is Opt && expr.name == "+" && expr.left is Num && expr.left.value == 0) {
        expr.right
    } else if (expr is Opt && expr.name == "+" && expr.right is Num && expr.right.value == 0) {
        expr.left
    } else expr
}

fun simpleWhen(expr: Expr) {
    when {
        expr is Opt && expr.name == "+" && expr.left is Num && expr.left.value == 0 -> expr.right
        expr is Opt && expr.name == "+" && expr.right is Num && expr.right.value == 0 -> expr.left
        else -> expr
    }
}

// scala
//sealed trait Expr
//case class Num(value:Int) extends Expr
//case class Opt(name:String,left:Expr,right:Expr) extedns Expr
//def simple(expr:Expr):Expr = expr match{
//    // 0+x
//    case Opt("+",Num(0),x)=>x
//    // x+0
//    case Opt("+),x,Num(0)=>x
//            case _ =>expr
//}

// 正向构造表达式
// val expr = Opt("+",Num(0),x)
// 反向构造表达式
// val ("+",Num(0),x) = expr

// 尝试和scala一样的写法
/*fun trySimplePatternMatching(expr: Expr) = when (expr) {
    Opt("+", Num(0), expr.right) -> expr.right
    Opt("+",expr.left, Num(0)) -> expr.left
    else -> expr
}*/

// 不行 kotlin

fun trySimplePatternMatching(expr: Expr) = when (expr) {
    is Num -> expr
    is Opt -> {
        when (expr) {
            Opt("+", Num(0), expr.right) -> expr.right
            Opt("+", expr.left, Num(0)) -> expr.left
            else -> expr
        }
    }
}

// 多层嵌套
// val ex = Opt("+", Num(0), Opt("+", Num(1), Num(2)))

//fun trySimplePatternMatchingColl(expr: Expr) = when (expr) {
//    is Num -> expr
//    is Opt -> {
//        when (expr) {
//            Opt("+", Num(0), expr.right) -> trySimplePatternMatchingColl(expr)//循环递归问题
//            Opt("+", expr.left, Num(0)) -> expr.left
//            else -> expr
//        }
//    }
//}

// 或者
/*
fun trySimplePatternMatchingColl(expr: Expr) = when (expr) {
    is Num -> expr
    is Opt -> {
        when (expr) {
            Opt("+", Num(0), expr.right) -> when (val r = expr.right) {
                Opt("+", expr.left, Num(0)) -> r.left
                else -> r
            }
            Opt("+", expr.left, Num(0)) -> expr.left
            else -> expr
        }
    }
}*/

// val ex = Opt("+", Num(0), Opt("+", Num(1), Num(2)))
/*fun trySimplePatternMatchingColl(expr: Expr) = when {
    expr.isAddZero() && expr.right.isAddZero() && expr.right.right.isZero() -> expr.right.left
    else -> expr
}*/

// val ex = Opt("+", Num(0), Opt("+", Num(1), Num(2)))
fun trySimplePatternMatchingColl(expr: Expr) = when {
    expr.isAddZero() && expr.right().isAddZero() && expr.right().right().isZero()
    -> expr.right().left()
    else -> expr
}

// 使用访问者模式Visitor

sealed class NewExpr {
    abstract fun isZero(v: Visitor): Boolean
    abstract fun isAddZero(v: Visitor): Boolean
    abstract fun simple(v: Visitor): NewExpr
}

data class NewNum(val value: Int) : NewExpr() {
    override fun isZero(v: Visitor): Boolean = v.visitIsZero(this)

    override fun isAddZero(v: Visitor): Boolean = v.visitIsAddZero(this)

    override fun simple(v: Visitor): NewExpr = v.visitSimple(this)

    override fun toString(): String {
        return value.toString()
    }
}

data class NewOpt(val name: String, val left: NewExpr, val right: NewExpr) : NewExpr() {
    override fun isZero(v: Visitor): Boolean = v.visitIsZero(this)

    override fun isAddZero(v: Visitor): Boolean = v.visitIsAddZero(this)

    override fun simple(v: Visitor): NewExpr = this
    override fun toString(): String {
        return "($left$name$right)"
    }

}

// 定义多个visit方法，这些方法的名称相同，但是参数类型不同。
// 参数类型为目标类的子类，在目标类中，需要在每个子类中定义一个accept方法，以注入Visitor对象，
// 然后访问者就可以对目标类不同的子类进行一些不同的操作了。
class Visitor {
    fun visitIsZero(expr: NewNum): Boolean {
        return expr.value == 0
    }

    fun visitIsZero(expr: NewOpt): Boolean {
        return false
    }

    fun visitIsAddZero(expr: NewNum): Boolean {
        return false
    }

    fun visitIsAddZero(expr: NewOpt): Boolean {
        return when (expr) {
            NewOpt("+", NewNum(0), expr.right) -> true
            NewOpt("+", expr.left, NewNum(0)) -> true
            else -> false
        }
    }

    fun visitSimple(expr: NewNum): NewExpr {
        return expr
    }

    // val ex = Opt("+", Num(0), Opt("+", Num(1), Num(2)))
    // 有点迷糊
    fun visitSimple(expr: NewOpt, v: Visitor): NewExpr {
        return when {
            (expr.right is NewNum && v.visitIsAddZero(expr) && v.visitIsAddZero(expr.right)) &&
                    (expr.right is NewOpt && expr.right.left is NewNum) &&
                    v.visitIsZero(expr.right.left)
            -> expr.right.left
            else -> expr
        }
    }
}

fun main() {
    val visitor = Visitor()
    val opt = NewOpt("+", NewNum(1), NewNum(2))
    val opt2 = NewOpt("+", NewNum(0), opt)
    val visitSimple = visitor.visitSimple(opt2, visitor)
    println(visitSimple)
}
