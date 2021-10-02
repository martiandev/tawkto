package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.model.network.request.UserImageRequest.Listener
import com.tawk.to.mars.git.model.network.request.UserRequest
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import java.lang.ref.WeakReference

class ResultAdapter:RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    lateinit var nvm:NetworkViewModel
     var items:ArrayList<User> = ArrayList()
    lateinit var activity:FragmentActivity

    constructor(items:List<User>,tt:TawkTo,activity:FragmentActivity)
    {
        nvm = ViewModelProvider(activity).get(NetworkViewModel::class.java)
        nvm.init(tt as TawkTo)
        tt.appComponent.inject(this)

        this.items.addAll(items)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = items.get(position)
        holder.itemView.setOnClickListener {

            nvm.request(UserRequest(user!!.login!!))
        }
        holder.tv_login.text = user.login
        var imageLoader = UserImageRequest(user.id!!,holder.iv_avatar.context,user.avatarUrl!!, WeakReference(holder.iv_avatar) )
        nvm.request(imageLoader)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    fun add(additional:List<User>)
    {
        var lastItem = items.size
        items.addAll(additional)
        notifyItemInserted(lastItem)
    }
    fun update(update:List<User>)
    {
        for(u in update)
        {
            var isDuplicate = false
            var shouldReplace = false
            var index = 0
            for(user in items)
            {
                if(u.id==user.id)
                {
                    if(u.updated_at!!.after(user.updated_at))
                    {
                       shouldReplace = true
                    }
                    isDuplicate = true
                    break
                }
                index++
            }
            if(isDuplicate)
            {
                if(shouldReplace)
                {
                    items.set(index,u)
                }
            }
            else
            {
                items.add(u)
                notifyItemInserted(items.size)
            }
        }

    }

    fun getLastID():Int
    {
        if(items.size>0)
        {
            return items.get(itemCount-1).id
        }
        else
        {
            return 0
        }

    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var iv_avatar:ImageView = view.findViewById(R.id.iv_avatar)
        var tv_login:TextView = view.findViewById(R.id.tv_login)
    }


}