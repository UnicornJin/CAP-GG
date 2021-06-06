package com.example.capgg.calculator

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.capgg.data.GradeDatabaseDao
import com.example.capgg.data.GradeRecord
import com.example.capgg.network.PriceApi
import com.example.capgg.network.response
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PriceApiStatus { LOADING, ERROR, DONE }

class HomePageViewModel(
    val dataSource: GradeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val records = dataSource.getAllRecords()

    private val _navigateToAddGradeRecord = MutableLiveData<Boolean>()
    val navigateToAddGradeRecord: LiveData<Boolean>
        get() = _navigateToAddGradeRecord

    fun signalNavigating() {
        _navigateToAddGradeRecord.value = true
    }

    fun doneNavigating() {
        _navigateToAddGradeRecord.value = null
    }

    private val _navigateToRecordEdit = MutableLiveData<Long>()
    val navigateToRecordEdit: LiveData<Long>
        get() = _navigateToRecordEdit

    fun onGradeRecordClicked(id: Long) {
        _navigateToRecordEdit.value = id
    }

    fun onGradeRecordEditNavigated() {
        _navigateToRecordEdit.value = null
    }

    private val _totalCAP = MutableLiveData<Double>()
    val totalCAP: LiveData<Double>
        get() = _totalCAP

    fun updateCAP() {
        _totalCAP.value = records.value?.let { capCompute(it) }
    }


    fun capCompute(records: List<GradeRecord>): Double {
        var result: Double = 5.0
        var sumCreditPGrade: Double = 0.0
        var sumCreditCountable: Double = 0.0
        for (record in records) {
            if (!record.suStatus) {
                sumCreditCountable += record.moduleCredit
                sumCreditPGrade += record.moduleCredit * record.moduleGrade!!
            }
        }
        if (sumCreditCountable > 0.0) {
            result = sumCreditPGrade / sumCreditCountable
        }
        return result
    }


    private val _status = MutableLiveData<PriceApiStatus>()
    val status: LiveData<PriceApiStatus>
        get() = _status

    private val _properties = MutableLiveData<response>()
    val properties: LiveData<response>
        get() = _properties

    private val _connected = MutableLiveData<Boolean>()
    val connected: LiveData<Boolean>
        get() = _connected

    init {
        getPriceProperties()
    }

    private fun getPriceProperties() {
        viewModelScope.launch {
            while (true) {
                PriceUpdate()
                delay(5000)
            }
        }

    }

    private suspend fun PriceUpdate() {
        try {
            Log.i("BitCoin Price", "requesting")
            _connected.value = false
            _status.value = PriceApiStatus.LOADING
            var response = PriceApi.retrofitService.getProperties()
            Log.i("BitCoin Price", "${response.toString()}")
            _status.value = PriceApiStatus.DONE
            if (response != null) {
                Log.i("BitCoin Price", "Got result")
                _connected.value = true
                _properties.value = response
            }
        } catch (e: Exception) {
            Log.i("BitCoin Price", "Error")
            _status.value = PriceApiStatus.ERROR
            _properties.value = null
            e.printStackTrace()
        }
    }
}