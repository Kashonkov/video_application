package com.kashonkov.videoapplication.domain.entities

data class CountryEntity(
    val name: CountryName?,
    val tld: List<String>?,
    val cca2: String?,
    val ccn3: String?,
    val cca3: String?,
    val cioc: String?,
    val independent: Boolean?,
    val status: String?,
    val unMember: Boolean?,
    val idd: PhoneData?,
    val capital: List<String>?,
    val altSpellings: List<String>?,
    val region: String?,
    val subregion: String?,
    val latlng: List<Double>?,
    val landlocked: Boolean?,
    val area: Double?,
    val flag: String?,
    val flags: Map<String, String>?,
    )

data class CountryName(
    val common: String?,
    val official: String?,
    val nativeName: NativeName?
)

data class NativeName(
    val official: String?,
    val common: String?,
)

data class PhoneData(
    val root: String?,
    val suffixes: List<String>?,
)