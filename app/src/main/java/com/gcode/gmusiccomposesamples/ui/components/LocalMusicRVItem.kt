/*
 * MIT License
 *
 * Copyright (c) 2022 VastGui
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.ui.theme.song_tv_color
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel

/**
 * 列表Item布局
 * @param localMusicBean
 * @param vm
 */
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LocalMusicRVItem(localMusicBean: LocalMusicBean, vm: MainViewModel = viewModel()) {

    Column(modifier = Modifier
        .clickable {
            vm.playMusicInMusicBean(localMusicBean)
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
                    .padding(5.dp)
                    .wrapContentWidth(Alignment.Start)
                    .weight(1f),
                style = TextStyle(
                    color = song_tv_color
                )
            )

            Text(
                localMusicBean.duration,
                modifier = Modifier
                    .padding(5.dp)
                    .wrapContentWidth(Alignment.End)
                    .weight(1f),
                style = TextStyle(
                    color = song_tv_color
                )
            )
        }
    }
}