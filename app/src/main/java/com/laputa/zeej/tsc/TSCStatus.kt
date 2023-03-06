package com.laputa.zeej.tsc

enum class TSCStatus(val text: String) {
    ERROR("-1"),
    S00("Ready"),
    S01("Head Open"),
    S03("Ribbon Jam"),
    S04("Ribbon Empty"),
    S05("No Paper"),
    S06("Paper Jam"),
    S07("Paper Empty"),
    S08("Cutting"),
    S09("Waiting to Press Print Key"),
    S10("Waiting to Take Label"),
    S11("Printing Batch"),
    S12("Pause"),
    ;
}