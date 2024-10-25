package com.gusto.lunchmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gusto.lunchmenu.LunchMenuState.Error
import com.gusto.lunchmenu.LunchMenuState.Loading
import com.gusto.lunchmenu.LunchMenuState.Success
import com.gusto.lunchmenu.data.ErrorMessage
import com.gusto.lunchmenu.data.LunchItem
import com.gusto.lunchmenu.data.LunchMenuDataSource
import com.gusto.lunchmenu.data.TitleMessage
import com.gusto.lunchmenu.provider.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LunchMenuViewModel(
    private val dataSource: LunchMenuDataSource,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _lunchMenu = MutableStateFlow<LunchMenuState>(Loading)
    val lunchMenu = _lunchMenu.asStateFlow()

    private val _canRetry = MutableStateFlow(true)
    val canRetry = _canRetry.asStateFlow()

    fun getLunchMenu() {
        _canRetry.update { false }
        _lunchMenu.update { Loading }
        viewModelScope.launch(dispatcherProvider.io) {
            runCatching {
                dataSource.getLunchMenu()
            }.fold(
                onSuccess = { processResults(it) },
                onFailure = { processError(it) }
            )
        }
    }

    private fun processResults(data: List<List<String>>) {
        _lunchMenu.update {
            Success(data.toLunchItems())
        }
        _canRetry.update { true }
    }

    private fun processError(error: Throwable) {
        _lunchMenu.update { Error(ErrorMessage.from(error)) }
        _canRetry.update { true }
    }

    private fun List<List<String>>.toLunchItems(): List<LunchItem> {
        val lunchItems = mutableListOf<LunchItem>()
        this.forEachIndexed { index, list ->
            lunchItems.add(LunchItem.Header(weekNumber = index))
            list.forEachIndexed { weekIndex, item ->
                lunchItems.add(
                    LunchItem.Menu(
                        titleMessage = TitleMessage.from(weekIndex, item),
                        description = "Some random description for $item"
                    )
                )
            }
        }
        return lunchItems
    }
}

class LunchMenuViewModelFactory @Inject constructor(
    private val dataSource: LunchMenuDataSource,
    private val dispatcherProvider: DispatcherProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(LunchMenuViewModel::class.java)) {
            LunchMenuViewModel(dataSource, dispatcherProvider) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

sealed class LunchMenuState {
    data object Loading : LunchMenuState()
    data class Error(val error: ErrorMessage) : LunchMenuState()
    data class Success(val items: List<LunchItem>) : LunchMenuState()
}