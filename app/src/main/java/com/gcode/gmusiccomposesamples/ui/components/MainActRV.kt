package com.gcode.gmusiccomposesamples.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.gcode.gmusiccomposesamples.R
import com.gcode.gmusiccomposesamples.model.LocalMusicBean
import com.gcode.gmusiccomposesamples.viewModel.MainViewModel
import java.util.*

/**
 * 歌曲播放列表
 * @param modifier Modifier
 * @param viewModel ViewModel
 */
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainActRV(
    modifier: Modifier = Modifier,
    viewModel: ViewModel
) {
    ConstraintLayout(modifier = modifier) {

        Image(painter = painterResource(id = R.drawable.background), contentDescription = "背景",modifier = Modifier.fillMaxSize(),contentScale = ContentScale.FillBounds)

        val targetSong by (viewModel as MainViewModel).targetSong.observeAsState(ArrayList<LocalMusicBean>())
        
        LazyColumn {
            items(
                items = targetSong
            ){
                LocalMusicRVItem(localMusicBean = it, viewModel = viewModel as MainViewModel)
            }
        }
    }
}