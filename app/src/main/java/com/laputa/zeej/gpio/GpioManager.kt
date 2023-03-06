package com.laputa.zeej.gpio

class GpioManager {

    fun setGpios(gpio: Int, value: Int): Int {
//        try {
//            su("/sys/class/gpio")
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            return -111
//        }
//        try {
//            su("sys/devices")
//            setGpio(gpio, value)
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            return -222
//        }
        return setGpio(gpio, value)
    }

    private external fun setGpio(gpio: Int, value: Int): Int

    private fun su(path: String): Boolean {
        try {
            /* Missing read/write permission, trying to chmod the file */
            val su: Process = Runtime.getRuntime().exec("/system/xbin/su")
            val cmd = "chmod 777 $path \n exit \n"
            su.outputStream.write(cmd.toByteArray())
            if (su.waitFor() != 0) {
                throw SecurityException()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            throw SecurityException()
        }
    }

    companion object {
        init {
            System.loadLibrary("zeej")
        }
    }


}