package com.sbmshukla.flavorfriend

import android.app.Application
import com.sbmshukla.flavorfriend.reposetories.Repository
import com.sbmshukla.flavorfriend.retrofit.MealApiInterface
import com.sbmshukla.flavorfriend.retrofit.RetrofitInstance

class FlavorFriendApplication:Application() {
    lateinit var repository: Repository
    lateinit var apiService: MealApiInterface


    override fun onCreate() {
        super.onCreate()
        initialization()
    }

    private fun initialization() {
        apiService = RetrofitInstance.getInstance().create(MealApiInterface::class.java)
        repository = Repository(
            apiService
        )
    }
}