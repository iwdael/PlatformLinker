package com.iwdael.platformlinker

enum class WechatScene(val scene: Int) {
    FRIENDS(0),
    CIRCLE(1),
    FAVORITE(2)
}

data class WechatTextMessage(
    val title: String,
    val summary: String,
    val text: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}


data class WechatImageMessage(
    val title: String,
    val summary: String,
    val image: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}

data class WechatMusicMessage(
    val title: String,
    val summary: String,
    val musicUrl: String,
    val albumUrl: String = "",
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}

data class WechatVideoUrlMessage(
    val title: String,
    val summary: String,
    val videoUrl: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}

data class WechatVideoFileMessage(
    val title: String,
    val summary: String,
    val videoFilePath: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}

data class WechatWebpageMessage(
    val title: String,
    val summary: String,
    val webpageUrl: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}


data class WechatMiniProgressMessage(
    val title: String,
    val summary: String,
    val webpageUrl: String,
    val userName: String,
    val path: String,
    val scene: WechatScene,
) : Message {
    override fun check() {
        if (title.isEmpty()) throw Exception("WechatTextMessage#title is empty")
        if (summary.isEmpty()) throw Exception("WechatTextMessage#summary is empty")
    }

    override fun authorize() = Authorize.WECHAT
}