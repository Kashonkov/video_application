package com.kashonkov.videoapplication.api.converters

import com.kashonkov.videoapplication.api.parsers.MPDParser
import com.kashonkov.videoapplication.domain.entities.MPD

class MPDCronetConverter: BaseCornetConverter<MPD>() {
    override fun onSuccess(result: MPD) {
        val xml = String()
    }

    override fun onBytesReceived(bytes: ByteArray) {
        MPDParser().parse(bytes)
    }
}