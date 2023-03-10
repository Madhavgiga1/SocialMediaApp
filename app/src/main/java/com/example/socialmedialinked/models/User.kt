package com.example.socialmedialinked.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var displayName: String= "Xyz",
    val email: String? = "",
    var photoUrl: String? = "https://firebasestorage.googleapis.com/v0/b/linkedinclone-75a50.appspot.com/o/images%2Fhaircut.png?alt=media&token=54285c4a-fd73-4ea7-8c15-53f3c7383dc0",
    var motto: String? ="My first time using the app",
    var userid: String? =null,
    var userposts:MutableList<String>? = mutableListOf()
): Parcelable
