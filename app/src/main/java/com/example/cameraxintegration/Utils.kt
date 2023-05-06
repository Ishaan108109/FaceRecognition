package com.example.cameraxintegration

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

object Utils {
    fun convertUriToBase64(context: Context, uri: Uri): String? {
        var inputStream: java.io.InputStream? = null
        try {
            val scheme = uri.scheme
            if (scheme == "file") {
                val file = File(uri.path)
                inputStream = FileInputStream(file)
            } else {
                val contentResolver: ContentResolver = context.contentResolver
                inputStream = contentResolver.openInputStream(uri)
            }
            if (inputStream == null) {
                return null
            }
            val buffer = ByteArray(8192)
            val output = ByteArrayOutputStream()
            var bytesRead = inputStream.read(buffer)
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }
            val imageBytes = output.toByteArray()
            val base64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            return base64
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            inputStream?.close()
        }
    }
}