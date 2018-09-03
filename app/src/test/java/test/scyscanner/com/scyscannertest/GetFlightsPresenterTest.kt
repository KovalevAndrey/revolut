package test.scyscanner.com.scyscannertest

import android.os.Bundle
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.data_source.DataSource
import com.avito.konveyor.data_source.ListDataSource
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import test.scyscanner.com.scyscannertest.module.flights.*
import test.scyscanner.com.scyscannertest.module.flights.item.ItineraryItem
import test.scyscanner.com.scyscannertest.module.flights.item.ItineraryItemConverter
import test.scyscanner.com.scyscannertest.module.flights.ui.GetFlightsView
import test.scyscanner.com.scyscannertest.utils.TestSchedulersFactory

@Suppress("IllegalIdentifier")
class GetFlightsPresenterTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var adapterPresenter: AdapterPresenter
    @Mock
    private lateinit var interactor: GetFlightsInteractor
    @Mock
    private lateinit var view: GetFlightsView
    @Mock
    private lateinit var journeyDatesProvider: JourneyDatesProvider
    @Mock
    private lateinit var itemConverter: ItineraryItemConverter

    private lateinit var presenter: GetFlightsPresenter

    @Before
    fun setUp() {
        createPresenter()
    }

    @Test
    fun `attach view - loads data from interactor - state is null`() {
        createPresenter(state = null)
        val item = PojoFactory().create<ItineraryItem>()
        mockItemConverter(listOf(item))
        val outboundDate = "2018-05-07"
        val inboundDate = "2018-05-08"
        mockJourneyDates(outboundDate, inboundDate)
        mockInteractor()
        presenter.attachView(view)

        verify(interactor).getFlights(outboundDate, inboundDate)
    }


    @Test
    fun `attach view - gets data from cache - state is not null`() {
        createPresenter(state = null)
        val item = PojoFactory().create<ItineraryItem>()
        mockItemConverter(listOf(item))
        val outboundDate = "2018-05-07"
        val inboundDate = "2018-05-08"
        mockJourneyDates(outboundDate, inboundDate)
        mockInteractor()
        presenter.attachView(view)

        recreatePresenter()
        clearInvocations(interactor)

        verify(interactor, never()).getFlights(any(), any())

        verify(view).onDataSourceChanged()
        val dataSource = captureAdapterDataSource()
        assertThat(dataSource, hasSize(1))
        assertThat(dataSource[0], Is(item))

    }

    private fun captureAdapterDataSource(): List<ItineraryItem> {
        val captor = argumentCaptor<DataSource<ItineraryItem>>()
        verify(adapterPresenter).onDataSourceChanged(captor.capture())
        return (captor.lastValue as ListDataSource<ItineraryItem>).toList()
    }


    private fun mockItemConverter(items: List<ItineraryItem>) {
        whenever(itemConverter.convertItems(any())).thenReturn(items)
    }

    private fun mockInteractor(result: FlightsResultConverted = PojoFactory().create()) {
        whenever(interactor.getFlights(any(), any())).thenReturn(Observable.just(result))
    }

    private fun mockJourneyDates(outboundDate: String,
                                 inboundDate: String) {
        whenever(journeyDatesProvider.provideOutboundDate()).thenReturn(outboundDate)
        whenever(journeyDatesProvider.provideInboundDate()).thenReturn(inboundDate)
    }

    private fun createPresenter(state: Bundle? = null) {
        presenter = GetFlightsPresenterImpl(
                adapterPresenter = adapterPresenter,
                schedulersFactory = TestSchedulersFactory(),
                interactor = interactor,
                journeyDatesProvider = journeyDatesProvider,
                itemConverter = itemConverter,
                savedState = state
        )
    }

    private fun recreatePresenter() {
        createPresenter(presenter.onSaveState())
    }

}