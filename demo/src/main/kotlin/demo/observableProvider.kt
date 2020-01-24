package demo

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class ObservableProvider {

    init {
        initMainThread()
    }

    fun topDefinedThreading(): Observable<String> = Observable.create<String> { emitter ->
            printWithThreadContext("Subscription to the topDefinedThreading observable")
            emitter.onNext("A value")
            emitter.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(mainThread())

    fun callSiteDefinedThreading(): Observable<String> = Observable.create<String> { emitter ->
            printWithThreadContext("Subscription to the callSiteDefinedThreading observable")
            emitter.onNext("A value")
            emitter.onComplete()
        }

    fun emitterCalledInCallback(): Observable<String> = Observable.create { emitter ->
        printWithThreadContext("Subscription to the emitterCalledInCallback observable")
        callbackSDK {
            printWithThreadContext("Passing value from SDK to the emitterCalledInCallback observable")
            emitter.onNext(it)
            emitter.onComplete()
        }
    }
    
    fun emitterCalledInSubscribe(): Observable<String> = Observable.create { emitter ->
        printWithThreadContext("Subscription to the emitterCalledInSubscribe observable")
        val queue: BlockingQueue<String> = LinkedBlockingQueue()
        callbackSDK {
            queue.add(it)
        }
        val message = queue.take()
        printWithThreadContext("Passing value from SDK to the emitterCalledInSubscribe observable")
        emitter.onNext(message)
        emitter.onComplete()
    }
}
