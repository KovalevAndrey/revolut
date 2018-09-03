package test.revolut.com.revoluttest.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.Executor

interface SchedulersFactory {
    fun trampoline(): Scheduler
    fun newThread(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler
    fun test(): Scheduler
    fun from(executor: Executor): Scheduler
    fun mainThread(): Scheduler
    fun single(): Scheduler
}

class SchedulersFactoryImpl : SchedulersFactory {

    override fun single(): Scheduler = Schedulers.single()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()

    override fun test(): Scheduler = TestScheduler()

    override fun from(executor: Executor): Scheduler = Schedulers.from(executor)

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

}