package com.example.finalproject.headspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.finalproject.R
import com.example.finalproject.dietplan.getBMICategory
import com.example.finalproject.headspace.databaseclass.VideoDbHelper
import com.example.finalproject.ui.theme.TextWhite

class PlayScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            fetchRandomContent(this)
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToHeadSpaceHome(context)
    }
}
@Composable
fun fetchRandomContent(lifecycleOwner: LifecycleOwner) {
    val context = LocalContext.current
    var positiveThought by remember {
        mutableStateOf("")
    }
    var videoUrl by remember {
        mutableStateOf("")
    }

    // Initialize VideoDbHelper
    val dbHelper = VideoDbHelper(LocalContext.current)

    dbHelper.clearVideos()

    // Example video URLs
    val videoUrls = listOf(
        "https://drive.google.com/uc?export=download&id=1ISO-RtefxE3EFLWjdSFguTo39upR08fl",
        "https://drive.google.com/uc?export=download&id=1To-k3iQHaFOIC4IlSwrg8Kqvuutj4rt0",
        "https://drive.google.com/uc?export=download&id=17Vsi_ckqf4puKCELiyFF_tbSIwv9fuip"
    )

    // Insert each video URL into the database if it doesn't already exist
    for (url in videoUrls) {
        if (!dbHelper.videoExists(url)) {
            dbHelper.insertVideo(url)
        }
    }

    // Get all videos from the database
    val videos = dbHelper.getAllVideos()

    // Select a random video URL
    videoUrl = if (videos.isNotEmpty()) {
        // Generate a random index
        val randomIndex = videos.indices.random()

        // Get the randomly selected video URL
        videos[randomIndex]
    } else {
        // Handle the case where no videos are available in the database
        // You can display a message or take appropriate action
        // For example:
        "No videos available"
    }

    // Select a random positive thought
    val positiveThoughts = listOf(
        "“Keep your face always toward the sunshine—and shadows will fall behind you.” —Walt Whitman",
        "“Setting goals is the first step in turning the invisible into the visible.” —Tony Robbins",
        "“You can have it all. Just not all at once.” —Oprah Winfrey",
        "“Think and wonder. Wonder and think.” —Dr. Suess",
        "“Today is a good day to try.” —Quasimodo, The Hunchback of Notre Dame"
    )
    val randomIndex = positiveThoughts.indices.random()
    positiveThought = positiveThoughts[randomIndex]

    // Prepare the video URI
    val videoUri = Uri.parse(videoUrl)

    // Set up the MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setDataSource(context, videoUri)
            setOnPreparedListener {
                start() // Start playback only after the MediaPlayer is prepared
            }
            setOnErrorListener { mp, what, extra ->
                Log.e("MediaPlayer", "Error occurred: $what, $extra")
                // Handle errors here
                true // Return true to indicate that the error has been handled
            }
            isLooping = true
            prepareAsync() // Start asynchronous preparation of the MediaPlayer
        }
    }

    // Lifecycle Observer
    val observer = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mediaPlayer.start()
                }
                Lifecycle.Event.ON_STOP -> {
                    mediaPlayer.pause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
                else -> {
                    // Do nothing for other events
                }
            }
        }
    }

    // Observe lifecycle events
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Display the video
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val surfaceView = SurfaceView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            mediaPlayer.setDisplay(holder)
                        }

                        override fun surfaceChanged(
                            holder: SurfaceHolder,
                            format: Int,
                            width: Int,
                            height: Int
                        ) {
                        }

                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                        }
                    })
                }
                surfaceView
            }
        )
        // Display the positive thought
        Text(
            text = positiveThought,
            style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold,fontSize = 24.sp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }

    // Observe lifecycle events
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
