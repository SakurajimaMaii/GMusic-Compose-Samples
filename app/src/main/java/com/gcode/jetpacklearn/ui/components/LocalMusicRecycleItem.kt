package com.gcode.jetpacklearn.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LocalMusicRecycleItem(localMusicBean: LocalMusicBean,viewModel: MainViewModel) {

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min), elevation = 5.dp, shape = RoundedCornerShape(10.dp)
    ) {
        ConstraintLayout(modifier = Modifier.clickable {
            viewModel.playMusicInMusicBean(localMusicBean)
        }) {
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
                        letterSpacing = 0.15.sp
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
                        .padding(5.dp)
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
                        .padding(5.dp)
                )
            }
        }
    }
}