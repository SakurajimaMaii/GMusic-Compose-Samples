package com.gcode.jetpacklearn.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.ui.theme.song_tv_color
import com.gcode.jetpacklearn.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LocalMusicRecycleItem(localMusicBean: LocalMusicBean, viewModel: MainViewModel) {

    ConstraintLayout(modifier = Modifier
        .clickable {
            viewModel.playMusicInMusicBean(localMusicBean)
        }
        .fillMaxWidth()
        .height(IntrinsicSize.Min)) {
        //可以理解为定义id
        val (song, singer, duration) = createRefs()

        localMusicBean.song?.let {
            Text(
                it,
                modifier = Modifier
                    .constrainAs(song) {
                        top.linkTo(parent.top)
                        absoluteLeft.linkTo(parent.start)
                    }
                    .padding(5.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 0.15.sp,
                    color = song_tv_color
                ),
                maxLines = 1
            )
        }

        localMusicBean.singer?.let {
            Text(
                it,
                modifier = Modifier
                    .constrainAs(singer) {
                        top.linkTo(song.bottom)
                        absoluteLeft.linkTo(song.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(5.dp),
                style = TextStyle(
                    color = song_tv_color
                )
            )
        }

        localMusicBean.duration?.let {
            Text(
                it,
                modifier = Modifier
                    .constrainAs(duration) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(5.dp),
                style = TextStyle(
                    color = song_tv_color
                )
            )
        }
    }
}