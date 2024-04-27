package com.example.finalproject.dietplan

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MealPlanner {
    private const val BASE_URL = "https://api.spoonacular.com"
    val instance: SpoonacularApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpoonacularApiService::class.java)
    }
}