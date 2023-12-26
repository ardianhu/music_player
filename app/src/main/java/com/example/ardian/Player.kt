package com.example.ardian

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class Player : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var selectedMediaFile: MediaFile
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val selectedMediaFile = intent.getSerializableExtra("mediaFile") as MediaFile
        currentPosition = intent.getIntExtra("position", 0)

        val songTitleTextView: TextView = findViewById(R.id.songTitleTextView)
        val artistTextView: TextView = findViewById(R.id.songArtistTextView)
        val albumTextView: TextView =findViewById(R.id.songAlbumTextView)
        songTitleTextView.text = selectedMediaFile.title
        artistTextView.text = selectedMediaFile.artist
        albumTextView.text = selectedMediaFile.album

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(selectedMediaFile.data)
        val albumCoverBytes = retriever.embeddedPicture

        if (albumCoverBytes != null) {
            val albumCoverBitmap = BitmapFactory.decodeByteArray(albumCoverBytes, 0, albumCoverBytes.size)
            val albumCoverImage: ImageView =findViewById(R.id.albumCoverImage)
            albumCoverImage.setImageBitmap(albumCoverBitmap)
        }

        mediaPlayer = MediaPlayer()

        mediaPlayer.setDataSource(selectedMediaFile.data)

        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }

//        mediaPlayer.setOnErrorListener {
//            Log.e("MediaPlayer", "error during playback", "ok")
//            false
//        }
        val playerPlayButton: ImageButton = findViewById(R.id.playerPlay)
        playerPlayButton.setOnClickListener{
            togglePlayback()
        }
    }

    private fun togglePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}