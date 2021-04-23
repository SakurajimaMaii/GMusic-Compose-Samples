//<editor-fold desc="Description">
package com.gcode.jetpacklearn.viewModel

import android.annotation.SuppressLint
import android.content.ContentUris
import android.database.Cursor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gcode.jetpacklearn.model.LocalMusicBean
import com.gcode.jetpacklearn.utils.AppUtils
import com.gcode.jetpacklearn.utils.MsgUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
class MainViewModel : ViewModel() {
    private var mData: MutableList<LocalMusicBean> = ArrayList()

    //记录当前正在播放的音乐的位置
    private var currentPlayPosition = -1

    //记录暂停音乐时进度条的位置
    private var currentPausePositionInSong = 0

    //创建MediaPlayer对象
    private var mediaPlayer: MediaPlayer? = null

    //判断是不是首次启动 默认值为false
    private var isFirstRun = false

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying:LiveData<Boolean> = _isPlaying

    private val _localMusicBean =
        MutableLiveData(LocalMusicBean(null, null, null, null, null, null))

    val localMusicBean: LiveData<LocalMusicBean>
        get() = _localMusicBean

    private val _expanded = MutableLiveData(false)
    val expanded: LiveData<Boolean> = _expanded

    fun onExpandedChanged(expanded: Boolean) {
        _expanded.value = expanded
    }

    //搜索歌曲部分
    private val _targetSong = MutableLiveData<MutableList<LocalMusicBean>>()

    val targetSong: MutableLiveData<MutableList<LocalMusicBean>>
        get() = _targetSong

    fun searchSong(targetSong:String = ""){
        if (targetSong != ""){
            val searchResult:MutableList<LocalMusicBean> = ArrayList()
            for(bean in mData){
                if(bean.song?.contains(targetSong) == true){
                    searchResult.add(bean)
                }
            }
            _targetSong.postValue(searchResult)
        }else{
            _targetSong.postValue(mData)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadLocalMusicData() {
        //加载本地文件到集合中
        //获取ContentResolver对象
        val resolver = AppUtils.context.contentResolver
        //获取本地存储的Uri地址
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //开始查询地址
        val cursor: Cursor? = resolver.query(uri, null, null, null, null)
        //遍历cursor
        //设立条件过滤掉一部分歌曲
        when {
            cursor == null -> {
                Log.e(AppUtils.context.packageName, "query failed, handle error.")
            }
            !cursor.moveToFirst() -> {
                MsgUtils.showShortMsg(AppUtils.context, "设备上没有歌曲")
            }
            else -> {
                val sliderLastAdjusted = System.currentTimeMillis()
                var no = 0
                do {
                    if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)) != "<unknown>" &&
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) != "<unknown>"
                    ) {
                        no++
                        val song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                        val id: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                        val sdf = SimpleDateFormat("mm:ss")
                        val time = sdf.format(Date(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))))
                        val bean = LocalMusicBean(id, no, song, singer, album, time)
                        mData.add(bean)
                    }
                } while (cursor.moveToNext())
                val newTime = System.currentTimeMillis()
                Log.d("MainViewModel","${newTime-sliderLastAdjusted}")
            }
        }
        cursor?.close()
    }

    fun playLastMusic(){
        if(currentPlayPosition==0||currentPlayPosition==-1){
            MsgUtils.showShortMsg(AppUtils.context, "已经是第一首了，没有上一曲！")
        }else{
            //获取上一首歌曲
            val lastMusicBean = mData[currentPlayPosition-1]
            currentPlayPosition-=1
            playMusicInMusicBean(lastMusicBean)
        }
    }

    fun playCurrentMusic(){
        if (currentPlayPosition == -1) {
            MsgUtils.showShortMsg(AppUtils.context, "请选择想要播放的音乐")
        } else {
            if (mediaPlayer?.isPlaying == true) {
                //此时处于播放状态，需要暂停音乐
                pauseMusic()
            } else {
                //此时没有播放音乐，点击开始播放音乐
                playMusic()
            }
        }
    }

    fun playNextMusic(){
        if(currentPlayPosition==mData.size-1||currentPlayPosition==mData.size){
            MsgUtils.showShortMsg(AppUtils.context, "已经是最后一首了，没有下一曲！")
        }else{
            //获取上一首歌曲
            val nextMusicBean = mData[currentPlayPosition+1]
            currentPlayPosition+=1
            playMusicInMusicBean(nextMusicBean)
        }
    }

    fun playMusicInMusicBean(musicBean: LocalMusicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        stopMusic()
        //重置多媒体播放器
        mediaPlayer!!.reset()
        //设置新的播放路径
        try {
            mediaPlayer!!.apply {
                val contentUri: Uri? =
                    musicBean.id?.let {
                        ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            it
                        )
                    }
                setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                if (contentUri != null) {
                    setDataSource(AppUtils.context, contentUri)
                }
            }

            currentPlayPosition = musicBean.no?.minus(1) ?: -1

            playMusic()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        onMusicChanged(musicBean)
    }

    private fun playMusic() {
        //播放音乐的函数
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                //从暂停到播放
                mediaPlayer!!.seekTo(currentPausePositionInSong)
                mediaPlayer!!.start()
            }
            _isPlaying.postValue(true)
        }
    }

    private fun pauseMusic() {
        /* 暂停音乐的函数*/
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            currentPausePositionInSong = mediaPlayer!!.currentPosition
            mediaPlayer!!.pause()
            _isPlaying.postValue(false)
        }
    }

    fun stopMusic() {
        if (mediaPlayer != null && isFirstRun) {
            Log.d("MainActivity", "stopMusic()")
            currentPausePositionInSong = 0
            mediaPlayer!!.pause()
            mediaPlayer!!.seekTo(0)
            mediaPlayer!!.stop()
        } else {
            isFirstRun = true
        }
    }

    private fun onMusicChanged(localMusicBean: LocalMusicBean) {
        _localMusicBean.postValue(localMusicBean)
    }

    init {
        mediaPlayer = MediaPlayer()
        //加载音乐程序
        loadLocalMusicData()
        searchSong()
    }
}