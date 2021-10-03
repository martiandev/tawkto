package com.tawk.to.mars.git.model.network

import com.tawk.to.mars.git.model.network.request.Request
import com.tawk.to.mars.git.util.SingletonHolder
import com.tawk.to.mars.git.view.app.TawkTo

class QueueManager
{
    companion object : SingletonHolder<QueueManager, TawkTo>({
        QueueManager()
    })

    var queue:ArrayList<Request> = ArrayList()
    fun addToQueue(r: Request)
    {
        queue.add(r)
    }
    fun getNext(): Request?
    {
        if(queue.size>0)
        {
            var next = queue.get(0)
            queue.remove(next)
            return next
        }
        else
        {
            return null
        }

    }
}