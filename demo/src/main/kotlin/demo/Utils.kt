package demo

import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.internal.schedulers.SingleScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

fun printWithThreadContext(message: String) = System.out.println("$message on thread: ${Thread.currentThread().name}")

fun initMainThread() {
    RxJavaPlugins.setInitSingleSchedulerHandler {
        val priority = Math.max(
            Thread.MIN_PRIORITY, Math.min(
                Thread.MAX_PRIORITY,
                Integer.getInteger("rx2.single-priority", Thread.NORM_PRIORITY)
            )
        )

        val factory = RxThreadFactory("mainThread", priority, true)
        SingleScheduler(factory)
    }
}

fun mainThread(): Scheduler {
    return Schedulers.single()
}

fun callbackSDK(callback: (String) -> Unit) {
    Thread({
        callback("Value from SDK")
    }, "Random SDK Thread").start()
}
