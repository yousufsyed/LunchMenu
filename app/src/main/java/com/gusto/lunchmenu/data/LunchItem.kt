package com.gusto.lunchmenu.data

sealed class LunchItem {
    data class Header(val weekNumber: Int) : LunchItem()
    data class Menu(val titleMessage: TitleMessage, val description: String) : LunchItem()
}
