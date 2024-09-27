package Models.NearbySearch

import com.google.gson.annotations.SerializedName


data class NearbySearchResult (

    @SerializedName("html_attributions" ) var htmlAttributions : ArrayList<String>  = arrayListOf(),
    @SerializedName("results"           ) var results          : ArrayList<Results> = arrayListOf(),
    @SerializedName("status"            ) var status           : String?            = null

)