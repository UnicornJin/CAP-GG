package com.example.capgg.calculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capgg.data.GradeDatabaseDao

class EditGradeViewModelFactory(
    val recordId: Long,
    val dataSource: GradeDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditGradeViewModel::class.java)) {
            return EditGradeViewModel(recordId, dataSource) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}