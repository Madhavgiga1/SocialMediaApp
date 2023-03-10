package com.example.socialmedialinked.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Indivpost(
    var post_author_name:String,
    var post_timestamp:String,
    var post_text:String?=null,
    var post_image: String?=null,
    var post_author_image:String?=null,
    var post_likes:Likes=Likes(0, emptyList())

): Parcelable {

    constructor() : this("", "", null, null,null)
}
