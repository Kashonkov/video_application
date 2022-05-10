package com.kashonkov.videoapplication.api.converters

import org.chromium.net.UrlRequest

abstract class BaseCornetCallback<T>: UrlRequest.Callback()  {
    abstract fun onSuccess(result: T)
}