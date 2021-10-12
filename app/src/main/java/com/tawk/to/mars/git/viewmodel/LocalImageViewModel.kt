package com.tawk.to.mars.git.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.util.BitmapUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.*
import java.net.URL

//Viewmodel for accessing avatar from the local disk
class LocalImageViewModel() : ViewModel() {
    //======================================== Method ==============================================
    //Checks if the avatar is saved in the filesystem posts a UserImageRequest if no matching avatar is found
    fun load(id: Int, context: Context, url: String, imageView: ImageView, position: Int) {
           CoroutineScope(Dispatchers.IO).async {

                if (url != null) {
                    var dir = File(context!!.filesDir, "/cache")
                    if (!dir!!.exists()) {
                        dir!!.mkdir()
                    }
                    else {
                        if (!dir!!.isDirectory) {
                            dir!!.delete()
                            dir!!.mkdir()
                        }
                    }
                   var  result = File(context!!.filesDir, "/cache/" + id + ".png")
                    if (result!!.exists()) {
                        display(context, url, imageView, position,result)
                    } else {
                        download(result,url)
                        display(context, url, imageView, position,result)
                    }
                }
            }

    }
    //Downloads if no matching avatar is found
    suspend fun download(result:File,url:String)
    {
        try
        {
            val u = URL(url)
            val conn = u.openConnection()
            val contentLength = conn.contentLength
            val stream = DataInputStream(u.openStream())
            val buffer = ByteArray(contentLength)

            stream.readFully(buffer)
            stream.close()

            val fos = DataOutputStream(FileOutputStream(result))
            fos.write(buffer)
            fos.flush()
            fos.close()


        } catch (e: FileNotFoundException) {
            return

        } catch (e: IOException) {
            return

        }


    }

    //Displays the avatar from filesystem
    suspend fun display(context: Context, url: String, imageView: ImageView, position: Int,result:File) {
        try {
            if (imageView != null) {
                //Checks if the avatar should be inverted
                if ((position + 1) % 4 == 0 && position > 0) {
                    val opts = BitmapFactory.Options()
                    opts.inJustDecodeBounds = false
                    var bmp: Bitmap = BitmapFactory.decodeStream(
                        FileInputStream(result!!.absolutePath),
                        null,
                        opts
                    )!!
                    //Inverts Bitmap colors
                    bmp = BitmapUtil.doInvert(bmp)!!
                    Handler(Looper.getMainLooper()).post {
                        if (context != null && bmp != null && imageView != null)
                        {
                            if(!(context as FragmentActivity).isDestroyed)
                            {
                                Glide.with(context)
                                    .load(bmp)
                                    .placeholder(R.drawable.coding)
                                    .into(imageView!!)
                            }

                        }

                    }
                } else {

                    Handler(Looper.getMainLooper()).post {
                        if (context != null && result != null && imageView != null) {
                            if(!(context as FragmentActivity).isDestroyed)
                            {
                                Glide.with(context)
                                    .load(result!!.absolutePath)
                                    .placeholder(R.drawable.coding)
                                    .into(imageView!!)
                            }

                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
    //==============================================================================================

}