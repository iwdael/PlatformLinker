package com.iwdael.platformlinker

import android.content.Intent
import com.iwdael.platformlinker.Platforms.Companion.CUSTOM_ERR_CODE
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


open class WechatSharePlatform : WechatAuthPlatform() {


    companion object {
        private const val NOT_FOUND_LISTENER = "找不到 ShareListener"
        const val TAG = "wechat-share"
    }

    override fun share(message: Any, listener: ShareListener) {
        var req = SendMessageToWX.Req()
        when (message) {
            is WechatTextMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXTextObject().apply {
                            text = message.text
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }
            is WechatImageMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXImageObject().apply {
                            this.imagePath = message.image
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }
            is WechatMusicMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXMusicObject().apply {
                            this.musicUrl = message.musicUrl
                            this.songAlbumUrl = message.albumUrl
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }

            is WechatVideoUrlMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXVideoObject().apply {
                            this.videoUrl = message.videoUrl
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }
            is WechatVideoFileMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXVideoFileObject().apply {
                            this.filePath = message.videoFilePath
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }

            is WechatWebpageMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXWebpageObject().apply {
                            this.webpageUrl = message.webpageUrl
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }
            is WechatMiniProgressMessage -> {
                val msg = WXMediaMessage()
                    .apply {
                        title = message.title
                        description = message.summary
                        mediaObject = WXMiniProgramObject().apply {
                            this.webpageUrl = message.webpageUrl
                            this.userName = message.userName
                            this.path = message.path
                        }
                    }
                req.message = msg
                req.scene = message.scene.scene
                req.transaction = message.javaClass.simpleName
            }
            is SendMessageToWX.Req->{
                req = message
            }
            else -> {
                PlatformLog.v(TAG, "不支持类型:${message.javaClass.simpleName}")
                listener.onFail(Authorize.WECHAT, CUSTOM_ERR_CODE)
                return
            }
        }
        Platforms.instance.lifecycle.bindShareListener(Authorize.WECHAT, listener)
        listener.onStart(Authorize.WECHAT)
        iwxapi.sendReq(req)
    }

    private val apiEventHandler = object : IWXAPIEventHandler {
        override fun onReq(p0: BaseReq?) {

        }

        override fun onResp(p0: BaseResp?) {
            PlatformLog.v(TAG, "onResp:${p0?.type}")
            p0 ?: return
            if (p0.type != ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) return
            val resp = p0 as SendMessageToWX.Resp
            val listener = Platforms.instance.lifecycle.shareListener(Authorize.WECHAT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                return
            }
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    PlatformLog.v(TAG, "分享成功")
                    listener.onSuccess(Authorize.WECHAT)
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    PlatformLog.v(TAG, "分享取消")
                    listener.onCancel(Authorize.WECHAT)
                }
                BaseResp.ErrCode.ERR_SENT_FAILED,
                BaseResp.ErrCode.ERR_BAN,
                BaseResp.ErrCode.ERR_UNSUPPORT,
                -> {
                    PlatformLog.v(TAG, "分享取消")
                    listener.onCancel(Authorize.WECHAT)
                }
            }
        }
    }

    override fun handleIntent(data: Intent?) {
        super.handleIntent(data)
        iwxapi.handleIntent(data, apiEventHandler)
    }
}