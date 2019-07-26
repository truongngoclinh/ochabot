package vn.ochabot.seaconnect.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("comments")
    var comments: ArrayList<String> = ArrayList(),
    @SerializedName("desc")
    var desc: String = "",
    @SerializedName("host")
    var host: String = "",
    @SerializedName("location")
    var location: String = "",
    @SerializedName("members")
    var members: ArrayList<String> = ArrayList(),
    @SerializedName("name")
    var name: String = "",
    @SerializedName("timestamp")
    var time: Long = 0L
)

data class Comment(
    @SerializedName("cmt")
    var cmt: String? = "",
    @SerializedName("user")
    var user: String? = ""
)
