package com.example.finalproject.headspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.finalproject.R
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp



class SleepTipsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepTipsScreen()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToHeadSpaceHome(context)
    }
}
@Composable
fun SleepTipsScreen() {
    val tips = listOf(
        "Stick to a sleep schedule" to "Set aside no more than eight hours for sleep. The recommended amount of sleep for a healthy adult is at least seven hours. Most people don't need more than eight hours in bed to be well-rested. Go to bed and get up at the same time every day, including weekends. Being consistent reinforces your body's sleep-wake cycle. If you don't fall asleep within about 20 minutes of going to bed, leave your bedroom and do something relaxing. Read or listen to soothing music. Go back to bed when you're tired. Repeat as needed, but continue to maintain your sleep schedule and wake-up time.",
        "Pay attention to what you eat and drink" to "Don't go to bed hungry or stuffed. In particular, avoid heavy or large meals within a couple of hours of bedtime. Discomfort might keep you up. Nicotine, caffeine, and alcohol deserve caution, too. The stimulating effects of nicotine and caffeine take hours to wear off and can interfere with sleep. And even though alcohol might make you feel sleepy at first, it can disrupt sleep later in the night.",
        "Create a restful environment" to "Keep your room cool, dark, and quiet. Exposure to light in the evenings might make it more challenging to fall asleep. Avoid prolonged use of light-emitting screens just before bedtime. Consider using room-darkening shades, earplugs, a fan, or other devices to create an environment that suits your needs. Doing calming activities before bedtime, such as taking a bath or using relaxation techniques, might promote better sleep.",
        "Limit daytime naps" to "Long daytime naps can interfere with nighttime sleep. Limit naps to no more than one hour and avoid napping late in the day. However, if you work nights, you might need to nap late in the day before work to help make up your sleep debt.",
        "Include physical activity in your daily routine" to "Regular physical activity can promote better sleep. However, avoid being active too close to bedtime. Spending time outside every day might be helpful, too.",
        "Manage worries" to "Try to resolve your worries or concerns before bedtime. Jot down what's on your mind and then set it aside for tomorrow. Stress management might help. Start with the basics, such as getting organized, setting priorities, and delegating tasks. Meditation also can ease anxiety.",
        "Get checked" to "An urge to move your legs, snoring, and a burning pain in your stomach, chest, or throat are symptoms of three common sleep disrupters—restless legs syndrome, sleep apnea, and gastroesophageal reflux disease or GERD. If these symptoms are keeping you up at night or making you sleepy during the day, see your doctor for an evaluation.",
        "Avoid alcohol and caffeine" to "If you do have a snack before bed, wine and chocolate shouldn't be part of it. Chocolate contains caffeine, which is a stimulant. Surprisingly, alcohol has a similar effect. It makes you a little sleepy, but it's actually a stimulant and it disrupts sleep during the night. Also stay away from anything acidic (such as citrus fruits and juices) or spicy, which can give you heartburn."
    )
    /*Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

    }*/
    Text(
        text = "Tips...",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.padding(start = 8.dp)
    )
    LazyColumn {
        items(tips) { (title, content) ->
            ExpandableTipSection(title = title, content = content)
        }
    }
}

@Composable
fun ExpandableTipSection(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }
    var bookmarked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(
        "FavoriteTips",
        Context.MODE_PRIVATE
    )

    val tipKey = "Tip_$title"

    // Load the favorite status from SharedPreferences
    bookmarked = sharedPreferences.getBoolean(tipKey, false)

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        bookmarked = !bookmarked
                        // Save the favorite status to SharedPreferences
                        sharedPreferences.edit().putBoolean(tipKey, bookmarked).apply()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (bookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = if (bookmarked) Color.Red else Color.White
                    )
                }
            }
            if (expanded) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}


private fun navigateToHeadSpaceHome(context: Context) {
    val intent = Intent(context, HeadSpaceHome::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}