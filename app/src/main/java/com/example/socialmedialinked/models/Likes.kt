package com.example.socialmedialinked.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Likes(
    var likeCount:Int = 0,
    var likedby: List<String?> = emptyList()
): Parcelable {
    constructor() : this(0, emptyList())
}
