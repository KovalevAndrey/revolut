package test.revolut.com.revoluttest.module.currencies.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.adapter.SimpleRecyclerAdapter
import com.avito.konveyor.blueprint.ViewHolderBuilder
import test.revolut.com.revoluttest.R

interface CurrenciesView {

    fun onDataSourceChanged()

    fun onDataRangeChanged(start: Int, end: Int)
}

class CurrenciesViewImpl(
        view: ViewGroup,
        private val viewHolderBuilder: ViewHolderBuilder<BaseViewHolder>,
        private val adapterPresenter: AdapterPresenter
) : CurrenciesView {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
    private lateinit var adapter : SimpleRecyclerAdapter

    init {
        recyclerView.layoutManager = layoutManager
    }

    override fun onDataSourceChanged() {
        if (recyclerView.adapter == null) {
            adapter = SimpleRecyclerAdapter(adapterPresenter, viewHolderBuilder)
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        } else {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDataRangeChanged(start: Int, end: Int) {
        adapter.notifyItemRangeChanged(start, end, null)
    }
}