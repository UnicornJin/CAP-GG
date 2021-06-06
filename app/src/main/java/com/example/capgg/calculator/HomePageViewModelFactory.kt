package com.example.capgg.calculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capgg.data.GradeDatabaseDao

class HomePageViewModelFactory(
    private val dataSource: GradeDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
            return HomePageViewModel(dataSource, application) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}