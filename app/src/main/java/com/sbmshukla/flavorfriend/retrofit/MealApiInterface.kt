package com.sbmshukla.flavorfriend.retrofit

import com.sbmshukla.flavorfriend.pojo.CategoryList
import com.sbmshukla.flavorfriend.pojo.MealList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiInterface {

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealList>

    @GET("lookup.php?")
    suspend fun getMealDetails(@Query("i") id:String): Response<MealList>

    @GET("filter.php?")
    suspend fun getPopularMeals(@Query("c") categoryName:String): Response<CategoryList>
}