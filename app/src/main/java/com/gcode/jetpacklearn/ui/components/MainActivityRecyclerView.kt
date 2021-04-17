package com.gcode.jetpacklearn.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.viewModel.MainViewModel
import java.util.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainActivityRecyclerView(
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    viewModel: ViewModel) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        val targetSong by (viewModel as MainViewModel).targetSong.observeAsState(ArrayList<LocalMusicBean>())

        targetSong.forEach{
            LocalMusicRecycleItem(localMusicBean = it, viewModel = viewModel as MainViewModel)
        }
    }
}