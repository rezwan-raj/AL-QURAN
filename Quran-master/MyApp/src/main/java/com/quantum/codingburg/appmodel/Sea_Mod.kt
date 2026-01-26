package com.quantum.codingburg.appmodel

data class Sea_Mod(
    val type: Int,
    val pos: Int,
    val name: String = "",
    val revelation: String = "",
    val verse: Int = 0,
    val nameAr: String = "",

    //
    val surah: Int = 0,
    val ayat: Int = 0,
    val indopak: String = "",
    val utsmani: String = "",
    val jalalayn: String = "",
    val latin: String = "",
    val terjemahan: String = "",
    val englishPro: String = "",
    val englishT: String = "",
    val trans: String = ""
)