package com.laputa.zeej.std_0010_kotlin.partialfunc

import java.lang.IllegalStateException

interface ApplyHandler {
    val successor: ApplyHandler?
    fun handleEvent(event: ApplyEvent)
}

class GroupLeader(override val successor: ApplyHandler?) : ApplyHandler {
    override fun handleEvent(event: ApplyEvent) {
        when {
            event.money <= 100 -> println("GroupLeader 同意 $event")

            else -> {
                when (successor) {
                    is ApplyHandler -> successor.handleEvent(event)
                    else -> throw IllegalStateException("GroupLeader 拒绝 $event")
                }
            }
        }
    }
}

class PresidentLeader(override val successor: ApplyHandler?) : ApplyHandler {
    override fun handleEvent(event: ApplyEvent) {
        when {
            event.money <= 500 -> println("PresidentLeader 同意 $event")
            else -> {
                when (successor) {
                    is ApplyHandler -> successor.handleEvent(event)
                    else -> throw IllegalStateException("PresidentLeader 拒绝 $event")
                }
            }
        }
    }
}

class Collage(override val successor: ApplyHandler? = null) : ApplyHandler {
    override fun handleEvent(event: ApplyEvent) {
        when {
            event.money <= 1000 -> println("Collage 同意 $event")
            else -> throw IllegalStateException("Collage 拒绝 $event")
        }
    }
}

fun main() {
    val collage = Collage()
    val presidentLeader = PresidentLeader(collage)
    val groupLeader = GroupLeader(presidentLeader)
    groupLeader.handleEvent(ApplyEvent(10,"买铅笔"))
    groupLeader.handleEvent(ApplyEvent(200,"团建"))
    groupLeader.handleEvent(ApplyEvent(600,"组织比赛"))
    groupLeader.handleEvent(ApplyEvent(1200,"旅游"))
}