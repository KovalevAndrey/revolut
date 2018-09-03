package test.revolut.com.revoluttest.module.currencies.item
import com.avito.konveyor.blueprint.Item

interface Item : Item {

    val stringId: String

    override val id: Long
        get() = stringId.hashCode().toLong()
}