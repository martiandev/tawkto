package com.tawk.to.mars.git.model.network

import android.util.Log
import com.tawk.to.mars.git.model.network.request.Request
import com.tawk.to.mars.git.util.SingletonHolder
import com.tawk.to.mars.git.view.app.TawkTo

class QueueManager
{
    init {
    }
    companion object : SingletonHolder<QueueManager, TawkTo>({
        QueueManager()
    })

    var queue:ArrayList<Request> = ArrayList()
    fun addToQueue(r: Request)
    {
        queue.add(r)
    }
    fun getNext(): Request
    {
        var next = queue.get(0)
        queue.remove(next)
        Log.i("QUEUE","NEXT -- "+queue.size)
        return next
    }
}