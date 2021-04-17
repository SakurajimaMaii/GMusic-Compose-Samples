package com.gcode.jetpacklearn.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.gcode.jetpacklearn.R
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.ui.components.MainActivityRecyclerView
import com.gcode.jetpacklearn.ui.components.MainActivitySearchView
import com.gcode.jetpacklearn.ui.components.MainActivityTopBar
import com.gcode.jetpacklearn.ui.theme.JetpackLearnTheme
import com.gcode.jetpacklearn.ui.theme.bottom_layout_main_bg_color
import com.gcode.jetpacklearn.utils.AppUtils
import com.gcode.jetpacklearn.utils.MsgUtils
import com.gcode.jetpacklearn.viewModel.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            JetpackLearnTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    var expanded by remember { mutableStateOf(false) }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        scaffoldState = rememberScaffoldState(),
                        topBar = {
                            val title = stringResource(id = R.string.app_name)

                            MainActivityTopBar(
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
                                        Image(painter = painterResource(id = R.drawable.ic_search),
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(5.dp)
                                                .clickable {
                                                    expanded = !expanded
                                                    viewModel.onExpandedChanged(expanded)
                                                },
                                            contentDescription = "搜索按钮"
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_theme),
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(5.dp),
                                            contentDescription = "主题设置按钮"
                                        )
                                    }
                                }
                            )
                        },
                        content = {
                            Column {
                                MainActivitySearchView(viewModel)
                                MainActivityRecyclerView(
                                    this@MainActivity,
                                    Modifier.weight(1f),
                                    viewModel = viewModel)
                                BottomControlLayout()
                            }
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopMusic()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun BottomControlLayout() {

        val localMusicBean: LocalMusicBean by viewModel.localMusicBean.observeAsState(
            LocalMusicBean(
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(0.dp, 80.dp)
                .background(bottom_layout_main_bg_color),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                localMusicBean.albumArt?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "歌曲图标",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
            Box(Modifier.weight(2f)) {
                Column(Modifier.fillMaxWidth()) {
                    localMusicBean.song?.let { Text(it,style = TextStyle(color = Color.White)) }
                    localMusicBean.singer?.let { Text(it,style = TextStyle(color = Color.White)) }
                }
            }
            Box(Modifier.weight(2f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val isPlaying by viewModel.isPlaying.observeAsState(false)

                    Image(painter = painterResource(id = R.drawable.ic_last),
                        contentDescription = "上一首",
                        modifier = Modifier
                            .clickable {
                                if (viewModel.getCurrentPlayPosition() == 0 || viewModel.getCurrentPlayPosition() == -1) {
                                    MsgUtils.showShortMsg(AppUtils.context, "已经是第一首了，没有上一曲！")
                                } else {
                                    viewModel.setCurrentPlayPosition(viewModel.getCurrentPlayPosition() - 1)
                                    val lastBean =
                                        viewModel.getMData()[viewModel.getCurrentPlayPosition()]
                                    viewModel.playMusicInMusicBean(lastBean)
                                }
                            }
                            .size(30.dp))

                    Image(painter = if(isPlaying){painterResource(id = R.drawable.ic_pause)}else{painterResource(id = R.drawable.ic_play)},
                        contentDescription = "播放或者暂停",
                        modifier = Modifier
                            .clickable {
                                if (viewModel.getCurrentPlayPosition() == -1) {
                                    MsgUtils.showShortMsg(AppUtils.context, "请选择想要播放的音乐")
                                } else {
                                    if (viewModel.getMediaPlayer()?.isPlaying == true) {
                                        //此时处于播放状态，需要暂停音乐
                                        viewModel.pauseMusic()
                                    } else {
                                        //此时没有播放音乐，点击开始播放音乐
                                        viewModel.playMusic()
                                    }
                                }
                            }
                            .size(60.dp)
                            .padding(10.dp))

                    Image(painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "下一首",
                        modifier = Modifier
                            .clickable {
                                Log.d(
                                    this@MainActivity.localClassName,
                                    "${viewModel.getCurrentPlayPosition()}"
                                )
                                if (viewModel.getCurrentPlayPosition() == viewModel.getMData().size - 1 || viewModel.getCurrentPlayPosition() == viewModel.getMData().size) {
                                    MsgUtils.showShortMsg(AppUtils.context, "已经是最后一首了，没有下一曲！")
                                } else {
                                    viewModel.setCurrentPlayPosition(viewModel.getCurrentPlayPosition() + 1)
                                    val lastBean =
                                        viewModel.getMData()[viewModel.getCurrentPlayPosition()]
                                    viewModel.playMusicInMusicBean(lastBean)
                                }
                            }
                            .size(30.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun DefaultPreview() {
    MainActivityTopBar(
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