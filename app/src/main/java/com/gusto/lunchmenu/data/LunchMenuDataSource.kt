package com.gusto.lunchmenu.data

import kotlinx.coroutines.delay
import javax.inject.Inject

interface LunchMenuDataSource {
    suspend fun getLunchMenu(): List<List<String>>
}

class DefaultLunchMenuDataSource @Inject constructor() : LunchMenuDataSource {

    override suspend fun getLunchMenu(): List<List<String>> {
        delay(3_000)
        return listOf(
            listOf("Chicken and waffles", "Tacos", "Curry", "Pizza", "Sushi"),
            listOf("Breakfast for lunch", "Hamburgers", "Spaghetti", "Salmon", "Sandwiches")
        )
    }
}

