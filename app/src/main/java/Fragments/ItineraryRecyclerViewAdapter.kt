package Fragments

import Models.Trips.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amigo.R
import com.example.amigo.databinding.ItineraryItemBinding

class ItineraryRecyclerViewAdapter(
    private val values: List<Activity>
) : RecyclerView.Adapter<ItineraryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItineraryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.activityName.text = item.name
        holder.activityAddress.text = item.address
        holder.activityDate.text = item.startDate.toString()
        holder.activityStartTime.text = item.startTime.toString()
        holder.activityEndTime.text = item.endTime.toString()
        when(item.category){
            "Flight" -> holder.activityCategoryPic.setImageResource(R.drawable.plane_pic)
            "Outdoor" -> holder.activityCategoryPic.setImageResource(R.drawable.outdoor_pic)
            "Kids" -> holder.activityCategoryPic.setImageResource(R.drawable.childrens_activities_pic)
            "Entertainment" -> holder.activityCategoryPic.setImageResource(R.drawable.entertainment_pic)
            "Travel" -> holder.activityCategoryPic.setImageResource(R.drawable.travel_pic)
            else -> holder.activityCategoryPic.setImageResource(R.drawable.image_placeholder)
        }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItineraryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val activityName: TextView = binding.lblActivityName
        val activityDate: TextView = binding.lblActivityDate
        val activityStartTime: TextView = binding.lblActivityStartTime
        val activityEndTime: TextView = binding.lblActivityEndTime
        val activityAddress: TextView = binding.lblActivityAddress
        val activityCategoryPic: ImageView = binding.ivActivityCategoryPic

        override fun toString(): String {
            return super.toString() + " '" + activityName.text + "'"
        }
    }

}