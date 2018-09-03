package test.revolut.com.revoluttest.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

open class TestSchedulersFactory(
        val computation: Scheduler = Schedulers.trampoline(),
        val io: Scheduler = Schedulers.trampoline(),
        val mainThread: Scheduler = Schedulers.trampoline(),
        val newThread: Scheduler = Schedulers.trampoline(),
        val test: Scheduler = Schedulers.trampoline(),
        val trampoline: Scheduler = Schedulers.trampoline(),
        val single: Scheduler = Schedulers.trampoline()
) : SchedulersFactory {

    override fun single() = single

    override fun computation() = computation

    override fun from(executor: Executor): Scheduler = Schedulers.from(executor)

    override fun io() = io

    override fun mainThread() = mainThread

    override fun newThread() = newThread

    override fun test() = test

    override fun trampoline() = trampoline
}