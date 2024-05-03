package com.example.finalproject.headspace.databaseclass
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


// Define the database helper class
class VideoDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Define table creation SQL statement
        val sql_create_video_table = """
            CREATE TABLE ${VideoContract.VideoEntry.TABLE_NAME} (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${VideoContract.VideoEntry.COLUMN_URL} TEXT NOT NULL
            );
        """.trimIndent()

        // Execute the SQL statement to create the table
        db.execSQL(sql_create_video_table)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if necessary
    }

    // Method to insert a video URL into the database
    fun insertVideo(url: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(VideoContract.VideoEntry.COLUMN_URL, url)
        }
        return db.insert(VideoContract.VideoEntry.TABLE_NAME, null, values)
    }
    // Method to clear all videos from the database
    fun clearVideos() {
        val db = writableDatabase
        db.delete(VideoContract.VideoEntry.TABLE_NAME, null, null)
    }

    // Method to retrieve all video URLs from the database
    fun getAllVideos(): List<String> {
        val videos = mutableListOf<String>()
        val db = readableDatabase
        val projection = arrayOf(VideoContract.VideoEntry.COLUMN_URL)
        val cursor = db.query(
            VideoContract.VideoEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val url = getString(getColumnIndexOrThrow(VideoContract.VideoEntry.COLUMN_URL))
                videos.add(url)
            }
        }
        cursor.close()
        return videos
    }
    fun videoExists(videoUrl: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM ${VideoContract.VideoEntry.TABLE_NAME} WHERE ${VideoContract.VideoEntry.COLUMN_URL} = ?"
        val cursor = db.rawQuery(query, arrayOf(videoUrl))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}

// Define database name and version
private const val DATABASE_NAME = "video.db"
private const val DATABASE_VERSION = 1