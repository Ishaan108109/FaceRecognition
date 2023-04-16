package com.example.cameraxintegration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxintegration.repo.local.repo
import com.example.cameraxintegration.repo.model.UserImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _insertUser = MutableLiveData<UserImageEntity>()
    val insertUser: LiveData<UserImageEntity> = _insertUser
    fun insertUser(userImageEntity: UserImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUser(userImageEntity)
            _insertUser.postValue(userImageEntity)
        }
    }
}