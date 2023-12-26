package com.example.ardian
import java.io.Serializable

data class MediaFile(val id: Long, val title: String, val artist: String, val album: String, val data: String) : Serializable