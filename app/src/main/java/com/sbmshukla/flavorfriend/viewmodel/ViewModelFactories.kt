package com.sbmshukla.flavorfriend.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbmshukla.flavorfriend.reposetories.Repository


class HomeViewModelFactory(private val application:Application,private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(application=application,repository = repository) as T
    }
}

class MealViewModelFactory(private val application:Application,private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(application=application,repository = repository) as T
    }
}
