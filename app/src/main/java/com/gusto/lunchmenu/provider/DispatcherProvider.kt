package com.gusto.lunchmenu.provider

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Singleton
data class DispatcherProvider(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher
)