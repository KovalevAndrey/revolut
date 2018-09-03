package test.scyscanner.com.scyscannertest

import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.lang.RuntimeException

class PojoFactory(private val factory: PodamFactory = PodamFactoryImpl()) {

    fun <T> create(type: Class<T>): T {
        return factory.manufacturePojoWithFullData(type)
                ?: throw RuntimeException("Podam failed to instantiate ${type.simpleName}")
    }

    inline fun <reified T : Any> create(): T {
        return create(T::class.java)
    }

}