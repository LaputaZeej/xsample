// IUserInterface.aidl
package com.laputa.zeej.std_0006_android.binder.case03;

// Declare any non-default types here with import statements

interface IUserInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

           int getStudentGrade(String name);
}