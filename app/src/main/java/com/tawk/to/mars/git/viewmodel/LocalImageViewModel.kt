package com.tawk.to.mars.git.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.*

class LocalImageViewModel() : ViewModel() {

    var request = MutableLiveData<UserImageRequest>()

    fun load(id: Int, context: Context, url: String, imageView: ImageView, position: Int) {
           CoroutineScope(Dispatchers.IO).async {
                if (url != null) {
                    var dir = File(context!!.filesDir, "/cache")
                    if (!dir!!.exists()) {
                        dir!!.mkdir()
                    } else {
                        if (!dir!!.isDirectory) {
                            dir!!.delete()
                            dir!!.mkdir()
                        }
                    }
                   var  result = File(context!!.filesDir, "/cache/" + id + ".png")
                    if (result!!.exists()) {
                        display(context, url, imageView, position,result)
                    } else {
                        request.postValue(UserImageRequest(id, context, url, imageView, position))
                    }
                }
            }

    }

    suspend fun display(context: Context, url: String, imageView: ImageView, position: Int,result:File) {
        try {
            if (imageView != null) {
                if ((position + 1) % 4 == 0 && position > 0) {
                    val opts = BitmapFactory.Options()
                    opts.inJustDecodeBounds = false
                    var bmp: Bitmap = BitmapFactory.decodeStream(
                        FileInputStream(result!!.absolutePath),
                        null,
                        opts
                    )!!
                    bmp = doInvert(bmp)!!
                    Handler(Looper.getMainLooper()).post {
                        Glide.with(context)
                            .load(bmp)
                            .placeholder(R.drawable.no)
                            .into(imageView!!)
                    }
                } else {

                    Handler(Looper.getMainLooper()).post {
                        Glide.with(context)
                            .load(result!!.absolutePath)
                            .placeholder(R.drawable.no)
                            .into(imageView!!)
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }


    suspend fun doInvert(src: Bitmap): Bitmap? {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int
        val height = src.height
        val width = src.width
        for (y in 0 until height) {
            for (x in 0 until width) {

                pixelColor = src.getPixel(x, y)
                A = Color.alpha(pixelColor)
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

    interface Listener {
        abstract fun onDone()
        abstract fun onFailed()
    }
}