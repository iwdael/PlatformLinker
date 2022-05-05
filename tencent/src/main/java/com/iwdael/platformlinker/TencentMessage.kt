package com.iwdael.platformlinker

import java.util.ArrayList


data class TencentDefaultFriendMessage(
    val title: String,
    val summary: String,
    val targetURL: String,
    val imageURL: String = "",
    val appName: String = ""
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("TencentDefaultFriendMessage#title is empty")
        if (summary.isEmpty()) throw Exception("TencentDefaultFriendMessage#summary is empty")
        if (targetURL.isEmpty()) throw Exception("TencentDefaultFriendMessage#targetURL is empty")
    }

    override fun authorize() = Authorize.TENCENT
}


class TencentImageFriendMessage(
    val imageURL: String = "",
    val appName: String = "",
    val hideExtIn: Boolean = false
) : Message {
    override fun check() {
        if (imageURL.isEmpty()) throw Exception("TencentImageFriendMessage#imageURL is empty")
        if (appName.isEmpty()) throw Exception("TencentImageFriendMessage#appName is empty")
    }
    override fun authorize() = Authorize.TENCENT
}

data class TencentMusicFriendMessage(
    val title: String,
    val targetURL: String,
    val audioURL: String,
    val summary: String = "",
    val imageURL: String = "",
    val appName: String = "",
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("TencentMusicFriendMessage#title is empty")
        if (targetURL.isEmpty()) throw Exception("TencentMusicFriendMessage#targetURL is empty")
        if (audioURL.isEmpty()) throw Exception("TencentMusicFriendMessage#audioURL is empty")
    }
    override fun authorize() = Authorize.TENCENT
}


data class TencentARKFriendMessage(
    val appPackage: String,
    val view: String,
    val meta: Map<String, String>
) : Message {
    override fun check() {
        if (appPackage.isEmpty()) throw Exception("TencentARKFriendMessage#appPackage is empty")
        if (view.isEmpty()) throw Exception("TencentARKFriendMessage#view is empty")
        if (meta.isEmpty()) throw Exception("TencentARKFriendMessage#meta is empty")
    }
    override fun authorize() = Authorize.TENCENT
}


data class TencentMiniProgressFriendMessage(
    val title: String,
    val summary: String,
    val targetURL: String,
    val imageURL: String,
    val miniProgressID: String,
    val miniProgressPath: String,
    //1体验版  3正式版
    val miniProgressType: Int = 1
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#title is empty")
        if (summary.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#summary is empty")
        if (targetURL.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#targetURL is empty")
        if (imageURL.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#imageURL is empty")
        if (miniProgressID.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#miniProgressID is empty")
        if (miniProgressPath.isEmpty()) throw Exception("TencentMiniProgressFriendMessage#miniProgressPath is empty")
    }
    override fun authorize() = Authorize.TENCENT
}

data class TencentDefaultQZoneMessage(
    val title: String,
    val summary: String,
    val targetURL: String,
    val imageURL: ArrayList<String> = arrayListOf()
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("TencentDefaultQZoneMessage#title is empty")
        if (summary.isEmpty()) throw Exception("TencentDefaultQZoneMessage#summary is empty")
        if (targetURL.isEmpty()) throw Exception("TencentDefaultQZoneMessage#targetURL is empty")
    }
    override fun authorize() = Authorize.TENCENT
}

data class TencentMoodQZoneMessage(
    val summary: String,
    val imageURL: ArrayList<String> = arrayListOf()
) : Message {
    override fun check() {
        if (summary.isEmpty() && imageURL.isEmpty()) throw Exception("TencentMoodQZoneMessage#summary and TencentMoodQZoneMessage#imageURL is empty")
    }
    override fun authorize() = Authorize.TENCENT
}

data class TencentVideoQZoneMessage(
    val summary: String,
    val videoURL: String,
) : Message {
    override fun check() {
        if (summary.isEmpty() && videoURL.isEmpty()) throw Exception("TencentVideoQZoneMessage#summary and TencentVideoQZoneMessage#videoURL is empty")
    }
    override fun authorize() = Authorize.TENCENT
}

data class TencentMiniProgressQZoneMessage(
    val title: String = "",
    val summary: String = "",
    val imageURL: ArrayList<String>,
    val miniProgressID: String,
    val miniProgressPath: String,
    //1体验版  3正式版
    val miniProgressType: Int = 1
) : Message {
    override fun check() {
        if (imageURL.isEmpty()) throw Exception("TencentMiniProgressQZoneMessage#imageURL is empty")
        if (miniProgressID.isEmpty()) throw Exception("TencentMiniProgressQZoneMessage#miniProgressID is empty")
        if (miniProgressPath.isEmpty()) throw Exception("TencentMiniProgressQZoneMessage#miniProgressPath is empty")
    }
    override fun authorize() = Authorize.TENCENT
}
