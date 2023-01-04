package com.laputa.zeej.std_0010_kotlin.delegate

interface SubjectX {
    fun doSomething()
}

private class DelegateX:SubjectX{
    override fun doSomething() {
       println("DelegateX::doSomething")
    }
}

private class RealSubjectX:SubjectX{
    private val delegate:SubjectX = DelegateX()
    override fun doSomething() {
        delegate.doSomething()
    }
}

private class RealSubjectByDelegate1 : SubjectX by DelegateX()

private class RealSubjectByDelegate2(private val delegate:SubjectX ) : SubjectX by delegate

fun main() {
    RealSubjectX().doSomething()
    RealSubjectByDelegate1().doSomething()
    RealSubjectByDelegate2(DelegateX()).doSomething()
}