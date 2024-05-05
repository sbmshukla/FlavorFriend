package com.sbmshukla.flavorfriend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sbmshukla.flavorfriend.pojo.MealList
import com.sbmshukla.flavorfriend.reposetories.Repository
import com.sbmshukla.flavorfriend.utils.Constance
import com.sbmshukla.flavorfriend.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MealViewModel(application: Application, private val repository: Repository) :
    AndroidViewModel(application) {

    var mealDetailsLiveData: MutableLiveData<Resource<MealList>> = MutableLiveData()

    fun getMealDetails(id:String) = viewModelScope.launch(Dispatchers.IO) {
        mealDetailsInternet(id=id)
    }

    private suspend fun mealDetailsInternet(id:String) {
        mealDetailsLiveData.postValue(Resource.Loading())
        try {
            if (Constance.isInternetConnected(this.getApplication())) {
                val response = repository.getMealDetails(id)
                mealDetailsLiveData.postValue(handleGetMealDetailsResponse(response))
            } else {
                mealDetailsLiveData.postValue(Resource.Error(message = "No Internet connection...!!!"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> mealDetailsLiveData.postValue(Resource.Error(message = "Unable to connect...!!!"))
                else -> mealDetailsLiveData.postValue(Resource.Error(message = "No Signal...!!!"))
            }
        }
    }

    private fun handleGetMealDetailsResponse(response: Response<MealList>): Resource<MealList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }
}