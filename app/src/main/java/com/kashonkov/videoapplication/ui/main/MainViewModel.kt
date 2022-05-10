package com.kashonkov.videoapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashonkov.videoapplication.VideoApplication
import com.kashonkov.videoapplication.api.converters.MPDCronetConverter
import com.kashonkov.videoapplication.api.netClient
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


const val successHttpStatus = 200

class MainViewModel : ViewModel() {
    fun testLoad() {
        val cur = Thread.currentThread()
        viewModelScope.launch {
            val client = netClient(VideoApplication.appContext)
            val testRequest = client.newUrlRequestBuilder(
                "https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0",
                jsonArrayCronetCallback,
                Executors.newSingleThreadExecutor()
            ).build()
            testRequest.start();
        }
    }

    private val jsonArrayCronetCallback = MPDCronetConverter()
}