package com.gcode.jetpacklearn.model

import android.graphics.Bitmap

data class LocalMusicBean(
    val id: Long?,
    val no: Int?,
    val song: String?, //歌手名称
    val singer: String?, //专辑名称
    val album: String?, //歌曲时长
    val duration: String?, //歌曲路径
    val albumArt: Bitmap?
)