package com.example.finalproject.headspace

class HeadspaceScore private constructor(private var score: Int) {

    companion object {
        private var instance: HeadspaceScore? = null

        fun getInstance(): HeadspaceScore {
            if (instance == null) {
                instance = HeadspaceScore(0) // Default score is 0
            }
            return instance!!
        }

        fun getScore(): Int {
            return getInstance().score
        }

        fun setScore(newScore: Int) {
            getInstance().score = newScore
        }
    }
}
