package com.gcode.gmusiccomposesamples.ui.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.gcode.gmusiccomposesamples.R
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.ui.components.MainActRV
import com.gcode.gmusiccomposesamples.ui.components.MainActSV
import com.gcode.gmusiccomposesamples.ui.components.MainActTopBar
import com.gcode.gmusiccomposesamples.ui.theme.MyTheme
import com.gcode.gmusiccomposesamples.ui.theme.bottom_layout_main_bg_color
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel
import com.gcode.tools.utils.MsgWindowUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : FragmentActivity() {

    private lateinit var viewModel: MainViewModel

    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    MsgWindowUtils.showShortMsg(this, "所有权限已经授权,如果没有歌曲显示请重启应用")
                } else {
                    MsgWindowUtils.showShortMsg(this, "以下权限未被授予$deniedList")
                }
            }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            MyTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface {
                    var expanded by remember { mutableStateOf(false) }

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
                                MainActSV(viewModel)
                                MainActRV(Modifier.weight(1f), viewModel = viewModel)
                                BottomControlLayout()
                            }
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopMusic()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun BottomControlLayout() {

        val localMusicBean: LocalMusicBean by viewModel.localMusicBean.observeAsState(
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
            Box(Modifier.weight(2f)) {
                Column(Modifier.fillMaxWidth()) {
                    Text(localMusicBean.song, style = TextStyle(color = Color.White))
                    Text(localMusicBean.singer, style = TextStyle(color = Color.White))
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
                            .clickable { viewModel.playLastMusic() }
                            .size(30.dp)
                    )

                    Image(painter = if (isPlaying) {
                        painterResource(id = R.drawable.ic_pause)
                    } else {
                        painterResource(id = R.drawable.ic_play)
                    },
                        contentDescription = "播放或者暂停",
                        modifier = Modifier
                            .clickable { viewModel.playCurrentMusic() }
                            .size(60.dp)
                            .padding(10.dp)
                    )

                    Image(painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "下一首",
                        modifier = Modifier
                            .clickable { viewModel.playNextMusic() }
                            .size(30.dp)
                    )
                }
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