package com.gcode.jetpacklearn.ui.components

import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.gcode.jetpacklearn.R
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.utils.AppUtils
import com.gcode.jetpacklearn.viewModel.MainViewModel
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainActivityRecyclerView(
    modifier: Modifier = Modifier,
    viewModel: ViewModel
) {
    ConstraintLayout(modifier = modifier) {

        Image(painter = painterResource(id = R.drawable.background), contentDescription = "背景",modifier = Modifier.fillMaxSize(),contentScale = ContentScale.FillBounds)

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())){
            val targetSong by (viewModel as MainViewModel).targetSong.observeAsState(ArrayList<LocalMusicBean>())

            targetSong.forEach{
                LocalMusicRecycleItem(localMusicBean = it, viewModel = viewModel as MainViewModel)
            }
        }
    }
}