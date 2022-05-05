package com.iwdael.platformlinker

import android.app.Activity
import android.app.Application
import android.os.Bundle


class PlatformLinkerActivityLifecycle : Application.ActivityLifecycleCallbacks {
    private val activities = mutableListOf<Activity>()
    private val boundAuthorizeListener = mutableMapOf<Activity, MutableMap<Authorize, AuthorizeListener>>()
    private val boundShareListener = mutableMapOf<Activity, MutableMap<Authorize, ShareListener>>()
    fun topActivity(): Activity? {
        return activities.firstOrNull()
    }

    fun authorizeListener(authorize: Authorize): AuthorizeListener? {
        activities.forEach {
            boundAuthorizeListener[it]!!.forEach { entry ->
                if (entry.key == authorize)
                    return entry.value
            }
        }
        return null
    }

    fun shareListener(share: Authorize): ShareListener? {
        activities.forEach {
            boundShareListener[it]!!.forEach { entry ->
                if (entry.key == share)
                    return entry.value
            }
        }
        return null
    }

    fun bindAuthorizeListener(authorize: Authorize, listener: AuthorizeListener) {
        val activity = topActivity()!!
        boundAuthorizeListener[activity]!![authorize] = listener
    }

    fun bindShareListener(authorize: Authorize, listener: ShareListener) {
        val activity = topActivity()!!
        boundShareListener[activity]!![authorize] = listener
    }


    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        activities.add(0, activity)
        boundAuthorizeListener[activity] = mutableMapOf()
        boundShareListener[activity] = mutableMapOf()
        PlatformLog.v("life","create:${activity.javaClass.simpleName}")
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        for (weak in activities) {
            if (weak == activity) {
                PlatformLog.v("life","destroy:${activity.javaClass.simpleName}")
                activities.remove(weak)
                boundAuthorizeListener.remove(activity)
                boundShareListener.remove(activity)
                break
            }
        }
    }

}