package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppDatabase
import com.example.model.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Teke Man Promotion", appName)
  }

  @Test
  fun `room database direct operations`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDatabase.getDatabase(context)
    val productDao = db.productDao()

    // Clear and insert
    val testProduct = Product(
        id = 1234,
        titleEn = "Room Direct Gift",
        titleAm = "የሩም ቀጥታ ስጦታ",
        descriptionEn = "Test Description",
        descriptionAm = "የሙከራ መግለጫ",
        price = 150.0,
        rating = 4.5f,
        categoryId = "wedding",
        discountPercentage = 5,
        specificationsEn = listOf("Spec A", "Spec B"),
        specificationsAm = listOf("ሙከራ ኤ", "ሙከራ ቢ")
    )

    productDao.insertProduct(testProduct)
    val products = productDao.getAllProducts().first()
    assertEquals(1, products.size)
    assertEquals("Room Direct Gift", products[0].titleEn)
  }
}
