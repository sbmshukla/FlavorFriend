package com.sbmshukla.flavorfriend.reposetories

import com.sbmshukla.flavorfriend.retrofit.MealApiInterface
import com.sbmshukla.flavorfriend.retrofit.RetrofitInstance

class Repository(private val apiService: MealApiInterface) {
    suspend fun getRandomMeals() =
        apiService.getRandomMeal()

    suspend fun getMealDetails(id: String) =
        apiService.getMealDetails(id = id)

    suspend fun getPopularMeals(categoryName: String) =
        apiService.getPopularMeals(categoryName = categoryName)
}