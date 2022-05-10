package com.kashonkov.videoapplication.api.parsers

import org.xmlpull.v1.XmlPullParser

fun XmlPullParser.readAttributes(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    for (i in 0 until this.attributeCount) {
        map[this.getAttributeName(i)] = this.getAttributeValue(i)
    }
    return map
}

inline fun XmlPullParser.untilTagOpened(func: ()-> Unit){
    while (this.next() != XmlPullParser.END_TAG){
        func()
    }
}

fun <T> Any?.cast(): T?{
    return this as? T
}

fun Any?.castInt(): Int?{
    return (this as? String)?.toInt()
}

fun Any?.castBoolean(): Boolean?{
    return (this as? String)?.toBooleanStrict()
}

fun Any?.castDouble(): Double?{
    return (this as? String)?.toDouble()
}