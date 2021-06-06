package com.example.capgg.calculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capgg.data.GradeDatabaseDao
import java.lang.Appendable

class AddGradeViewModelFactory(
    val dataSource: GradeDatabaseDao,
    val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGradeViewModel::class.java)) {
            return AddGradeViewModel(dataSource, application) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}