[msm/arch/arm64/kernel/head.S]
    340 	str	x22, [x4]			// Save processor ID
    341 	str	x21, [x5]			// Save FDT pointer
    342 	str	x24, [x6]			// Save PHYS_OFFSET
    343 	mov	x29, #0
    344 	b	start_kernel        //跳转start_kernel函数
[msm/init/main.c]
    start_kernel
    rest_init                       // 剩余初始化
        rcu_scheduler_starting
        [msm/kernel/fork.c]
            kernel_thread           // 内核中重要的进程，负责内核线程的调度和管理，内核线程基本都是以它为父进程的
                do_fork
    kernel_init                     // 重要
        kernel_init_freeable
msm/kernel/rcutree.c

msm/mm/mempolicy.c
msm/kernel/kthread.c
msm/include/linux/kthread.h
msm/include/linux/rcupdate.h
msm/kernel/rcupdate.c
msm/kernel/pid.c
msm/include/linux/sched.h
msm/kernel/sched/core.c
msm/kernel/cpu/idle.c
msm/drivers/base/init.c