package com.gcode.gmusiccomposesamples.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.ui.theme.song_tv_color
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel

/**
 * 列表Item布局
 * @param localMusicBean LocalMusicBean
 * @param viewModel MainViewModel
 */
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LocalMusicRVItem(localMusicBean: LocalMusicBean, viewModel: MainViewModel) {

    Column(modifier = Modifier
        .clickable {
            viewModel.playMusicInMusicBean(localMusicBean)
        }
        .height(IntrinsicSize.Min)) {

        Text(
            localMusicBean.song,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 0.15.sp,
                color = song_tv_color
            ),
            maxLines = 1
        )

        Row{
            Text(
                localMusicBean.singer,
                modifier = Modifier
                    .padding(5.dp).wrapContentWidth(Alignment.Start).weight(1f),
                style = TextStyle(
                    color = song_tv_color
                )
            )

            Text(
                localMusicBean.duration,
                modifier = Modifier
                    .padding(5.dp).wrapContentWidth(Alignment.End).weight(1f),
                style = TextStyle(
                    color = song_tv_color
                )
            )
        }
    }
}