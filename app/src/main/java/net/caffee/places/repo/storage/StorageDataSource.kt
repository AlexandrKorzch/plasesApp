package net.caffee.places.repo.storage

import okhttp3.ResponseBody


interface StorageDataSource {

    fun writeImageFileToDisk(body: ResponseBody, advantageId: String?): String

}