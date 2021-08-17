package com.gcode.jetpacklearn.utils

import android.content.Context
import android.widget.Toast

object MsgUtils {
    fun showShortMsg(context:Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}