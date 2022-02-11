package com.laputa.zeej.std_0010_kotlin;

public class JavaCase {
    public static void main(String[] args) {
        Kitty kitty = Black.INSTANCE;
        kitty.play();

        Black.INSTANCE.play();
        Black.run();
    }
}
