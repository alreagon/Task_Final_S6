package com.example.task_final_s6.repository

import com.example.task_final_s6.model.RequestUser
import com.example.task_final_s6.network.ApiUserServices

//this repository class will do network call
class UserRepository constructor(private val apiUserServices: ApiUserServices){
    fun getAllUser() = apiUserServices.getAllUser()
    fun addDataUser(reqUser : RequestUser) = apiUserServices.addDataUser(reqUser)
    fun updateDataUser(id : String, reqUser : RequestUser) = apiUserServices.updateDataUser(id, reqUser)
}