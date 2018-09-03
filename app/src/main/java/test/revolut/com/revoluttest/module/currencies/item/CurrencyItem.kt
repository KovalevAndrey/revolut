package test.revolut.com.revoluttest.module.currencies.item

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class CurrencyItem(override val stringId: String,
                   var value: BigDecimal? = null,
                   var isMainCurrency: Boolean = false) : Item, Parcelable
