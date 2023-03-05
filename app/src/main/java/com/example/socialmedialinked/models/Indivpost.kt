package com.example.socialmedialinked.models
import android.net.Uri

data class Indivpost(
    val post_author_name:String,
    val post_timestamp:String,
    val post_text:String?=null,
    var post_image: String?=null,
    var post_video: Uri?=null,
    val postlikes:Int=0
) {
    // Public no-argument constructor required by Firebase
    constructor() : this("", "", null, null, null)
}
