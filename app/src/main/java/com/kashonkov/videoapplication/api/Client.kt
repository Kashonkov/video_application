package com.kashonkov.videoapplication.api

import android.content.Context
import org.chromium.net.CronetEngine
import org.chromium.net.CronetEngine.Builder.HTTP_CACHE_DISABLED

fun netClient(context: Context): CronetEngine {
    val cronetBuilder = CronetEngine.Builder(context)
    cronetBuilder.enableQuic(true)
        .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 0)
    return cronetBuilder.build()
}