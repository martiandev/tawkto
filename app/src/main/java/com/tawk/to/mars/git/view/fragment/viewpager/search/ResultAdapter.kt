package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.util.Log
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
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import java.lang.ref.WeakReference

class ResultAdapter:RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    lateinit var nvm:NetworkViewModel
    lateinit var dvm:DatabaseViewModel
     var items:ArrayList<User> = ArrayList()

    constructor(items:List<User>,tt:TawkTo,activity:FragmentActivity)
    {
        nvm = ViewModelProvider(activity).get(NetworkViewModel::class.java)
        dvm = ViewModelProvider(activity).get(DatabaseViewModel::class.java)
        nvm.init(tt as TawkTo)
        dvm.init(tt as TawkTo)
        tt.appComponent.inject(this)

        this.items.addAll(items)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = items.get(position)
        holder.itemView.setOnClickListener {
            Log.i("DF","Initiating Search:"+user.id+" - "+user.login)

            dvm.getUser(user.id)
        }
        holder.tv_login.text = user.login
        var imageLoader = UserImageRequest(user.id!!,holder.iv_avatar.context,user.avatarUrl!!, WeakReference(holder.iv_avatar) )
        nvm.request(imageLoader)
        holder.iv_edit.visibility  = when(user.note==null)
        {
            true->View.GONE
            false->when(user.note!!.length>0)
            {
                true->View.VISIBLE
                false->View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
    
    fun update(update:List<User>)
    {
        for(u in update)
        {
            var isDuplicate = false
            var index = 0
            for(user in items)
            {
                if(u.id==user.id)
                {
                    isDuplicate = true
                    break
                }
                index++
            }
            if(isDuplicate)
            {
                items.set(index,u)
                notifyItemChanged(index)
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
        var iv_edit:ImageView = view.findViewById(R.id.iv_edit)
    }


}