package com.tawk.to.mars.git.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserUpdate
import com.tawk.to.mars.git.model.entity.UserUpdateNote
import com.tawk.to.mars.git.model.entity.UserUpdateProfile
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat

@RunWith(AndroidJUnit4::class)
class TTDatabaseTest : TestCase()
{
    private lateinit var db:TTDatabase
    private lateinit var userDao:UserDao
    private val USERS = 60
    //Mock User Data
    private val USERS_LOGIN = "login"
    private val USERS_NODE_ID = "node_id"
    private val USERS_AVATAR_URL = "avatar_url"
    private val USERS_GRVATAR_ID = "gravatar_id"
    private val USERS_URL = "url"
    private val USERS_HTML_URL = "html_url"
    private val USERS_FOLLOWERS_URL = "followers_url"
    private val USERS_FOLLOWING_URL = "following_url"
    private val USERS_GIST_URL = "gists_url"
    private val USERS_STARRED_URL = "starred_url"
    private val USERS_SUBS_URL = "subscriptions_url"
    private val USERS_ORG_URL = "organizations_url"
    private val USERS_REPO_URL = "repos_url"
    private val USERS_EVENT_URL = "events_url"
    private val USERS_RECEIVED_EVENTS_URL = "received_events_url"
    private val USERS_SITE_ADMIN = false
    //Mock Profile Data
    private val df = SimpleDateFormat("MM-dd-yy hh:mm")
    private val PROFILE_NAME = "name"
    private val PROFILE_COMPANY = "company"
    private val PROFILE_BLOG = "blog"
    private val PROFILE_LOCATION = "location"
    private val PROFILE_EMAIL = "email"
    private val PROFILE_HIRABLE = true
    private val PROFILE_BIO = "bio"
    private val PROFILE_TWITTER = "twitter_username"
    private val PROFILE_REPO = 1
    private val PROFILE_GIST = 2
    private val PROFILE_FOLLOWERS = 1
    private val PROFILE_FOLLOWING = 2
    private val PROFILE_CREATED_AT = df.parse("11-02-21 11:00")
    private val PROFILE_UPDATED_AT = df.parse("11-03-21 11:00")
    //Mock note
    private val NOTE = "HELLO"
    @Before
    public override fun setUp()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context,TTDatabase::class.java,"memory").build()
        userDao = db.userDao()
    }


    fun initUsers():ArrayList<User>
    {
        var result = ArrayList<User>()
        for(i in 1..USERS)
        {
            var u = User(i)
            result.add(u)
        }
        return result
    }
    fun populateData(user:User):User
    {
        user.login = USERS_LOGIN
        user.nodeId = USERS_NODE_ID
        user.avatarUrl = USERS_AVATAR_URL
        user.gravatarId = USERS_GRVATAR_ID
        user.url = USERS_URL
        user.htmlUrl = USERS_HTML_URL
        user.followersUrl = USERS_FOLLOWERS_URL
        user.followingUrl = USERS_FOLLOWING_URL
        user.gistUrl = USERS_GIST_URL
        user.starredUrl = USERS_STARRED_URL
        user.subscriptionsUrl = USERS_SUBS_URL
        user.organizationsUrl = USERS_ORG_URL
        user.reposUrl = USERS_REPO_URL
        user.eventsUrl = USERS_EVENT_URL
        user.receivedEventsUrl = USERS_RECEIVED_EVENTS_URL
        user.isSiteAdmin = USERS_SITE_ADMIN
        return user
    }
    fun populateProfile(user:User):User
    {
        user.name = PROFILE_NAME
        user.company = PROFILE_COMPANY
        user.blog = PROFILE_BLOG
        user.location = PROFILE_LOCATION
        user.email = PROFILE_EMAIL
        user.hireable = PROFILE_HIRABLE
        user.bio = PROFILE_BIO
        user.twitterUsername = PROFILE_TWITTER
        user.publicRepos = PROFILE_REPO
        user.publicGists = PROFILE_GIST
        user.followers = PROFILE_FOLLOWERS
        user.following = PROFILE_FOLLOWING
        user.created_at = PROFILE_CREATED_AT
        user.updated_at = PROFILE_UPDATED_AT
        return user
    }
    @Test
    fun initUsersTest()
    {
        var users = initUsers()
        assertEquals("number of users does not match",USERS,users.size)
    }
    @Test
    fun insert()
    {
        var users = initUsers()
        userDao.nukeTable()
        for(u in users)
        {
            assertNotSame("Insert Failed",(-1).toLong(),userDao.insert(u))
        }
        for(u in users)
        {
            assertSame("Duplicate not blocked",(-1).toLong(),userDao.insert(u))
        }

    }

    @Test
    fun clear()
    {
        userDao.insert(initUsers())
        assertEquals("Database should be populated",30,userDao.getSince(0,30).size)
        userDao.nukeTable()
        assertEquals("Database should be empty",0,userDao.getSince(0,30).size)
    }
    @Test
    fun get()
    {
        var users = initUsers()
        userDao.insert(initUsers())
        assertEquals("Database should be populated",30,userDao.getSince(0,30).size)
        var nullUser = userDao.get(0)
        var u = userDao.get(1)
        assertNull("User should be null retrieved",nullUser)
        assertEquals("User not retrieved",1,u.id)
    }
    //Should update user only
    @Test
    fun update()
    {
        userDao.nukeTable()
        userDao.insert(initUsers())
        assertEquals("Database should be populated",30,userDao.getSince(0,30).size)
        var u = userDao.get(1)
        populateData(u)
        userDao.update(UserUpdate(u))
        var userUpdate = userDao.get(1)

        assertEquals("login not saved",USERS_LOGIN,userUpdate.login)
        assertEquals("node id not saved",USERS_NODE_ID,userUpdate.nodeId)
        assertEquals("avatar not saved",USERS_AVATAR_URL,userUpdate.avatarUrl)
        assertEquals("gavatar not saved",USERS_GRVATAR_ID,userUpdate.gravatarId)
        assertEquals("url not saved",USERS_URL,userUpdate.url)
        assertEquals("html url not saved",USERS_HTML_URL,userUpdate.htmlUrl)
        assertEquals("followers url not saved",USERS_FOLLOWERS_URL,userUpdate.followersUrl)
        assertEquals("following url not saved",USERS_FOLLOWING_URL,userUpdate.followingUrl)
        assertEquals("gist url not saved",USERS_GIST_URL,userUpdate.gistUrl)
        assertEquals("starred url not saved",USERS_STARRED_URL,userUpdate.starredUrl)
        assertEquals("subscription url not saved",USERS_SUBS_URL,userUpdate.subscriptionsUrl)
        assertEquals("org url not saved",USERS_ORG_URL,userUpdate.organizationsUrl)
        assertEquals("repo url not saved",USERS_REPO_URL,userUpdate.reposUrl)
        assertEquals("event url not saved",USERS_EVENT_URL,userUpdate.eventsUrl)
        assertEquals("event received url not saved",USERS_RECEIVED_EVENTS_URL,userUpdate.receivedEventsUrl)
        assertEquals("is site admin not saved",USERS_SITE_ADMIN,userUpdate.isSiteAdmin)

        assertEquals("name should be null",null,userUpdate.name)
        assertEquals("company should be null",null,userUpdate.company)
        assertEquals("blog should be null",null,userUpdate.blog)
        assertEquals("location should be null",null,userUpdate.location)
        assertEquals("email should be null",null,userUpdate.email)
        assertEquals("hireable should be null",null,userUpdate.hireable)
        assertEquals("bio should be null",null,userUpdate.bio)
        assertEquals("twitterUsername should be null",null,userUpdate.twitterUsername)
        assertEquals("publicRepos should be null",null,userUpdate.publicRepos)
        assertEquals("publicGists should be null",null,userUpdate.publicGists)
        assertEquals("followers should be null",null,userUpdate.followers)
        assertEquals("following should be null",null,userUpdate.following)
        assertEquals("created_at should be null",null,userUpdate.created_at)
        assertEquals("updated_at should be null",null,userUpdate.updated_at)

        assertEquals("note should be empty","",userUpdate.note)


        //

    }

    //Should update profile only
    @Test
    fun updateProfile()
    {
        userDao.nukeTable()
        userDao.insert(initUsers())
        assertEquals("Database should be populated",30,userDao.getSince(0,30).size)
        var u = userDao.get(1)
        populateProfile(u)
        userDao.update(UserUpdateProfile(u))
        var userUpdate = userDao.get(1)

        assertEquals("login should be null",null,userUpdate.login)
        assertEquals("node id should be null",null,userUpdate.nodeId)
        assertEquals("avatar should be null",null,userUpdate.avatarUrl)
        assertEquals("gavatar should be null",null,userUpdate.gravatarId)
        assertEquals("url should be null",null,userUpdate.url)
        assertEquals("html url should be null",null,userUpdate.htmlUrl)
        assertEquals("followers url should be null",null,userUpdate.followersUrl)
        assertEquals("following url should be null",null,userUpdate.followingUrl)
        assertEquals("gist url should be null",null,userUpdate.gistUrl)
        assertEquals("starred url should be null",null,userUpdate.starredUrl)
        assertEquals("subscription url should be null",null,userUpdate.subscriptionsUrl)
        assertEquals("org url should be null",null,userUpdate.organizationsUrl)
        assertEquals("repo url should be null",null,userUpdate.reposUrl)
        assertEquals("event url should be null",null,userUpdate.eventsUrl)
        assertEquals("event received url should be null",null,userUpdate.receivedEventsUrl)
        assertEquals("is site admin should be null",null,userUpdate.isSiteAdmin)

        assertEquals("name was not saved",PROFILE_NAME,userUpdate.name)
        assertEquals("company was not saved",PROFILE_COMPANY,userUpdate.company)
        assertEquals("blog was not saved",PROFILE_BLOG,userUpdate.blog)
        assertEquals("location was not saved",PROFILE_LOCATION,userUpdate.location)
        assertEquals("email was not saved",PROFILE_EMAIL,userUpdate.email)
        assertEquals("hireable was not saved",PROFILE_HIRABLE,userUpdate.hireable)
        assertEquals("bio was not saved",PROFILE_BIO,userUpdate.bio)
        assertEquals("twitterUsername was not saved",PROFILE_TWITTER,userUpdate.twitterUsername)
        assertEquals("publicRepos was not saved",PROFILE_REPO,userUpdate.publicRepos)
        assertEquals("publicGists was not saved",PROFILE_GIST,userUpdate.publicGists)
        assertEquals("followers was not saved",PROFILE_FOLLOWERS,userUpdate.followers)
        assertEquals("following was not saved",PROFILE_FOLLOWING,userUpdate.following)
        assertEquals("created_at was not saved",PROFILE_CREATED_AT,userUpdate.created_at)
        assertEquals("updated_at was not saved",PROFILE_UPDATED_AT,userUpdate.updated_at)

        assertEquals("note should be empty","",userUpdate.note)


    }

    //Should update note only
    @Test
    fun updateNote()
    {
        userDao.nukeTable()
        userDao.insert(initUsers())
        assertEquals("Database should be populated",30,userDao.getSince(0,30).size)
        var u = userDao.get(1)
        populateProfile(u)
        userDao.update(UserUpdateNote(u.id,NOTE))
        var userUpdate = userDao.get(1)

        assertEquals("login should be null",null,userUpdate.login)
        assertEquals("node id should be null",null,userUpdate.nodeId)
        assertEquals("avatar should be null",null,userUpdate.avatarUrl)
        assertEquals("gavatar should be null",null,userUpdate.gravatarId)
        assertEquals("url should be null",null,userUpdate.url)
        assertEquals("html url should be null",null,userUpdate.htmlUrl)
        assertEquals("followers url should be null",null,userUpdate.followersUrl)
        assertEquals("following url should be null",null,userUpdate.followingUrl)
        assertEquals("gist url should be null",null,userUpdate.gistUrl)
        assertEquals("starred url should be null",null,userUpdate.starredUrl)
        assertEquals("subscription url should be null",null,userUpdate.subscriptionsUrl)
        assertEquals("org url should be null",null,userUpdate.organizationsUrl)
        assertEquals("repo url should be null",null,userUpdate.reposUrl)
        assertEquals("event url should be null",null,userUpdate.eventsUrl)
        assertEquals("event received url should be null",null,userUpdate.receivedEventsUrl)
        assertEquals("is site admin should be null",null,userUpdate.isSiteAdmin)

        assertEquals("name should be null",null,userUpdate.name)
        assertEquals("company should be null",null,userUpdate.company)
        assertEquals("blog should be null",null,userUpdate.blog)
        assertEquals("location should be null",null,userUpdate.location)
        assertEquals("email should be null",null,userUpdate.email)
        assertEquals("hireable should be null",null,userUpdate.hireable)
        assertEquals("bio should be null",null,userUpdate.bio)
        assertEquals("twitterUsername should be null",null,userUpdate.twitterUsername)
        assertEquals("publicRepos should be null",null,userUpdate.publicRepos)
        assertEquals("publicGists should be null",null,userUpdate.publicGists)
        assertEquals("followers should be null",null,userUpdate.followers)
        assertEquals("following should be null",null,userUpdate.following)
        assertEquals("created_at should be null",null,userUpdate.created_at)
        assertEquals("updated_at should be null",null,userUpdate.updated_at)


        assertEquals("note should be :"+NOTE,NOTE,userUpdate.note)


    }

    @Test
    fun retrieve30()
    {
        userDao.nukeTable()
        userDao.insert(initUsers())
        assertEquals("Users Retrieved should be 30",30,userDao.getSince(0,30).size)
    }
    @Test
    fun retrieveSearch()
    {
        userDao.nukeTable()
        val users = initUsers()
        users.get(0).login = "mock"
        users.get(0).note = "note mock"
        users.get(1).login = "user"
        users.get(1).note = "mock"
        users.get(2).login = "note"
        users.get(2).note = "user"
        users.get(3).login = "unique"
        users.get(3).note = "note user unique"
        userDao.insert(users)
        assertEquals("Users Retrieved should be 3",3,userDao.search("user").size)
        assertEquals("Users Retrieved should be 2",2,userDao.search("mock").size)
        assertEquals("Users Retrieved should be 3",3,userDao.search("note").size)
        assertEquals("Users Retrieved should be 1",1,userDao.search("uniq").size)
    }

    @After
    fun closeDb() {
        db.close()
    }




}