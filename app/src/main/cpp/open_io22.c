#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>


int main(void)
{
	int ret, fd;
	fd = open("/sys/class/gpio/export", O_WRONLY);
	if (fd < 0) {
		printf("open export file failed");
		return -1;
	}
        ret = write(fd, "220", strlen("220"));
        if (ret < 0) {
                printf("write export failed");
                return -1;
        }
      	close(fd);
        fd = open("/sys/class/gpio/gpio220/direction", O_RDWR);
        if (fd < 0) {
                printf("open direction file failed");
                return -1;
        }
        ret = write(fd, "out", strlen("out"));
        if (ret < 0) {
                printf("write direction failed");
                return -1;
        }
        close(fd);
        fd = open("/sys/class/gpio/gpio220/value", O_RDWR);
        if (fd < 0) {
                printf("open value file failed");
                return -1;
        }
        ret = write(fd, "1", strlen("1"));
        if (ret < 0) {
                printf("write value failed");
                return -1;
        }
}

