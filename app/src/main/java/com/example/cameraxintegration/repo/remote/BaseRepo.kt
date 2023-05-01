package com.example.cameraxintegration.repo.remote

import android.util.Log
import com.example.cameraxintegration.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo() {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()
                Log.d("BaseRepo", "safeApiCall: ${response.errorBody()}")
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    Resource.Error(errorMessage = "Something went wrong")
                }
            } catch (e: HttpException) {
                Resource.Error(errorMessage = e.message ?: "Something went wrong")
            } catch (e: IOException) {
                Log.d("BaseRepo", "safeApiCall: $e")
                Resource.Error("Please check your network connection")
            } catch (e: Exception) {
                Log.d("BaseRepo", "safeApiCall: $e")
                Resource.Error(errorMessage = "Something went wrong")
            }
        }
    }
}