package com.gusto.lunchmenu

import com.gusto.lunchmenu.data.LunchMenuDataSource
import com.gusto.lunchmenu.provider.DispatcherProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LunchMenuViewModelTest {

    private val dataSource = mockk<LunchMenuDataSource>()
    private val dispatcherProvider = mockk<DispatcherProvider>() {
        every { io } returns UnconfinedTestDispatcher()
    }

    private val viewModel = LunchMenuViewModel(dataSource, dispatcherProvider)

    @Test
    fun testGetLunchMenuSuccess() = runTest {
        coEvery { (dataSource.getLunchMenu()) } returns listOf(listOf("Item 1", "Item 2"))

        viewModel.getLunchMenu()
        assert(viewModel.lunchMenu.value is LunchMenuState.Success)
        coVerify(exactly = 1) {  dataSource.getLunchMenu() }
    }

    @Test
    fun testGetLunchMenuFailure() {
        coEvery { (dataSource.getLunchMenu()) } throws RuntimeException("This should fail")

        viewModel.getLunchMenu()
        coVerify { dataSource.getLunchMenu() }
        assert(viewModel.lunchMenu.value is LunchMenuState.Error)
    }

}