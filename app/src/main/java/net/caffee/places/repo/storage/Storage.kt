package net.caffee.places.repo.storage

import android.os.Environment
import okhttp3.ResponseBody
import java.io.*


object Storage : StorageDataSource {

    override fun writeImageFileToDisk(body: ResponseBody, advantageId: String?): String {

        try {
            val path = Environment.getExternalStorageDirectory()
            val futureStudioIconFile = File(path, "$advantageId.jpg")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
//                    Log.d("TAG", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                return futureStudioIconFile.absolutePath
            } catch (e: IOException) {
                return "not downloaded 1 ${e.message}"
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return "not downloaded 2"
        }
    }

}