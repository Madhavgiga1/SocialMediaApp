package com.example.socialmedialinked.viewmodels

import androidx.lifecycle.ViewModel
import com.example.socialmedialinked.models.User

class UserViewModel : ViewModel() {

    var currentUser: User?=null

    @JvmName("setCurrentUser1")
    fun setCurrentUser(user: User?) {
        if (user != null) {
            currentUser = user
        }
    }

    @JvmName("getCurrentUser1")
    fun getCurrentUser(): User? {
        return currentUser
    }


}
