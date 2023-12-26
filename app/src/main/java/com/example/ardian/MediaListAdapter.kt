package com.example.ardian
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
//import com.example.ardian.MediaFile
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView

class MediaListAdapter(context: Context, resource: Int, objects: List<MediaFile>, private val onItemClickListener: ((MediaFile) -> Unit)? = null) :
    ArrayAdapter<MediaFile>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mediaFile = getItem(position)
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item_media_file,
            parent,
            false
        )

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val artistTextView: TextView = view.findViewById(R.id.artistTextView)
        val albumCoverImageView: ImageView = view.findViewById(R.id.albumCoverImage)

        titleTextView.text = mediaFile?.title
        artistTextView.text = mediaFile?.artist

        albumCoverImageView.setImageResource(R.drawable.album)

        loadAlbumCoverAsync(mediaFile?.data, albumCoverImageView)

        view.setOnClickListener {
            onItemClickListener?.invoke(mediaFile!!)
        }

        return view
    }

    private fun loadAlbumCoverAsync(data: String?, imageView: ImageView) {
        data?.let {
            val metadataRetriever = MediaMetadataRetriever()
            metadataRetriever.setDataSource(data)
            val rawAlbumCover = metadataRetriever.embeddedPicture

            if(rawAlbumCover != null) {
                val bitmap = BitmapFactory.decodeByteArray(rawAlbumCover, 0, rawAlbumCover.size)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

//    private fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }
}