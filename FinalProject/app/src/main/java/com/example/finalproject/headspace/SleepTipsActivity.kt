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
        "Avoid alcohol and caffeine" to "If you do have a snack before bed, wine and chocolate shouldn't be part of it. Chocolate contains caffeine, which is a stimulant. Surprisingly, alcohol has a similar effect. It makes you a little sleepy, but it's actually a stimulant and it disrupts sleep during the night. Also stay away from anything acidic (such as citrus fruits and juices) or spicy, which can give you heartburn.",
        "Increase Bright light Exposure" to "Your body has a natural time-keeping clock known as your circadian rhythm. It affects your brain, body, and hormones, helping you stay awake and telling your body when it’s time to sleep. Natural sunlight or bright light during the day helps keep your circadian rhythm healthy. This improves daytime energy, as well as nighttime sleep quality and duration.In people with insomnia, daytime bright light exposure improved sleep quality and durationTrusted Source. It also reduced the time it took to fall asleep by 83%. A similar study in older adults found that 2 hoursTrusted Source of bright light exposure during the day increased the amount of sleep by 2 hours and sleep efficiency by 80%",
        "Reduce blue light exposure" to "Exposure to light during the day is beneficial, but nighttime light exposure has the opposite effect.Blue light — which electronic devices like smartphones and computers emit in large amounts — is the worst in this regard.\n" +
                "There are several popular methods you can use to reduce nighttime blue light exposure. These include:\n" +
                "I) Wear glasses that block blue light.\n" +
                "II) Download an app such as f.lux to block blue light on your laptop or computer.\n" +
                "III) Install an app that blocks blue light on your smartphone. These are available for both iPhones and Android models.\n" +
                "IV) Stop watching TV and turn off any bright lights 2 hours before heading to bed.",
        "Take a melatonin supplement" to "Melatonin is a key sleep hormoneTrusted Source that tells your brain when it’s time to relax and head to bed. Melatonin supplements are an extremely popular sleep aid. Often used to treat insomniaTrusted Source, melatonin may be one of the easiest ways to fall asleep faster. In one study, taking 2 mg of melatonin before bed improved sleep quality and energy the next day and helped people fall asleep faster.",
        "Set room temperature" to "Body and bedroom temperature can also profoundly affect sleep quality. As you may have experienced during the summer or in hot locations, it can be very hard to get a good night’s sleep when it’s too warm. One studyTrusted Source found that bedroom temperature affected sleep quality more than external noise. Other studiesTrusted Source reveal that increased body and bedroom temperature can decrease sleep quality and increase wakefulness. Around 70°F (20°C) seems to be a comfortable temperature for most people, although it depends on your preferences and habits"
    )
    Column(
        modifier = Modifier
            .background(Color.LightGray)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tips...",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 16.dp)
            )
        }
        LazyColumn {
            items(tips) { (title, content) ->
                ExpandableTipSection(title = title, content = content)
            }
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
                        // Increment the Headspace score by 10 if the content is marked as favorite
                        if (bookmarked) {
                            HeadspaceScore.setScore(HeadspaceScore.getScore() + 10)
                            val currentScore = HeadspaceScore.getScore()
                            ScoreInSharedPreferences.addScoreToSharedPreferences(context,currentScore)
                        }
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