package com.tawk.to.mars.git.view.fragment.viewpager

import com.tawk.to.mars.git.model.entity.User


interface ClickListener {

    abstract fun onSelected(b: User)
}