package com.kashonkov.videoapplication.domain.entities

data class MPD(
    val period: Period? = null,
    val xsi: String? = null,
    val xmlns: String? = null,
    val yt: String? = null,
    val schemaLocation: String? = null,
    val minBufferTime: String? = null,
    val profiles: String? = null,
    val type: String? = null,
    val mediaPresentationDuration: String? = null,
)

data class Period(
    val adaptations: List<Adaptation>?,
)

sealed class Adaptation()

data class AudioAdaptation(
    val id: String? = null,
    val role: Role? = null,
    val mimeType: String? = null,
    val subsegmentAlignment: Boolean? = null,
    val segment: AdaptationSegment? = null,
    val representations: List<AudioRepresentation>? = null,
) : Adaptation()

data class VideoAdaptation(
    val id: String? = null,
    val role: Role? = null,
    val mimeType: String? = null,
    val subsegmentAlignment: Boolean? = null,
    val segment: AdaptationSegment? = null,
    val representations: List<VideoRepresentation>? = null,
) : Adaptation()

data class Role(
    val schemeIdUri: String? = null,
    val value: String? = null,
)

data class AdaptationSegment(
    var segmentTimeline: AdaptationSegmentTimeline? = null,
    var startNumber: Int? = null,
    var timescale: Int? = null,
)

data class AdaptationSegmentTimeline(
    val points: List<AdaptationTimelinePoint>?,
)

data class AdaptationTimelinePoint(
    val point: Int?
)

data class AudioRepresentation(
    val id: String?,
    val codecs: String?,
    val audioSamplingRate: Int?,
    val startWithSAP: Int?,
    val bandwidth: Int?,
    val audioChannelConfiguration: AudioChannelConfiguration?,
    val baseURL: String?,
    val segment: RepresentationSegment?,
)

data class VideoRepresentation(
    val id: String?,
    val codecs: String?,
    val startWithSAP: Int?,
    val bandwidth: Int?,
    val width: Int?,
    val height: Int?,
    val maxPlayoutRate: Int?,
    val frameRate: Int?,
    val baseURL: String?,
    val segment: RepresentationSegment?,
)

data class AudioChannelConfiguration(
    val schemeIdUri: String?,
    val value: Int?
)

data class RepresentationSegment(
    var initialization: Initialization?,
    var segmentUrls: List<SegmentURL>?,
)

data class Initialization(
    var sourceURL: String?,
)

data class SegmentURL(
    var media: String?,
)