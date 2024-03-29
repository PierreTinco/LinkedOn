package fr.isen.tinco.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Dish(
    @SerializedName("name_fr") val name: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("ingredients") val ingredients: List<Ingredient>,
    @SerializedName("prices") val prices: List<Price>
    ): Serializable {
        fun getThumbnailURL(): String?  {
            return if(images.isNotEmpty() && images.first().isNotEmpty()) {
                images.first()
            } else {
                null
            }
        }
}