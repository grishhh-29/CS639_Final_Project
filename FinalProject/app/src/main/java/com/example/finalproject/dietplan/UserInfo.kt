package com.example.finalproject.dietplan

import com.google.firebase.auth.FirebaseAuth

class UserInfo {
    /*companion object {
        fun extractUsernameFromEmail(email: String): String {
            return email.substringBefore('@')
        }
    }*/
    companion object {
        fun extractUsernameFromEmail(): String {
            // Regular expression to match the desired pattern (letters before @)
            var auth: FirebaseAuth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val email = user?.email
            val pattern = "^[a-zA-Z]+".toRegex()
            val matchResult = email?.let { pattern.find(it) }
            return matchResult?.value ?: ""
        }
    }
}