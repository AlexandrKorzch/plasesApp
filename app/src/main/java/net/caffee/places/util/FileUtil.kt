package net.caffee.places.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


fun createFileBody(path: String): MultipartBody.Part {
    val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), File(path))
    val body = MultipartBody.Part.createFormData("file", "file.jpeg", reqFile)
    return body
}