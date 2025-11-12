package com.example.android.systemdesign.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.systemdesign.data.repository.SystemDesignRepositoryImpl
import com.example.android.systemdesign.domain.usecase.GetSystemDesignTopicsUseCase

class MainViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = SystemDesignRepositoryImpl()
            val useCase = GetSystemDesignTopicsUseCase(repository)
            return MainViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
