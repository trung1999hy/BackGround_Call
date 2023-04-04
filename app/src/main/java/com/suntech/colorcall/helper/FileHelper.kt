package com.suntech.colorcall.helper

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FileHelper @Inject constructor(val context: Context) {

    companion object {
        const val VIDEO_OK = 1
        const val IMAGE_OK = 2
    }

    private val imageDirection = File("${context.filesDir}/image/")
    private val videoDirection = File("${context.filesDir}/video/")

    private suspend fun downloadVideo(url: String, filePath: File = videoDirection): Int {
        if (File(filePath.toString(), nameFromURL(url)).exists()) {
            Timber.d("Video đã được tải xuống")
            return VIDEO_OK
        }
        return suspendCoroutine { result ->
            PRDownloader.download(url, filePath.toString(), nameFromURL(url)).build()
                .setOnProgressListener {
                    Timber.d("${it.currentBytes} / ${it.totalBytes}")
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        result.resume(VIDEO_OK)
                    }

                    override fun onError(error: Error?) {
                        error?.connectionException?.let { result.resumeWithException(it) }
                        PRDownloader.cleanUp(1)
                    }
                })
        }
    }

    private suspend fun downloadImage(url: String, filePath: File = imageDirection): Int {
        if (File(filePath.toString(), nameFromURL(url)).exists()) {
            Timber.d("Ảnh đã được tải xuống")
            return IMAGE_OK
        }
        return suspendCoroutine { result ->
            PRDownloader.download(url, filePath.toString(), nameFromURL(url)).build()
                .setOnProgressListener {
                    Timber.d("${it.currentBytes} / ${it.totalBytes}")
                }
                  .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        result.resume(IMAGE_OK)
                    }

                    override fun onError(error: Error?) {
                        error?.connectionException?.let { result.resumeWithException(it) }
                        PRDownloader.cleanUp(1)
                    }
                })
        }
    }

    fun urlVideoToFile(url: String): File {
        return File(videoDirection, nameFromURL(url))
    }

    fun urlImageToFile(url: String): File {
        return File(imageDirection, nameFromURL(url))
    }

    private fun nameFromURL(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

    fun save(imageUrl: String, videoUrl: String, onSuccess: (number: Int) -> Unit, onFailure: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val videoResult = async { downloadVideo(videoUrl) }
            val imageResult = async { downloadImage(imageUrl) }
            if (videoResult.await() + imageResult.await() == 0)
                withContext(Dispatchers.Main) { onFailure }
            else
                withContext(Dispatchers.Main) { onSuccess(videoResult.await() + imageResult.await()) }
        }
    }
}

