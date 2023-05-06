package com.example.cameraxintegration

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
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
        val loadedImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val bitmap = Bitmap.createScaledBitmap(loadedImage!!, 400, 300, true)

        val rotatedBitmap = bitmap.rotate(-90f)

        val compressBitmapPrev: Bitmap? = rotatedBitmap //compressImage1(bitmapPrev, 100.0)
        val out = ByteArrayOutputStream()
        compressBitmapPrev?.compress(Bitmap.CompressFormat.PNG, 0, out)
        val byteArray = out.toByteArray()
        val flags = android.util.Base64.NO_WRAP or android.util.Base64.DEFAULT
        return Base64.encodeToString(byteArray, flags)


    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}