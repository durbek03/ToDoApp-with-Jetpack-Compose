package com.example.mvcexample.globalui

import java.util.*

fun Int.formatTaskAmountText(): String {
    var sizeText = this.toString()
    return if (sizeText.toInt() >= 0 && sizeText.toInt() != 1) {
        sizeText += " tasks"
        sizeText
    } else {
        sizeText += " task"
        sizeText
    }
}

fun String.checkIfToday(): Boolean {
    val calendar = Calendar.getInstance()
    val calYear = calendar.get(Calendar.YEAR)
    val calMonth = calendar.get(Calendar.MONTH)
    val calDay = calendar.get(Calendar.DAY_OF_MONTH)
    val year = this.substring(6).toInt()
    val month = this.substring(3, 5).toInt().minus(1)
    val day = this.substring(0, 2).toInt()
    return calYear == year && calDay == day && calMonth == month
}

fun String.checkIfTomorrow(): Boolean {
    val calendar = Calendar.getInstance()
    val calYear = calendar.get(Calendar.YEAR)
    val calMonth = calendar.get(Calendar.MONTH)
    val calDay = calendar.get(Calendar.DAY_OF_MONTH)
    val year = this.substring(6).toInt()
    val month = this.substring(3, 5).toInt().minus(1)
    val day = this.substring(0, 2).toInt()
    return calYear == year && calMonth == month && calDay + 1 == day
}