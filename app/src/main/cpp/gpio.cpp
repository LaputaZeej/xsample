#include <jni.h>
#include <string>

#include <cstdio>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int setGoio(int open);

extern "C"
JNIEXPORT jint
Java_com_laputa_zeej_gpio_GpioManager_setGpio(JNIEnv *env, jobject thiz, jint gpio, jint value) {
    return setGoio(value);
}

int setGoio(int value) {
    int ret, fd;
    fd = open("/sys/class/gpio/export", O_WRONLY);
    if (fd < 0) {
        printf("open export file failed");
        return -1;
    }
    ret = write(fd, "220", strlen("220"));
    if (ret < 0) {
        printf("write export failed");
        return -2;
    }
    close(fd);
    fd = open("/sys/class/gpio/gpio220/direction", O_RDWR);
    if (fd < 0) {
        printf("open direction file failed");
        return -3;
    }
    ret = write(fd, "out", strlen("out"));
    if (ret < 0) {
        printf("write direction failed");
        return -4;
    }
    close(fd);
    fd = open("/sys/class/gpio/gpio220/value", O_RDWR);
    if (fd < 0) {
        printf("open value file failed");
        return -5;
    }
    if (value) {
        ret = write(fd, "1", strlen("1"));
    }else{
        ret = write(fd, "1", strlen("0"));
    }

    if (ret < 0) {
        printf("write value failed");
        return -6;
    }
    return ret;
}