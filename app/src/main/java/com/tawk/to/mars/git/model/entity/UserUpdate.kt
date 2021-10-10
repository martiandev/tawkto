package com.tawk.to.mars.git.model.entity


//Update basic user info
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

    constructor(user:User)
    {
        this.id = user.id
        this.login = user.login
        this.nodeId = user.nodeId
        this.avatarUrl = user.avatarUrl
        this.gravatarId = user.gravatarId
        this.url = user.url
        this.htmlUrl = user.htmlUrl
        this.followersUrl = user.followersUrl
        this.followingUrl = user.followingUrl
        this.gistUrl = user.gistUrl
        this.starredUrl = user.starredUrl
        this.subscriptionsUrl = user.subscriptionsUrl
        this.organizationsUrl = user.organizationsUrl
        this.reposUrl = user.reposUrl
        this.eventsUrl = user.eventsUrl
        this.receivedEventsUrl = user.receivedEventsUrl
        this.type = user.type
        this.isSiteAdmin = user.isSiteAdmin

    }
}