package com.tawk.to.mars.git.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.tawk.to.mars.git.model.entity.embedded.Note
import java.util.*

@Entity
data class User(var id:Int) {

    @SerializedName("login")
    var login:String ?= null
    @SerializedName("node_id")
    var nodeId:String ?= null
    @SerializedName("avatar_url")
    var avatarUrl:String ? = null
    @SerializedName("gravatar_id")
    var gravatarId:String ?= null
    var url:String ?= null
    @SerializedName("html_url")
    var htmlUrl:String ?= null
    @SerializedName("followers_url")
    var followersUrl:String ?= null
    @SerializedName("following_url")
    var followingUrl:String ?= null
    @SerializedName("gists_url")
    var gistUrl:String ?= null
    @SerializedName("starred_url")
    var starredUrl:String ?= null
    @SerializedName("subscriptions_url")
    var subscriptionsUrl:String ?= null
    @SerializedName("organizations_url")
    var organizationsUrl:String ?= null
    @SerializedName("repos_url")
    var reposUrl:String ?= null
    @SerializedName("events_url")
    var eventsUrl:String ?= null
    @SerializedName("received_events_url")
    var receivedEventsUrl:String ?= null
    var type:String ?= null
    @SerializedName("site_admin")
    var isSiteAdmin:Boolean ?= null
    var name:String ?= null
    var company:String ?= null
    var blog:String ?= null
    var location:String ?= null
    var email:String ?= null
    var hireable:Boolean ?= null
    var bio:String ?= null
    @SerializedName("twitter_username")
    var twitterUsername:String ?= null
    @SerializedName("public_repos")
    var publicRepos:Int ?= null
    @SerializedName("public_gists")
    var publicGists:Int ?= null
    var followers:Int ?= null
    var following:Int ?= null
    @SerializedName("created_at")
    var created_at: String?= null
    @SerializedName("updated_at")
    var updated_at:String ?= null
//    @Embedded
//    var note: Note?= null
}