package Fragments

import Models.Trips.Trip
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.amigo.R
import com.example.amigo.TripItineraryActivity
import com.example.amigo.databinding.TripItemBinding

class TripRecyclerViewAdapter(
    private val values: List<Trip>
) : RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder>() {
    lateinit var view: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = parent
        return ViewHolder(
            TripItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)
        if (item != null) {
            holder.tripName.text = item.name
            holder.tripStartDate.text = item.startDate.toString()
            holder.tripEndDate.text = item.endDate.toString()
            holder.tripImage.setImageResource(R.drawable.image_placeholder)
            holder.btnViewTrip.setOnClickListener{
                val intent = Intent(view.context, TripItineraryActivity::class.java)
                intent.putExtra("TripId", item.tripId)
                intent.putExtra("TripName", item.name)
                startActivity(view.context, intent, null)
            }
        }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: TripItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tripName: TextView = binding.lblTripName
        val tripStartDate: TextView = binding.lblTripStartDate
        val tripEndDate: TextView = binding.lblTripEndDate
        val tripImage: ImageView = binding.ivTripImage
        val btnViewTrip: Button = binding.btnViewTrip

        override fun toString(): String {
            return super.toString() + " '" + tripName.text + "'"
        }
    }

}