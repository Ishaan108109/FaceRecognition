package com.example.cameraxintegration.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.CheckSpoofingResponse
import com.example.cameraxintegration.model.UserImageEntity
import com.example.cameraxintegration.network.Client
import com.example.cameraxintegration.network.Resource
import com.example.cameraxintegration.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private val repo by lazy {
        Repo(Client.api)
    }
    private val _insertUser = MutableLiveData<UserImageEntity>()
    val insertUser: LiveData<UserImageEntity> = _insertUser

    private val _spoofingImage = MutableLiveData<Resource<CheckSpoofingResponse>>()
    val spoofingImage: LiveData<Resource<CheckSpoofingResponse>> = _spoofingImage

    fun insertUser(userImageEntity: UserImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUser(userImageEntity)
            _insertUser.postValue(userImageEntity)
        }
    }

    fun isImageSpoofed(checkSpoofingBody: CheckSpoofingBody) = viewModelScope.launch {
        _spoofingImage.postValue(Resource.Loading())
        _spoofingImage.postValue(repo.isImageSpoofed(checkSpoofingBody))
    }


}