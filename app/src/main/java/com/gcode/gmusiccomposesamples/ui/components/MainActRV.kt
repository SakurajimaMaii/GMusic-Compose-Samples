package com.gcode.gmusiccomposesamples.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gcode.gmusiccomposesamples.R
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel
import java.util.*

/**
 * 歌曲播放列表
 * @param modifier
 * @param vm
 */
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainActRV(
    modifier: Modifier = Modifier,
    vm: MainViewModel = viewModel()
) {
    val targetSong by vm.targetSong.observeAsState(ArrayList<LocalMusicBean>())

    LazyColumn(modifier = modifier.background(Color(0xFFdcdde1))) {
        items(
            items = targetSong
        ){
            LocalMusicRVItem(localMusicBean = it)
        }
    }
}