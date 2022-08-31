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

package com.gcode.gmusiccomposesamples.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.gcode.gmusiccomposesamples.R
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.ui.components.MainActRV
import com.gcode.gmusiccomposesamples.ui.components.MainActSV
import com.gcode.gmusiccomposesamples.ui.components.MainActTopBar
import com.gcode.gmusiccomposesamples.ui.theme.MyTheme
import com.gcode.gmusiccomposesamples.ui.theme.bottom_layout_main_bg_color
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel
import com.gcode.vasttools.activity.VastComposeActivity
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : VastComposeActivity() {

    private val mViewModel:MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)

        setContent {
            MyTheme(darkTheme = false) {
                ProvideWindowInsets {
                    Surface {
                        rememberSystemUiController().setStatusBarColor(
                            Color.Transparent, darkIcons = MaterialTheme.colors.isLight)

                        var expanded by remember { mutableStateOf(false) }

                        Column {
                            Spacer(modifier = Modifier
                                .statusBarsHeight()
                                .fillMaxWidth().background(MaterialTheme.colors.primarySurface))

                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                scaffoldState = rememberScaffoldState(),
                                topBar = {
                                    val title = stringResource(id = R.string.app_name)

                                    MainActTopBar(
                                        title = { Text(title) },
                                        icon = {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_app),
                                                modifier = Modifier.size(40.dp),
                                                contentDescription = "App图标"
                                            )
                                        },
                                        more = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_search),
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .padding(5.dp)
                                                        .clickable {
                                                            expanded = !expanded
                                                            mViewModel.onExpandedChanged(expanded)
                                                        },
                                                    contentDescription = "搜索按钮"
                                                )
                                            }
                                        },
                                    )
                                },
                                content = {
                                    Column {
                                        MainActSV()
                                        MainActRV(Modifier.weight(1f))
                                        BottomControlLayout()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onDestroy() {
        super.onDestroy()
        mViewModel.stopMusic()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun BottomControlLayout() {

        val localMusicBean: LocalMusicBean by mViewModel.localMusicBean.observeAsState(
            LocalMusicBean()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(0.dp, 80.dp)
                .background(bottom_layout_main_bg_color),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 歌曲图标
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "歌曲图标",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            // 歌曲信息
            Column(Modifier.fillMaxWidth().weight(2f)) {
                Text(localMusicBean.song, style = TextStyle(color = Color.White))
                Text(localMusicBean.singer, style = TextStyle(color = Color.White))
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically, //设置垂直方向对齐
                horizontalArrangement = Arrangement.spacedBy(10.dp) //设置子项的间距
            ) {

                val isPlaying by mViewModel.isPlaying.observeAsState(false)

                // 上一首按钮
                Image(painter = painterResource(id = R.drawable.ic_last),
                    contentDescription = "上一首",
                    modifier = Modifier
                        .clickable { mViewModel.playLastMusic() }
                        .size(30.dp)
                )

                // 播放暂停按钮
                Image(painter = if (isPlaying) {
                    painterResource(id = R.drawable.ic_pause)
                } else {
                    painterResource(id = R.drawable.ic_play)
                },
                    contentDescription = "播放或者暂停",
                    modifier = Modifier
                        .clickable { mViewModel.playCurrentMusic() }
                        .size(40.dp)
                )

                // 下一首按钮
                Image(painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "下一首",
                    modifier = Modifier
                        .clickable { mViewModel.playNextMusic() }
                        .size(30.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun DefaultPreview() {
    MainActTopBar(
        title = { Text("GMusic") },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_app),
                modifier = Modifier.size(40.dp),
                contentDescription = "App图标"
            )
        },
        more = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search), modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp), contentDescription = "搜索按钮"
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_theme), modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp), contentDescription = "主题设置按钮"
                )
            }
        }
    )
}