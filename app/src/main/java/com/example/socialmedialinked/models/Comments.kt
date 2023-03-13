package com.example.socialmedialinked.models

data class Comments(
    var user: User?,
    var text: String
){
    constructor() : this(null,"")

}