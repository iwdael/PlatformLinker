package com.iwdael.platformlinker

import android.content.Intent
import android.os.Bundle
import com.iwdael.platformlinker.Platforms.Companion.CUSTOM_ERR_CODE
import com.tencent.connect.common.Constants.REQUEST_QQ_SHARE
import com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE
import com.tencent.connect.share.QQShare.*
import com.tencent.connect.share.QzonePublish
import com.tencent.connect.share.QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD
import com.tencent.connect.share.QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO
import com.tencent.connect.share.QzoneShare
import com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_KEY_TYPE
import com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


open class TencentSharePlatform : TencentAuthorizePlatform() {
    companion object {
        const val TAG = "tencent-share"
        private const val NOT_FOUND_LISTENER = "找不到ShareListener"
    }

    override fun share(message: Any, listener: ShareListener) {
        var bundle = Bundle()
        var isFriend: Boolean = true
        when (message) {
            is TencentDefaultFriendMessage -> {
                isFriend = true
                bundle.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_DEFAULT)
                if (message.title.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TITLE, message.title)
                if (message.summary.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.targetURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TARGET_URL, message.targetURL)
                if (message.imageURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                if (message.appName.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_APP_NAME, message.appName)
            }
            is TencentImageFriendMessage -> {
                isFriend = true
                bundle.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_IMAGE)
                if (message.imageURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                if (message.appName.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_APP_NAME, message.appName)
                bundle.putInt(
                    SHARE_TO_QQ_EXT_INT,
                    if (message.hideExtIn) SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE else SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN
                )
            }
            is TencentMusicFriendMessage -> {
                isFriend = true
                bundle.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_AUDIO)
                if (message.title.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TITLE, message.title)
                if (message.summary.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.targetURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TARGET_URL, message.targetURL)
                if (message.imageURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                if (message.audioURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_AUDIO_URL, message.audioURL)
                if (message.appName.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_APP_NAME, message.appName)
            }
            is TencentARKFriendMessage -> {
                isFriend = true
                val json =
                    "{\"app\":\"${message.appPackage}\",\"view\":\"${message.view}\",\"meta\":${message.meta}}"
                bundle.putString(SHARE_TO_QQ_ARK_INFO, json);
            }
            is TencentMiniProgressFriendMessage -> {
                isFriend = true
                if (message.title.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TITLE, message.title)
                if (message.summary.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.targetURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TARGET_URL, message.targetURL)
                if (message.imageURL.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                if (message.miniProgressID.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_MINI_PROGRAM_APPID, message.miniProgressID)
                if (message.miniProgressPath.isNotEmpty())
                    bundle.putString(
                        SHARE_TO_QQ_MINI_PROGRAM_PATH,
                        message.miniProgressPath
                    )
                bundle.putString(
                    SHARE_TO_QQ_MINI_PROGRAM_TYPE,
                    message.miniProgressType.toString()
                )
                bundle.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_MINI_PROGRAM)
            }
            is TencentDefaultQZoneMessage -> {
                isFriend = false
                if (message.title.isNotEmpty())
                    bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, message.title)
                if (message.summary.isNotEmpty())
                    bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.targetURL.isNotEmpty())
                    bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, message.targetURL)
                if (message.imageURL.isNotEmpty())
                    bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                bundle.putInt(SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
            }
            is TencentMoodQZoneMessage -> {
                isFriend = false
                if (message.summary.isNotEmpty())
                    bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.imageURL.isNotEmpty())
                    bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                bundle.putInt(SHARE_TO_QZONE_KEY_TYPE, PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD)
            }
            is TencentVideoQZoneMessage -> {
                isFriend = false
                if (message.summary.isNotEmpty())
                    bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.videoURL.isNotEmpty())
                    bundle.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, message.videoURL)
                bundle.putInt(SHARE_TO_QZONE_KEY_TYPE, PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO)
            }
            is TencentMiniProgressQZoneMessage -> {
                isFriend = false
                if (message.title.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_TITLE, message.title)
                if (message.summary.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_SUMMARY, message.summary)
                if (message.imageURL.isNotEmpty())
                    bundle.putStringArrayList(SHARE_TO_QQ_IMAGE_URL, message.imageURL)
                if (message.miniProgressID.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_MINI_PROGRAM_APPID, message.miniProgressID)
                if (message.miniProgressPath.isNotEmpty())
                    bundle.putString(SHARE_TO_QQ_MINI_PROGRAM_PATH, message.miniProgressPath)
                bundle.putInt(SHARE_TO_QQ_MINI_PROGRAM_TYPE, message.miniProgressType)
                bundle.putInt(SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_MINI_PROGRAM)
            }
            is Bundle -> {
                val error = -10001
                when {
                    bundle.getInt(SHARE_TO_QZONE_KEY_TYPE, error) != error -> {
                        isFriend = false
                    }
                    bundle.getInt(SHARE_TO_QQ_KEY_TYPE, error) != error -> {
                        isFriend = true
                    }
                }
                bundle = message
            }
            else -> {
                PlatformLog.v(TAG, "不支持类型:${message.javaClass.simpleName}")
                listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                return
            }
        }
        val activity = Platforms.instance.lifecycle.topActivity()
        if (activity == null) {
            PlatformLog.v(TAG, NOT_FOUND_CONTEXT)
            listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            return
        }
        listener.onStart(Authorize.TENCENT)
        Platforms.instance.lifecycle.bindShareListener(Authorize.TENCENT, listener)
        val intent = Intent(activity, TencentAuthActivity::class.java)
        intent.putExtra(TencentAuthActivity.BUNDLE_EXTRA_TYPE_IS_AUTHORIZE, false)
        intent.putExtra(TencentAuthActivity.BUNDLE_EXTRA_SHARE_BUNDLE, bundle)
        intent.putExtra(TencentAuthActivity.BUNDLE_EXTRA_SHARE_TYPE_IS_FRIEND, isFriend)
        activity.startActivity(intent)
    }

    fun share(bundle: Bundle, isFriend: Boolean) {
        val activity = Platforms.instance.lifecycle.topActivity()
        val listener = Platforms.instance.lifecycle.shareListener(Authorize.TENCENT)
        if (listener == null) {
            PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            listener?.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            return
        }
        if (activity == null) {
            PlatformLog.v(TAG, NOT_FOUND_CONTEXT)
            listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            return
        }
        if (isFriend) {
            PlatformLog.v(TAG, "在${activity.javaClass.simpleName}发起QQ分享,appID:${appID}")
            tencent.shareToQQ(activity, bundle, shareListener)
        } else {
            PlatformLog.v(TAG, "在${activity.javaClass.simpleName}发起QQ空间分享,appID:${appID}")
            tencent.shareToQzone(activity, bundle, shareListener)
        }
    }

    override fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.activityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_QQ_SHARE)
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener)
        if (requestCode == REQUEST_QZONE_SHARE)
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener)
    }

    private val shareListener = object : IUiListener {
        override fun onComplete(p0: Any?) {
            PlatformLog.v(TAG, "分享成功")
            val listener = Platforms.instance.lifecycle.shareListener(Authorize.TENCENT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onSuccess(Authorize.TENCENT)
            }
        }

        override fun onError(p0: UiError?) {
            PlatformLog.v(TAG, "分享失败")
            val listener = Platforms.instance.lifecycle.shareListener(Authorize.TENCENT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            }
        }

        override fun onCancel() {
            PlatformLog.v(TAG, "分享取消")
            val listener = Platforms.instance.lifecycle.shareListener(Authorize.TENCENT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onCancel(Authorize.TENCENT)
            }
        }

        override fun onWarning(p0: Int) {
        }
    }
}