package com.sbmshukla.flavorfriend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sbmshukla.flavorfriend.pojo.CategoryList
import com.sbmshukla.flavorfriend.pojo.Meal
import com.sbmshukla.flavorfriend.pojo.MealList
import com.sbmshukla.flavorfriend.reposetories.Repository
import com.sbmshukla.flavorfriend.utils.Constance.Companion.isInternetConnected
import com.sbmshukla.flavorfriend.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel(application: Application, private val repository: Repository) :
    AndroidViewModel(application) {
    var randomMealLiveData: MutableLiveData<Resource<MealList>> = MutableLiveData()
    var popularMealLiveData: MutableLiveData<Resource<CategoryList>> = MutableLiveData()

        init {
            getPopularMeals(categoryName="Seafood")
        }

    fun getRandomMeal() = viewModelScope.launch(Dispatchers.IO) {
        randomMealInternet()
    }

    private fun getPopularMeals(categoryName: String) = viewModelScope.launch(Dispatchers.IO) {
        popularMealInternet(categoryName=categoryName)
    }

    private suspend fun randomMealInternet() {
        randomMealLiveData.postValue(Resource.Loading())
        try {
            if (isInternetConnected(this.getApplication())) {
                val response = repository.getRandomMeals()
                randomMealLiveData.postValue(handleGetRandomMealResponse(response))
            } else {
                randomMealLiveData.postValue(Resource.Error(message = "No Internet connection...!!!"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> randomMealLiveData.postValue(Resource.Error(message = "Unable to connect...!!!"))
                else -> randomMealLiveData.postValue(Resource.Error(message = "No Signal...!!!"))
            }
        }
    }

    private suspend fun popularMealInternet(categoryName:String) {
        popularMealLiveData.postValue(Resource.Loading())
        try {
            if (isInternetConnected(this.getApplication())) {
                val response = repository.getPopularMeals(categoryName=categoryName)
                popularMealLiveData.postValue(handlePopularMealsResponse(response))
            } else {
                popularMealLiveData.postValue(Resource.Error(message = "No Internet connection...!!!"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> popularMealLiveData.postValue(Resource.Error(message = "Unable to connect...!!!"))
                else -> popularMealLiveData.postValue(Resource.Error(message = "No Signal...!!!"))
            }
        }
    }

    private fun handleGetRandomMealResponse(response: Response<MealList>): Resource<MealList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun handlePopularMealsResponse(response: Response<CategoryList>): Resource<CategoryList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }
}