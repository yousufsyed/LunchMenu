package com.gusto.lunchmenu

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gusto.lunchmenu.data.LunchMenuDataSource
import com.gusto.lunchmenu.provider.DispatcherProvider
import com.gusto.lunchmenu.ui.MainActivity
import com.gusto.lunchmenu.ui.MainActivityPrompt
import com.gusto.lunchmenu.ui.theme.MyApplicationTheme
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LunchMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dataSource = mockk<LunchMenuDataSource>()
    private val dispatcherProvider = mockk<DispatcherProvider>() {
        every { io } returns UnconfinedTestDispatcher()
    }

    private val viewModel = LunchMenuViewModel(dataSource, dispatcherProvider)


    @Test
    fun lunchMenuResultsTest() {
        coEvery { (dataSource.getLunchMenu()) } returns
                listOf(listOf("Item 1", "Item 2"))

        viewModel.getLunchMenu()

        // Start the app
        composeTestRule.setContent {
            MyApplicationTheme {
                MainActivityPrompt(
                    modifier = Modifier,
                    viewModel
                )
            }
        }

        Thread.sleep(5000)
        composeTestRule.onNodeWithText("Week 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Monday - Item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tuesday - Item 2").assertIsDisplayed()
    }

    @Test
    fun lunchMenuErrorTest() {
        coEvery { (dataSource.getLunchMenu()) } throws RuntimeException("This should fail")

        // Start the app
        composeTestRule.setContent {
            MyApplicationTheme {
                MainActivityPrompt(
                    modifier = Modifier,
                    viewModel
                )
            }
        }
        Thread.sleep(5000)

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
    }

}