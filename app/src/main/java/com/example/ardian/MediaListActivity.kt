package com.example.ardian

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ListView

class MediaListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val listView: ListView = findViewById(R.id.musicList)

        if(checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            val mediaFiles: List<MediaFile> = getMediaFiles()
            val adapter = MediaListAdapter(this, R.layout.list_item_media_file, mediaFiles) { selectedMediaFile ->
                navigateToPlayer(selectedMediaFile)
            }
            listView.adapter = adapter
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                1
            )
        }
    }

    private fun navigateToPlayer(selectedMediaFile: MediaFile) {
        val intent = Intent(this, Player::class.java)
        intent.putExtra("mediaFile", selectedMediaFile)
        startActivity(intent)
    }

    //    @Suppress("DEPRECATION")
    private fun getMediaFiles(): List<MediaFile> {
        val mediaFiles = mutableListOf<MediaFile>()
        val contentResolver: ContentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor: Cursor? = contentResolver.query(uri, projection, selection, null, sortOrder, null)

        cursor?.use{
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(MediaStore.Audio.Media._ID))
                val title = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val album = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val data = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))
                val mediaFile = MediaFile(id, title, artist, album, data)
                mediaFiles.add(mediaFile)
            }
        }

        return mediaFiles
    }
}