package com.tawk.to.mars.git.model.entity

import java.util.*

class UserUpdate {
    var id:Int ?= null
    var login:String ?= null
    var nodeId:String ?= null
    var avatarUrl:String ? = null
    var gravatarId:String ?= null
    var url:String ?= null
    var htmlUrl:String ?= null
    var followersUrl:String ?= null
    var followingUrl:String ?= null
    var gistUrl:String ?= null
    var starredUrl:String ?= null
    var subscriptionsUrl:String ?= null
    var organizationsUrl:String ?= null
    var reposUrl:String ?= null
    var eventsUrl:String ?= null
    var receivedEventsUrl:String ?= null
    var type:String ?= null
    var isSiteAdmin:Boolean ?= null
    var name:String ?= null
    var company:String ?= null
    var blog:String ?= null
    var location:String ?= null
    var email:String ?= null
    var hireable:Boolean ?= null
    var bio:String ?= null
    var twitterUsername:String ?= null
    var publicRepos:Int ?= null
    var publicGists:Int ?= null
    var followers:Int ?= null
    var following:Int ?= null
    var created_at: Date?= null
    var updated_at: Date?= null
}