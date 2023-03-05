package com.example.socialmedialinked.viewmodels

import androidx.lifecycle.ViewModel
import com.example.socialmedialinked.models.User

class UserViewModel : ViewModel() {

    var currentUser: User? = null

    @JvmName("setCurrentUser1")
    fun setCurrentUser(user: User) {
        currentUser = user
    }

    @JvmName("getCurrentUser1")
    fun getCurrentUser(): User? {
        return currentUser
    }

    companion object {
        private var instance: UserViewModel? = null

        fun getInstance(): UserViewModel {
            if (instance == null) {
                instance = UserViewModel()
            }
            return instance!!
        }
    }
}
