package e.vatsal.newsfeed.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsModel(
    @SerializedName("articles")
    var articles: ArrayList<Article>? = arrayListOf(),
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("totalResults")
    var totalResults: Int? = 0
): Parcelable