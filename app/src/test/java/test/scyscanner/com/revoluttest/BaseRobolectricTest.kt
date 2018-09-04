package test.scyscanner.com.revoluttest

import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config
import test.revolut.com.revoluttest.BuildConfig

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(
    packageName = "test.revolut.com.revoluttest",
    constants = BuildConfig::class,
    sdk = [21]
)
@RunWith(TestRunner::class)
open class BaseRobolectricTest