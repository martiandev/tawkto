package com.tawk.to.mars.git.model.entity

import java.util.*

class UserUpdateProfile {
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
        this.name = user.name
        this.company = user.company
        this.blog = user.blog
        this.location = user.location
        this.email = user.email
        this.hireable = user.hireable
        this.bio = user.bio
        this.twitterUsername = user.twitterUsername
        this.publicRepos = user.publicRepos
        this.publicGists = user.publicGists
        this.followers = user.followers
        this.following = user.following
        this.created_at = user.created_at
        this.updated_at = user.updated_at
    }
}