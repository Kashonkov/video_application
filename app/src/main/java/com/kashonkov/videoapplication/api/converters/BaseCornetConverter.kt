package com.kashonkov.videoapplication.api.converters

import android.util.Log
import com.kashonkov.videoapplication.ui.main.successHttpStatus
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels

abstract class BaseCornetConverter<T> : BaseCornetCallback<T>() {
    private val TAG = BaseCornetConverter::class.simpleName
    private val BYTE_BUFFER_CAPACITY_BYTES = 64 * 1024

    private val bytesReceived = ByteArrayOutputStream()
    private val receiveChannel = Channels.newChannel(bytesReceived)

    abstract fun onBytesReceived(bytes: ByteArray)

    override fun onRedirectReceived(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        newLocationUrl: String?
    ) {
        Log.i(TAG, "$newLocationUrl")
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        info?.let {
            val responseStatus = info.httpStatusCode
            if (responseStatus == successHttpStatus) {
                request?.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES))
            }
        }
    }

    override fun onReadCompleted(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        byteBuffer: ByteBuffer?
    ) {
        byteBuffer?.flip()

        try {
            receiveChannel.write(byteBuffer)
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage ?: e.message ?: "")
        }

        byteBuffer?.clear()
        request?.read(byteBuffer)
    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
        onBytesReceived(bytesReceived.toByteArray())
        close()
        Log.i(TAG, "$info")
    }

    override fun onFailed(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        error: CronetException?
    ) {
        close()
        Log.i(TAG, "$error")
    }

    override fun onCanceled(request: UrlRequest?, info: UrlResponseInfo?) {
        close()
        super.onCanceled(request, info)
    }

    private fun close() {
        receiveChannel.close()
        bytesReceived.close()
    }
}