package com.example.finalproject.headspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.finalproject.R


class SleepMeditationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SleepMeditationActivityScreen()
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToHeadSpaceHome(context)
    }
}
data class SleepStory(val imageResId: Int, val url: String)

@Composable
fun SleepMeditationActivityScreen() {
    val sleepStories = listOf(
        SleepStory(R.drawable.bedtime1, "https://www.storynory.com/the-cat-who-was-afraid-of-mice/"),
        SleepStory(R.drawable.bedtime2, "https://www.storynory.com/narcissus/"),
        SleepStory(R.drawable.bedtime3, "https://www.storynory.com/how-apollo-found-his-lyre/"),
        SleepStory(R.drawable.bedtime4, "https://www.storynory.com/androcles-and-the-lion/"),
        SleepStory(R.drawable.bedtime5, "https://www.storynory.com/childe-rowland/"),
        SleepStory(R.drawable.bedtime6, "https://www.storynory.com/rowland-2-rowland-at-the-inn/"),
        SleepStory(R.drawable.bedtime7, "https://www.storynory.com/rowland-3-rowland-and-the-herbal-cure/"),
        SleepStory(R.drawable.bedtime8, "https://www.storynory.com/rowland-4-the-fairy-queen/")
    )

    LazyColumn {
        items(sleepStories) { story ->
            SleepStoryItem(story)
        }
    }
}

@Composable
fun SleepStoryItem(story: SleepStory) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .build()
                customTabsIntent.launchUrl(context, Uri.parse(story.url))
                HeadspaceScore.setScore(HeadspaceScore.getScore() + 2)
                val currentScore = HeadspaceScore.getScore()
                ScoreInSharedPreferences.addScoreToSharedPreferences(context,currentScore)
            }
    ) {
        Image(
            painter = painterResource(id = story.imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}



private fun navigateToHeadSpaceHome(context: Context) {
    val intent = Intent(context, HeadSpaceHome::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}