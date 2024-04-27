package com.example.finalproject.dietplan

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("/mealplanner/generate")
    fun generateMealPlan(
        @Query("timeFrame") timeFrame: String? = null,
        @Query("targetCalories") targetCalories: Int? = null,
        @Query("diet") diet: String? = null,
        @Query("apiKey") apiKey: String
    ): Call<MealNutrients>
    @GET("/mealplanner/generate")
    fun generateMealPlanbyDay(
        @Query("timeFrame") timeFrame: String? = null,
        @Query("targetCalories") targetCalories: Int? = null,
        @Query("diet") diet: String? = null,
        @Query("apiKey") apiKey: String
    ): Call<Day>
}
