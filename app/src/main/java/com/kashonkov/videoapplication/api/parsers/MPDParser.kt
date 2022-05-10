package com.kashonkov.videoapplication.api.parsers

import android.util.Log
import android.util.Xml
import com.kashonkov.videoapplication.domain.entities.*
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream

/**
 * Common attrs
 */
private const val ID = "id"
private const val VALUE = "value"
private const val SCHEME_ID_URI = "schemeIdUri"

/**
 * MPD attrs
 */
private const val MPD_TAG = "MPD"
private const val XSI = "xmlns:xsi"
private const val XMLNS = "xmlns"
private const val YT = "xmlns:yt"
private const val SCHEMA_LOCATION = "xsi:schemaLocation"
private const val MIN_BUFFER_TIME = "minBufferTime"
private const val PROFILES = "profiles"
private const val TYPE = "type"
private const val MEDIA_PRESENTATION_DURATION = "mediaPresentationDuration"

/**
 * Period attrs
 */
private const val PERIOD_TAG = "Period"

/**
 * Adaptation attrs
 */
private const val ADAPTATION_TAG = "AdaptationSet"
private const val MIME_TYPE = "mimeType"
private const val SUBSEGMENT_ALIGNMENT = "subsegmentAlignment"
private const val AUDIO = "audio"
private const val VIDEO = "video"

/**
 * Role attrs
 */
private const val ROLE_TAG = "Role"

/**
 * Segment attrs
 */
private const val SEGMENT_TAG = "SegmentList"
private const val START_NUMBER = "startNumber"
private const val TIMESCALE = "timescale"

/**
 * Timeline attrs
 */
private const val SEGMENT_TIMELINE_TAG = "SegmentTimeline"

/**
 * S attrs
 */
private const val S_TAG = "S"
private const val D = "d"

/**
 * Representation attrs
 */
private const val REPRESENTATION_TAG = "Representation"
private const val AUDIO_SAMPLING_RATE = "audioSamplingRate"
private const val START_WITH_SAP = "startWithSAP"
private const val BANDWITH = "bandwidth"
private const val CODECS = "codecs"
private const val WIDTH = "width"
private const val HEIGHT = "height"
private const val MAX_PLAYOUT_RATE = "maxPlayoutRate"
private const val FRAME_RATE = "frameRate"

/**
 * AudioChannelConfiguration attrs
 */
private const val AUDIO_CHANNEL_CONFIGURATION_TAG = "AudioChannelConfiguration"

/**
 * BaseUrl attrs
 */
private const val BASE_URL_TAG = "BaseURL"

/**
 * Initialization attrs
 */
private const val INITIALIZATION_TAG = "Initialization"
private const val SOURCE_URL = "sourceURL"

/**
 * Initialization attrs
 */
private const val SEGMENT_URL_TAG = "SegmentURL"
private const val MEDIA = "media"

class MPDParser {
    val TAG = MPDParser::class.simpleName
    fun parse(bytes: ByteArray): MPD {
        ByteArrayInputStream(bytes).use {
            val parser = Xml.newPullParser().apply {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(it, null)
            }
            parser.next()
            readMPD(parser)
        }
        return MPD()
    }

