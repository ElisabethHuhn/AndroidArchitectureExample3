package com.huhn.androidarchitectureexample.viewmodel

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Dates
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.EnhancedDateItem

data class DatesState(
    val dates : ArrayList<Dates> = arrayListOf(),
    val selectedDate: Dates? = null,
    val enhancedDates: ArrayList<EnhancedDateItem> = arrayListOf(),
    val selectedEnhancedDate: EnhancedDateItem? = null,
    val errors: String = ""
    )

sealed interface DatesUserEvent {
    data object GetDates : DatesUserEvent
    data class SelectDate(val dates : Dates) : DatesUserEvent
    data class SelectEnhancedDate(val dates : EnhancedDateItem) : DatesUserEvent
    data object PrintDates : DatesUserEvent
    data object DeleteDates : DatesUserEvent

    data class SetError(val error: String) : DatesUserEvent
    data object ClearError : DatesUserEvent
}
