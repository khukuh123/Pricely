package com.bangkit.pricely.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private val timeStamp: String = SimpleDateFormat(
        "yyyy-mm-HH",
        Locale.US
    ).format(System.currentTimeMillis())

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempImage(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun createCustomTempImage(context: Context): File {
        val path = File(context.cacheDir, "images").also { it.mkdirs() }
        return File.createTempFile(timeStamp, ".jpg", path)
    }
}