package Fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import mtm.example.amigo.databinding.SearchResultItemBinding
import com.google.android.libraries.places.api.model.Place
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import kotlin.concurrent.thread

class SearchResultRecyclerViewAdapter(
    private val values: List<Place>
) : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            SearchResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.placeName.text = item.displayName
        holder.placeAddress.text = item.formattedAddress
        holder.placeRating.text = "Rating: ${item.rating}/5"
        //holder.placeProfilePic.setImageResource(R.drawable.image_placeholder)
        //holder.placeProfilePic.setImageBitmap(getImageBitmap(item.photoMetadatas[7].authorAttributions.asList()[0].photoUri))
        Log.i("Checking API Results", "${item.photoMetadatas[7].authorAttributions.asList()[0].photoUri.toUri()}")
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val placeName: TextView = binding.tvPlaceName
        val placeAddress: TextView = binding.tvPlaceAddress
        val placeRating: TextView = binding.tvRating
        //val placeProfilePic: ImageView = binding.ivProfile

        override fun toString(): String {
            return super.toString() + " '" + placeName.text + "'"
        }
    }

    /*
 Stack overflow post: bad bitmap error when setting uri
 Asked by: retnuh
 Date asked: 10 September 2010
 Answered by: paxdiablo
 Date answered: 10 September 2010
 Url: https://stackoverflow.com/questions/3681714/bad-bitmap-error-when-setting-uri
  */
    private fun getImageBitmap(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        thread{
            try {
                val aURL = URL(url)
                val conn = aURL.openConnection()
                conn.connect()
                val inputStream: InputStream = conn.getInputStream()
                val bufferedInputStream = BufferedInputStream(inputStream)
                bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                bufferedInputStream.close()
                inputStream.close()
            } catch (e: Exception) {
                Log.e("Debugging Bitmap", "Error getting bitmap", e)
        }

        }
        return bitmap
    }

}