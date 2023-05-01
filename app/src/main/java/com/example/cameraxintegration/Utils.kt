package com.example.cameraxintegration

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream

object Utils {
    fun uriToBase64(context: Context, uri: Uri?): String {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}