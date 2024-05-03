package com.example.finalproject.headspace

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

class WorkoutDateManager(context: Context) {
    companion object {
        private const val PREF_NAME = "WorkoutDates"
        private const val PREF_KEY = "workout_dates"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun addWorkoutDate(date: LocalDate) {
        val dates = getWorkoutDates().map { it.toString() }.toMutableSet()
        dates.add(date.toString()) // Convert LocalDate to String here
        prefs.edit().putStringSet(PREF_KEY, dates).apply()
    }


    fun getWorkoutDates(): List<LocalDate> {
        val dates = prefs.getStringSet(PREF_KEY, setOf()) ?: setOf()
        return dates.map { LocalDate.parse(it) }
    }
}

data class WorkoutDate(val date: LocalDate)
