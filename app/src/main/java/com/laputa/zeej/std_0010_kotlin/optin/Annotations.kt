package com.laputa.zeej.std_0010_kotlin.optin

// This class can only be used with the compiler argument '-Xopt-in=kotlin.RequiresOptIn'
@RequiresOptIn("this api is not stable.",RequiresOptIn.Level.WARNING)
annotation class UnstableApi

@RequiresOptIn("this api is removed",RequiresOptIn.Level.ERROR)
annotation class UnSupportApi