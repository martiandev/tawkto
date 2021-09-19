package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.fragment.viewpager.ClickListener

class ResultAdapter:RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    lateinit var items:List<User>
    lateinit var cl:ClickListener

    constructor(items:List<User>,cl:ClickListener)
    {
        this.items = items
        this.cl = cl
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var iv_avatar:ImageView = view.findViewById(R.id.iv_avatar)
        var tv_login:TextView = view.findViewById(R.id.tv_login)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = items.get(position)
        holder.itemView.setOnClickListener {
            if(cl!=null)
            {
                cl.onSelected(user)
            }
        }
        holder.tv_login.text = user.login
        Glide.with(holder.iv_avatar.context)
            .load(user.avatarUrl)
            .placeholder(R.drawable.no)
            .into(holder.iv_avatar);
    }

    override fun getItemCount(): Int {
       return items.size
    }
}