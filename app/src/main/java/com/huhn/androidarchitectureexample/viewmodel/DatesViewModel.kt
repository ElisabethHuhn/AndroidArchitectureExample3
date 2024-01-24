package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DatesApiService
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Dates
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.EnhancedDateItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class DatesViewModel(
    private val datesApiService: DatesApiService
) : ViewModel()
{
    private val _datesState = MutableStateFlow(DatesState())
    var datesState = _datesState.asStateFlow()

    fun onDatesUserEvent(event: DatesUserEvent) {
        when (event) {
            is DatesUserEvent.GetDates -> getDates()
            is DatesUserEvent.SelectDate -> onSelectDateChanged(event.dates)
            is DatesUserEvent.SelectEnhancedDate -> onSelectEnhancedDateChanged(event.dates)
            is DatesUserEvent.SetError -> onErrorChanged(event.error)
            is DatesUserEvent.ClearError -> onClearError()
            else -> {}
        }
    }

    fun getDates() {
        viewModelScope.launch {
            try {
                val datesResponse = datesApiService.fetchDates()

                onDatesListChanged(datesResponse)
            }
            catch (e: Exception) {
                onErrorChanged(e.message ?: "Try/Catch: Error loading dates")
            }
        }
    }

    fun getEnhancedDates (dateString: String) {
        viewModelScope.launch {
            try {
                val datesResponse = datesApiService.fetchEnhancedDates(dateString)

                onEnhancedDatesListChanged(datesResponse)
            }
            catch (e: Exception) {
                onErrorChanged(e.message ?: "Try/Catch: Error loading enhanced dates")
            }
        }
    }




    //region state update functions
    private fun onEnhancedDatesListChanged (dates: ArrayList<EnhancedDateItem>) {
        _datesState.update {
            it.copy(enhancedDates = dates)
        }
    }


    private fun onDatesListChanged (dates: ArrayList<Dates>) {
        _datesState.update {
            it.copy(dates = dates)
        }
    }

    private fun onSelectDateChanged (selectedDate: Dates) {
        _datesState.update {
            it.copy(selectedDate = selectedDate)
        }
    }
    private fun onSelectEnhancedDateChanged (selectedDate: EnhancedDateItem) {
        _datesState.update {
            it.copy(selectedEnhancedDate = selectedDate)
        }
    }

    protected fun onErrorChanged(error: String){
        _datesState.update {
            it.copy(errors = error)
        }
    }

    private fun onClearError(){
        _datesState.update {
            it.copy(errors = "")
        }
    }

    //end region

    //region actions in response to user event triggers. See onUserEvent() above

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _datesState.update {
            it.copy(errors = throwable.message ?: "CoroutineExceptionHandler : Error loading dates and routes")
        }
    }


    //end region
}