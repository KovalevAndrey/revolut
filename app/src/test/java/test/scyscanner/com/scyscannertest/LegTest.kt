package test.scyscanner.com.scyscannertest

import com.google.gson.Gson
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import test.scyscanner.com.scyscannertest.application.api.model.Leg

@Suppress("IllegalIdentifier")
class ShopRegularTest : BaseRobolectricTest() {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = test.revolut.com.revoluttest.application.api.GsonFactory().createGson()
    }

    @Test
    fun `fromJson - parses leg `() {
        val leg = gson.fromJson(json, Leg::class.java)

        assertThat(leg.id, Is("123421"))
        assertThat(leg.ogiginStation, Is(234324))
        assertThat(leg.destinationStation, Is(234325))
        assertThat(leg.departure, Is("2018-05-06T17:10:00"))
        assertThat(leg.arrival, Is("2018-05-06T18:10:00"))
        assertThat(leg.duration, Is(75))
    }
}

@Language("JSON")
private const val json = """
    {
        "Id": "123421",
        "OriginStation" : 234324,
        "DestinationStation" : 234325,
        "Departure" : "2018-05-06T17:10:00",
        "Arrival" : "2018-05-06T18:10:00",
        "Duration" : 75
    }
"""
