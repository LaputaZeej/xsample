package com.laputa.zeej.tsc

import java.lang.RuntimeException

data class PrintException(val msg:String):RuntimeException(msg)