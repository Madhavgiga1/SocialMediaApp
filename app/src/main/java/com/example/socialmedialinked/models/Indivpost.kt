package com.example.socialmedialinked.models

data class Indivpost(
    val post_author_name:String,
    val post_timestamp:String,
    val post_text:String?=null,
    var post_image: String?=null,
    val postlikes:Int=0
) {

    constructor() : this("", "", null, null, 0)
}
