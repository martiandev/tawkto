package com.tawk.to.mars.git.model.network.request

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import kotlinx.coroutines.*
import java.io.*
import java.lang.ref.WeakReference
import java.net.URL

class UserImageRequest(val id:Int, val context:Context, val url:String, val imageView:ImageView,val position:Int): Request()
{
    var dir:File ? = null
    var result:File ? = null
    var listener:Listener ? = null

    fun start(listener:Listener)
    {
        this.listener = listener
        CoroutineScope(Dispatchers.IO).async {
            download()
        }
    }

    suspend fun download()
    {

        if(url!=null)
        {

            this.dir=File(context!!.filesDir, "/cache")
            if(!this.dir!!.exists())
            {
                this.dir!!.mkdir()
            }
            else
            {
                if(!this.dir!!.isDirectory)
                {
                    this.dir!!.delete()
                    this.dir!!.mkdir()
                }
            }
            result = File(context!!.filesDir, "/cache/"+id+".png")
            if(result!!.exists())
            {
                display()
            }
            else
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

                    display()

                } catch (e: FileNotFoundException) {
                    listener!!.onFailed()
                    return

                } catch (e: IOException) {
                    listener!!.onFailed()
                    return

                }
            }
        }
    }

   suspend fun display()
    {
        try {



                if(imageView!=null)
                {
                    if((position+1)%4==0&&position>0)
                    {
                        val opts = BitmapFactory.Options()
                        opts.inJustDecodeBounds = false
                        var bmp:Bitmap = BitmapFactory.decodeStream(FileInputStream(result!!.absolutePath), null, opts)!!
                        bmp = doInvert(bmp)!!
                        Handler(Looper.getMainLooper()).post {
                            Glide.with(context)
                                .load(bmp)
                                .placeholder(R.drawable.no)
                                .into(imageView!!)
                        }
                    }
                    else
                    {

                        Handler(Looper.getMainLooper()).post {
                            Glide.with(context)
                                .load(result!!.absolutePath)
                                .placeholder(R.drawable.no)
                                .into(imageView!!)
                        }
                    }

                }


            listener!!.onDone()

        }
        catch (e:Exception)
        {
            e.printStackTrace()
            listener!!.onFailed()

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

        // return final bitmap
        return bmOut
    }

    interface Listener{
        abstract fun onDone()
        abstract fun onFailed()
    }
}