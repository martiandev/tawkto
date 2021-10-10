package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tawk.to.mars.git.databinding.ItemUserBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.LocalImageViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import com.tawk.to.mars.git.viewmodel.SelectionViewModel

class ResultAdapter:RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    lateinit var networkViewModel:NetworkViewModel
    lateinit var localImageViewModel: LocalImageViewModel
    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var selectionViewModel: SelectionViewModel
    lateinit var activity:FragmentActivity
    var items:ArrayList<User> = ArrayList()


    constructor(items:List<User>,tt:TawkTo,activity:FragmentActivity)
    {
        tt.appComponent.inject(this)
        this.activity = activity
        networkViewModel = ViewModelProvider(activity).get(NetworkViewModel::class.java)
        selectionViewModel = ViewModelProvider(activity).get(SelectionViewModel::class.java)
        databaseViewModel = ViewModelProvider(activity).get(DatabaseViewModel::class.java)
        localImageViewModel = ViewModelProvider(activity).get(LocalImageViewModel::class.java)
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
        return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false),selectionViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = items.get(position)
        holder.bind(user,localImageViewModel,position,networkViewModel,activity)
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
            if(!isDuplicate)
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
    class ViewHolder(private val binding:ItemUserBinding, private val selectionViewModel: SelectionViewModel):RecyclerView.ViewHolder(binding.root)
    {
        companion object
        {
            @JvmStatic
            @BindingAdapter("imageUrl","id","lIVM","postion")
            fun loadImage(@NonNull ivIcon: ImageView, imageUrl:String, id:Int, lIVM:LocalImageViewModel,position: Int) {
                if (imageUrl.length > 1)
                {
                    lIVM.load(id,ivIcon.context,imageUrl,ivIcon,position)
                }

            }
        }
        fun click(view: View)
        {
            selectionViewModel.select(binding.user!!)
        }
        fun bind(user:User,localImageViewModel: LocalImageViewModel,position: Int,networkViewModel: NetworkViewModel,activity: FragmentActivity)
        {

            binding.user = user
            binding.localImageVM = localImageViewModel
            binding.positon = position
            binding.viewHolder = this
        }
    }

}