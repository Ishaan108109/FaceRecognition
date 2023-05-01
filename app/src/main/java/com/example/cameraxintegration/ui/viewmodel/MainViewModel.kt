package com.example.cameraxintegration.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.SpoofingResponse
import com.example.cameraxintegration.model.UserImageEntity
import com.example.cameraxintegration.network.Resource
import com.example.cameraxintegration.repo.repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _insertUser = MutableLiveData<UserImageEntity>()
    val insertUser: LiveData<UserImageEntity> = _insertUser

    private val _checkSpoofing = MutableLiveData<Resource<SpoofingResponse>>()
    val checkSpoofing: LiveData<Resource<SpoofingResponse>> = _checkSpoofing
    fun insertUser(userImageEntity: UserImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUser(userImageEntity)
            _insertUser.postValue(userImageEntity)
        }
    }

    fun checkSpoofing(checkSpoofingBody: CheckSpoofingBody) = viewModelScope.launch {
        _checkSpoofing.postValue(Resource.Loading())
        _checkSpoofing.postValue(repo.checkSpoofingImage(checkSpoofingBody))
    }
}