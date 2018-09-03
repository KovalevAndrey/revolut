package test.revolut.com.revoluttest.module.currencies.item

import com.avito.konveyor.blueprint.Item
import com.avito.konveyor.blueprint.ItemBlueprint
import com.avito.konveyor.blueprint.ItemPresenter
import com.avito.konveyor.blueprint.ViewHolderBuilder
import test.revolut.com.revoluttest.R

class CurrencyItemBlueprint(override val presenter: ItemPresenter<CurrencyItemView, CurrencyItem>) :
        ItemBlueprint<CurrencyItemView, CurrencyItem> {

    override val viewHolderProvider = ViewHolderBuilder.ViewHolderProvider(
            layoutId = R.layout.currency_item,
            creator = { _, view -> CurrencyItemViewImpl(view) }
    )

    override fun isRelevantItem(item: Item) = item is CurrencyItem

}