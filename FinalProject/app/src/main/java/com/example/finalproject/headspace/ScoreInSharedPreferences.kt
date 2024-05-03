package com.example.finalproject.headspace

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class ScoreInSharedPreferences {
    companion object {
        fun addScoreToSharedPreferences(context: Context, newScore: Int) {
            val sharedPreferences = context.getSharedPreferences("HeadspaceScore", Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("score", newScore).apply()
        }

        fun getStoredScore(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences("HeadspaceScore", Context.MODE_PRIVATE)
            return sharedPreferences.getInt("score", 0)
        }
    }
}
