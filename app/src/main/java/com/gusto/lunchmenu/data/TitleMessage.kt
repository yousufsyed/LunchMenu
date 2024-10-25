package com.gusto.lunchmenu.data

import com.gusto.lunchmenu.R
import java.io.IOException

data class ErrorMessage(val resId: Int) {
    companion object {
        fun from(error: Throwable) = ErrorMessage(
            when (error) {
                is IOException -> R.string.lunch_menu_no_internet_error
                else -> R.string.lunch_menu_generic_error
            }
        )
    }
}

data class TitleMessage(val resId: Int, val name: String) {

    companion object {

        private val map: HashMap<Int, Int> = hashMapOf(
            0 to R.string.lunch_weekday_monday,
            1 to R.string.lunch_weekday_tuesday,
            2 to R.string.lunch_weekday_wednesday,
            3 to R.string.lunch_weekday_thursday,
            4 to R.string.lunch_weekday_friday
        )

        fun from(index: Int, name: String) = TitleMessage(
            resId =map[index] ?: R.string.lunch_weekday_unknown,
            name = name
        )
    }
}
