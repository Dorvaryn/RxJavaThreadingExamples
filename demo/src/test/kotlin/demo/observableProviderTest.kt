package demo

import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class ObservableProviderTest {

    private val provider = ObservableProvider()

    @Before
    fun setUp() {
        System.out.println("Start of Test:")
    }

    @Test
    fun topDefinedThreading() {
        provider.topDefinedThreading()
            .map { value ->
                printWithThreadContext("Map operation executed")
            }
            .flatMap {
                printWithThreadContext("Flatmap operation executed")
                provider.topDefinedThreading()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe {
                printWithThreadContext("Got result from topDefinedThreading observable")
            }
    }

    @Test
    fun callSiteDefinedThreading() {
        provider.callSiteDefinedThreading()
            .map { value ->
                printWithThreadContext("Map operation executed")
            }
            .flatMap {
                printWithThreadContext("Flatmap operation executed")
                provider.callSiteDefinedThreading()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe {
                printWithThreadContext("Got result from callSiteDefinedThreading observable")
            }
    }

    @Test
    fun emitterCalledInCallback() {
        provider.emitterCalledInCallback()
            .map { value ->
                printWithThreadContext("Map operation executed")
            }
            .flatMap {
                printWithThreadContext("Flatmap operation executed")
                provider.emitterCalledInCallback()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe {
                printWithThreadContext("Got result from emitterCalledInCallback observable")
            }
    }

    @Test
    fun emitterCalledInSubscribe() {
        provider.emitterCalledInSubscribe()
            .map { value ->
                printWithThreadContext("Map operation executed")
            }
            .flatMap {
                printWithThreadContext("Flatmap operation executed")
                provider.emitterCalledInSubscribe()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe {
                printWithThreadContext("Got result from emitterCalledInSubscribe observable")
            }
    }

    @After
    fun tearDown() {
        Thread.sleep(1000)
        System.out.println("End of Test\n")
    }
}
