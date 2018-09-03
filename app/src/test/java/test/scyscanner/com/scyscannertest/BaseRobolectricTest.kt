package test.scyscanner.com.scyscannertest

import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(
    packageName = "com.scyscanner.test",
    constants = BuildConfig::class,
    sdk = [21]
)
@RunWith(TestRunner::class)
open class BaseRobolectricTest