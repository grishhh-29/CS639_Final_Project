package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import com.example.finalproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class loginWithGoogle : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            navigateToSuccessActivity(this)
            finish()
            return
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.id2))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Launch Google sign-in intent
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)!!
                firebaseAuthWithGoogle(account,sharedPreferences)
            } catch (e: com.google.android.gms.common.api.ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToLoginBack(context)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount,sharedPreferences: SharedPreferences) {
        val auth = FirebaseAuth.getInstance() // Get FirebaseAuth instance
        val context = applicationContext
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    sharedPreferences.edit {
                        putBoolean("is_logged_in", true)
                    }
                    navigateToSuccessActivity(context)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context,
                        "Invalid credentials. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateToSuccessActivity(context: Context) {
        val intent = Intent(context, SuccessActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
    private fun navigateToLoginBack(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
        (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}