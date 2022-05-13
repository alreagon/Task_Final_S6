package com.example.task_final_s6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.task_final_s6.repository.UserRepository

class UserViewModelFactory constructor(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            UserViewModel(this.repository) as T
        }else{
            throw IllegalArgumentException("View model not found")
        }
    }

}