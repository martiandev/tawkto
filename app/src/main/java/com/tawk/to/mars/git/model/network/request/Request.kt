package com.tawk.to.mars.git.model.network.request

import java.util.*

abstract class Request {
    var uuid:String = UUID.randomUUID().toString()
}