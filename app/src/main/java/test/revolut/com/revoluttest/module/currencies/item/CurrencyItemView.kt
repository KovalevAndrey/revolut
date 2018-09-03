package test.revolut.com.revoluttest.module.currencies.item

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.blueprint.ItemView
import test.revolut.com.revoluttest.R
import java.math.BigDecimal

interface CurrencyItemView : ItemView {

    fun setCurrencyName(currency: String)

    fun setCurrencyValue(value: String)

    fun setOnClickListener(listener: () -> Unit)

    fun setEditTextEditable(isEditable: Boolean)

    fun setOnValueChangesListener(listener: (s: BigDecimal) -> Unit)

    fun removesetOnValueChangesListener()

}

class CurrencyItemViewImpl(view: View) : BaseViewHolder(view), CurrencyItemView {
    private val currencyName = view.findViewById<TextView>(R.id.currency_name)
    private val currencyValue = view.findViewById<EditText>(R.id.currency_value)
    private var watcher: TextWatcher? = null

    override fun setCurrencyName(currency: String) {
        currencyName.text = currency
    }

    override fun setCurrencyValue(value: String) {
        currencyValue.setText(value)
    }

    override fun setOnClickListener(listener: () -> Unit) {
        currencyValue.setOnClickListener { listener() }
    }

    override fun setEditTextEditable(isEditable: Boolean) {
        currencyValue.isFocusable = isEditable
        currencyValue.isFocusableInTouchMode = isEditable
    }

    override fun setOnValueChangesListener(listener: (s: BigDecimal) -> Unit) {
        watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    listener(s.toString().toBigDecimal())
                } else {
                    listener(BigDecimal(0))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        }
        currencyValue.addTextChangedListener(watcher)
    }

    override fun removesetOnValueChangesListener() {
        watcher?.let {
            currencyValue.removeTextChangedListener(it)
        }
    }
}
