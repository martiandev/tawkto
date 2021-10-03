package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import com.tawk.to.mars.git.viewmodel.SelectionViewModel
import java.lang.ref.WeakReference

class ResultAdapter:RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    lateinit var networkViewModel:NetworkViewModel
    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var selectionViewModel: SelectionViewModel
     var items:ArrayList<User> = ArrayList()
    lateinit var activity:FragmentActivity



    constructor(items:List<User>,tt:TawkTo,activity:FragmentActivity)
    {
        tt.appComponent.inject(this)
        this.activity = activity
        networkViewModel = ViewModelProvider(activity).get(NetworkViewModel::class.java)
        selectionViewModel = ViewModelProvider(activity).get(SelectionViewModel::class.java)
        databaseViewModel = ViewModelProvider(activity).get(DatabaseViewModel::class.java)
        databaseViewModel.init(tt as TawkTo)
        networkViewModel.init(tt as TawkTo)
        this.items.addAll(items)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        databaseViewModel.savedNote.observe(activity, Observer {
            var ctr =0
            for(u in items)
            {
                if(u.id==it.id)
                {
                    items.set(ctr,it)
                    notifyItemChanged(ctr)
                    break
                }
                ctr++
            }
        })


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = items.get(position)
        holder.itemView.setOnClickListener {
            selectionViewModel.select(user)
        }

        holder.tv_login.text = user.login
        var imageLoader = UserImageRequest(user.id!!,holder.iv_avatar.context,user.avatarUrl!!, WeakReference(holder.iv_avatar),position)
        networkViewModel.request(imageLoader)
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