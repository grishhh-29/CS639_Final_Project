package com.example.finalproject.dietplan
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.finalproject.R
import com.example.finalproject.ui.theme.AquaBlue
import com.example.finalproject.ui.theme.BlueViolet1
import com.example.finalproject.ui.theme.ButtonBlue
import com.example.finalproject.ui.theme.DeepBlue
import com.example.finalproject.ui.theme.LightGreen1
import com.example.finalproject.ui.theme.LightGreen2
import com.example.finalproject.ui.theme.PurpleGrey80
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SuccessActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)

        // Check if user is not logged in
        if (!sharedPreferences.getBoolean("is_logged_in", false)) {
            navigateToLogin(this)
            finish()
            return
        }

        setContent {
            SuccessScreen(auth, sharedPreferences)
        }
    }
}
@Composable
fun SuccessScreen(auth: FirebaseAuth, sharedPreferences: SharedPreferences) {
    var expandedMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentUser = auth.currentUser
    // Check if the current user is signed in with Google
    val isGoogleSignIn = currentUser?.let {
        it.providerData.any { userInfo -> userInfo.providerId == GoogleAuthProvider.PROVIDER_ID }
    } ?: false
    val user = auth.currentUser
    val displayName = user?.displayName
    Box(modifier = Modifier.fillMaxSize()) {
        /*Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
               // text = "Welcome ${displayName ?: ""}",
                text = "Welcome $displayName",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Log.d("TAG", "displayName+++: $displayName")*/
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                MyDietPlannerCard(auth,context)
            }
        )
        // Menu button at Bottom right
        IconButton(onClick = { expandedMenu = true }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
        }

        DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false },
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                if (!isGoogleSignIn) {
                    DropdownMenuItem(
                        text = { Text("Reset Password") },
                        onClick = {
                            Toast.makeText(
                                context,
                                "Redirecting to Password Reset",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToResetPassword(context)
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Support") },
                    onClick = {
                        Toast.makeText(
                            context,
                            "Contact: vs42370n@pace.edu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        logout(context, sharedPreferences)
                    }
                )
            }
        Spacer(modifier = Modifier.width(16.dp))
        /*Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Menu",
            )
        }*/
        BottomMenu(
            items = listOf(
                BottomMenuContent("Home", R.drawable.ic_home),
                BottomMenuContent("Music", R.drawable.ic_music),
                BottomMenuContent("Profile", R.drawable.ic_profile)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor : Color = Color.Black,
    inactiveTextColor: Color = AquaBlue,
    initialSelectedItemIndex: Int = 0
){
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor
            ) {
                selectedItemIndex = index
                when (index) {
                    0 -> navigateToSuccessActivity(context) // Navigate to SuccessActivity when "Home" is clicked
                    1 -> {
                        navigateToResetPassword(context)
                    }
                    2 -> {
                        navigateToUserProfile(context)
                    }
                }
            }
        }
    }
}
@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor : Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
    onItemClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if(isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = item.title,
            color = if(isSelected) activeTextColor else inactiveTextColor
        )
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDietPlannerCard(auth: FirebaseAuth,context: Context) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                //verticalArrangement = Arrangement.Top,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "What are you feeling today!!",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            // Load GIF into ImageView
            val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))
            // Load Lottie animation
            LottieAnimation(
                modifier = Modifier.height(500.dp)
                    .weight(1f),
                composition = composition.value
            )
            Spacer(modifier = Modifier.height(16.dp))
            val composition1 = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie3))
            // Load Lottie animation
            LottieAnimation(
                modifier = Modifier.height(500.dp)
                    .weight(1f),
                composition = composition1.value
            )
        }
        // Cards content in a Column
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // First Card - My Diet Planner
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Navigate to GoalSelectionActivity or screen here
                    Toast.makeText(context, "Redirecting to your Goal", Toast.LENGTH_SHORT).show()
                    navigateToMyDietPlanner(context)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "My Diet Planner",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Second Card - Begin your Workout
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Handle click for Begin your Workout card
                    Toast.makeText(context, "Begin your Workout", Toast.LENGTH_SHORT).show()
                    // Add navigation logic or any other action here
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Calorie Tracker",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Third Card - Headspace
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Handle click for Begin your Workout card
                    Toast.makeText(context, "Lets talk...", Toast.LENGTH_SHORT).show()
                    // Add navigation logic or any other action here
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Head-Space",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun navigateToMyDietPlanner(context: Context) {
    val intent = Intent(context, MyDietPlanner::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun logout(context: Context, sharedPreferences: SharedPreferences) {
    val auth = FirebaseAuth.getInstance()

    // Sign out the user from Firebase
    auth.signOut()

    // Revoke access token from Google
    val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
    googleSignInClient.revokeAccess().addOnCompleteListener {
        // Update login status
        sharedPreferences.edit {
            putBoolean("is_logged_in", false)
        }

        // Navigate to login activity
        navigateToLogin(context)
    }
}


private fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}
private fun navigateToResetPassword(context: Context) {
    val intent = Intent(context, PasswordResetScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToSuccessActivity(context: Context) {
    // Check if the current activity is not already SuccessActivity
    if (context !is SuccessActivity) {
        val intent = Intent(context, SuccessActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
private fun navigateToUserProfile(context: Context) {
    // Check if the current activity is not already SuccessActivity
    if (context !is UserProfile) {
        val intent = Intent(context, UserProfile::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
