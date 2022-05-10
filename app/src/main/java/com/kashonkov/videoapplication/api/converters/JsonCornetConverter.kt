package com.kashonkov.videoapplication.api.converters

import com.google.gson.GsonBuilder

abstract class JsonCornetConverter<T>(val classOfT: Class<T>): BaseCornetConverter<T>() {
    override fun onBytesReceived(bytes: ByteArray) {
        val gson = GsonBuilder().create()
        val model= gson.fromJson(String(bytes),classOfT)
        onSuccess(model)
    }
}