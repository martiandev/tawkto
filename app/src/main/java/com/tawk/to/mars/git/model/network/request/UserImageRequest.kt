package com.tawk.to.mars.git.model.network.request

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import kotlinx.coroutines.*
import java.io.*
import java.lang.ref.WeakReference
import java.net.URL

class UserImageRequest(val id:Int, val context:Context, val url:String, val imageView:WeakReference<ImageView>,): Request()
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
            var urlSplit = url!!.split("/")
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

            Handler(Looper.getMainLooper()).post {
                if(imageView.get()!=null)
                {
                    Glide.with(context)
                        .load(result!!.absolutePath)
                        .placeholder(R.drawable.no)
                        .into(imageView.get()!!)
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
    interface Listener{
        abstract fun onDone()
        abstract fun onFailed()
    }
}