    private fun readMPD(parser: XmlPullParser): MPD? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, MPD_TAG)
        val attributes = parser.readAttributes()
        var period: Period? = null

        parser.nextTag()

        when (parser.name) {
            PERIOD_TAG -> period = readPeriod(parser)
        }

        MPD(
            xsi = attributes[XSI].cast(),
            xmlns = attributes[XMLNS].cast(),
            yt = attributes[YT].cast(),
            schemaLocation = attributes[SCHEMA_LOCATION].cast(),
            minBufferTime = attributes[MIN_BUFFER_TIME].cast(),
            profiles = attributes[PROFILES].cast(),
            type = attributes[TYPE].cast(),
            period = period,
            mediaPresentationDuration = attributes[MEDIA_PRESENTATION_DURATION].cast()
        )
    }

    private fun readPeriod(parser: XmlPullParser): Period? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, PERIOD_TAG)
        val adaptations = mutableListOf<Adaptation>()
        while (parser.next() != XmlPullParser.END_TAG) {
            when (parser.name) {
                ADAPTATION_TAG -> {
                    readAdaptation(parser)?.let { adaptations.add(it) }
                }
            }
        }

        Period(adaptations = adaptations)
    }

    private fun readAdaptation(parser: XmlPullParser): Adaptation? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, ADAPTATION_TAG)
        val attributes = parser.readAttributes()
        val type: String = attributes[MIME_TYPE].cast()
            ?: throw UnsupportedOperationException("$TAG Empty mime type of adaptation")

        when {
            type.contains(AUDIO, true) -> readAudioAdaptation(parser)
            type.contains(VIDEO, true) -> readVideoAdaptation(parser)
            else -> {
                throw UnsupportedOperationException("$TAG Unknown mime type $type")
            }
        }
    }

    private fun readAudioAdaptation(parser: XmlPullParser): AudioAdaptation? =
        readOrNull {
            val attributes = parser.readAttributes()
            var role: Role? = null
            var segment: AdaptationSegment? = null
            val representations = mutableListOf<AudioRepresentation>()
            parser.untilTagOpened {
                when (parser.name) {
                    ROLE_TAG -> role = readRole(parser)
                    SEGMENT_TAG -> segment = readAdaptationSegment(parser)
                    REPRESENTATION_TAG -> {
                        readAudioRepresentation(parser)?.let { representations.add(it) }
                    }
                }
            }
            AudioAdaptation(
                id = attributes[ID].cast(),
                mimeType = attributes[MIME_TYPE].cast(),
                subsegmentAlignment = attributes[SUBSEGMENT_ALIGNMENT].castBoolean(),
                role = role,
                segment = segment,
                representations = representations
            )
        }

    private fun readVideoAdaptation(parser: XmlPullParser): VideoAdaptation? =
        readOrNull {
            val attributes = parser.readAttributes()
            var role: Role? = null
            var segment: AdaptationSegment? = null
            val representations = mutableListOf<VideoRepresentation>()
            parser.untilTagOpened {
                when (parser.name) {
                    ROLE_TAG -> role = readRole(parser)
                    SEGMENT_TAG -> segment = readAdaptationSegment(parser)
                    REPRESENTATION_TAG -> {
                        readVideoRepresentation(parser)?.let { representations.add(it) }
                    }
                }
            }
            VideoAdaptation(
                id = attributes[ID].cast(),
                mimeType = attributes[MIME_TYPE].cast(),
                subsegmentAlignment = attributes[SUBSEGMENT_ALIGNMENT].castBoolean(),
                role = role,
                segment = segment,
                representations = representations
            )
        }

    private fun readRole(parser: XmlPullParser): Role? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, ROLE_TAG)
        val attrs = parser.readAttributes()
        parser.next()
        Role(
            schemeIdUri = attrs[SCHEME_ID_URI].cast(),
            value = attrs[VALUE].cast()
        )
    }

    private fun readAdaptationSegment(parser: XmlPullParser): AdaptationSegment? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, SEGMENT_TAG)
        val attrs = parser.readAttributes()
        var segmentTimeline: AdaptationSegmentTimeline? = null
        parser.untilTagOpened {
            when (parser.name) {
                SEGMENT_TIMELINE_TAG -> segmentTimeline = readAdaptationSegmentTimeline(parser)
            }
        }
        AdaptationSegment(
            timescale = attrs[TIMESCALE].castInt(),
            startNumber = attrs[START_NUMBER].castInt(),
            segmentTimeline = segmentTimeline,
        )
    }

    private fun readAdaptationSegmentTimeline(parser: XmlPullParser): AdaptationSegmentTimeline? =
        readOrNull {
            parser.require(XmlPullParser.START_TAG, null, SEGMENT_TIMELINE_TAG)
            val segmentList = mutableListOf<AdaptationTimelinePoint>()
            parser.untilTagOpened {
                when (parser.name) {
                    S_TAG -> {
                        readTimeLinePoint(parser)?.let { segmentList.add(it) }
                    }
                }
            }
            AdaptationSegmentTimeline(segmentList)
        }

    private fun readTimeLinePoint(parser: XmlPullParser): AdaptationTimelinePoint? =
        readOrNull {
            parser.require(XmlPullParser.START_TAG, null, S_TAG)
            val attributes = parser.readAttributes()
            parser.next()
            AdaptationTimelinePoint(
                attributes[D].castInt()
            )
        }

    private fun readAudioRepresentation(parser: XmlPullParser): AudioRepresentation? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, REPRESENTATION_TAG)
        val attrs = parser.readAttributes()
        var audioConfiguration: AudioChannelConfiguration? = null
        var baseUrl: String? = null
        var segment: RepresentationSegment? = null

        parser.untilTagOpened {
            when (parser.name) {
                AUDIO_CHANNEL_CONFIGURATION_TAG -> audioConfiguration =
                    readAudioChannelConfiguration(parser)
                BASE_URL_TAG -> baseUrl = readBaseUrl(parser)
                SEGMENT_TAG -> segment = readRepresentationSegment(parser)
            }
        }

        AudioRepresentation(
            id = attrs[ID].cast(),
            codecs = attrs[CODECS].cast(),
            audioSamplingRate = attrs[AUDIO_SAMPLING_RATE].castInt(),
            startWithSAP = attrs[START_WITH_SAP].castInt(),
            bandwidth = attrs[BANDWITH].castInt(),
            audioChannelConfiguration = audioConfiguration,
            baseURL = baseUrl,
            segment = segment,
        )
    }

    private fun readVideoRepresentation(parser: XmlPullParser): VideoRepresentation? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, REPRESENTATION_TAG)
        val attrs = parser.readAttributes()
        var baseUrl: String? = null
        var segment: RepresentationSegment? = null

        parser.untilTagOpened {
            when (parser.name) {
                BASE_URL_TAG -> baseUrl = readBaseUrl(parser)
                SEGMENT_TAG -> segment = readRepresentationSegment(parser)
            }
        }

        VideoRepresentation(
            id = attrs[ID].cast(),
            codecs = attrs[CODECS].cast(),
            startWithSAP = attrs[START_WITH_SAP].castInt(),
            bandwidth = attrs[BANDWITH].castInt(),
            width = attrs[WIDTH].castInt(),
            height = attrs[HEIGHT].castInt(),
            maxPlayoutRate = attrs[MAX_PLAYOUT_RATE].castInt(),
            frameRate = attrs[FRAME_RATE].castInt(),
            baseURL = baseUrl,
            segment = segment,
        )
    }

    private fun readAudioChannelConfiguration(parser: XmlPullParser): AudioChannelConfiguration? =
        readOrNull {
            parser.require(XmlPullParser.START_TAG, null, AUDIO_CHANNEL_CONFIGURATION_TAG)
            val attrs = parser.readAttributes()
            parser.next()
            AudioChannelConfiguration(
                schemeIdUri = attrs[SCHEME_ID_URI].cast(),
                value = attrs[VALUE].castInt(),
            )
        }

    private fun readBaseUrl(parser: XmlPullParser): String? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, BASE_URL_TAG)
        val url = parser.nextText()
        url
    }

    private fun readRepresentationSegment(parser: XmlPullParser): RepresentationSegment? =
        readOrNull {
            parser.require(XmlPullParser.START_TAG, null, SEGMENT_TAG)
            var initialization: Initialization? = null
            val segments = mutableListOf<SegmentURL>()
            parser.untilTagOpened {
                when (parser.name) {
                    INITIALIZATION_TAG -> initialization = readInitialization(parser)
                    SEGMENT_URL_TAG -> readSegmentUrl(parser)?.let { segments.add(it) }
                }
            }
            RepresentationSegment(
                initialization = initialization,
                segmentUrls = segments,
            )
        }

    private fun readInitialization(parser: XmlPullParser): Initialization? = readOrNull {
        parser.require(XmlPullParser.START_TAG, null, INITIALIZATION_TAG)
        val attrs = parser.readAttributes()
        parser.next()
        Initialization(
            sourceURL = attrs[SOURCE_URL].cast()
        )
    }

    private fun readSegmentUrl(parser: XmlPullParser): SegmentURL? =
        readOrNull {
            parser.require(XmlPullParser.START_TAG, null, SEGMENT_URL_TAG)
            val attributes = parser.readAttributes()
            parser.next()
            SegmentURL(
                attributes[MEDIA].cast()
            )
        }

    private inline fun <T> readOrNull(func: () -> T): T? {
        return try {
            func()
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
            null

        }
    }
}