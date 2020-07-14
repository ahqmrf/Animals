package tech.tolife.apps.animals.model

import android.os.Parcelable
import android.view.View
import androidx.navigation.Navigation
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import tech.tolife.apps.animals.view.ListFragmentDirections

data class ApiKey(
    val message: String?,
    val key: String?
)

@Parcelize
data class Animal(
    val name: String?,
    val taxonomy: Taxonomy?,
    val location: String?,
    val speed: Speed?,
    val diet: String?,

    @SerializedName("lifespan")
    val lifeSpan: String?,

    @SerializedName("image")
    val imageUrl: String?
) : Parcelable

@Parcelize
data class Taxonomy(
    val kingdom: String?,
    val order: String?,
    val family: String?
) : Parcelable

@Parcelize
data class Speed(
    val metric: String?,
    val imperial: String?
) : Parcelable

data class AnimalPalette(var color: Int)