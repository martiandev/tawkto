package com.tawk.to.mars.git.model.network.request

import java.util.*
//Base class for Network Requests
abstract class Request {
    var uuid:String = UUID.randomUUID().toString()
}