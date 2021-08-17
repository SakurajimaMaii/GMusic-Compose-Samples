package com.gcode.gmusiccomposesamples.model

/**
 * 歌曲实例
 * @property id Long
 * @property no Int
 * @property song String
 * @property singer String
 * @property album String
 * @property duration String
 * @constructor
 */
data class LocalMusicBean @JvmOverloads constructor(
    val id: Long = 0L,
    val no: Int = 0,
    val song: String = "", //歌手名称
    val singer: String = "", //专辑名称
    val album: String = "", //歌曲时长
    val duration: String = "", //歌曲路径
)