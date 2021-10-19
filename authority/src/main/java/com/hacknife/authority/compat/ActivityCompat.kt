package com.hacknife.authority.compat

import androidx.appcompat.app.AppCompatActivity
import com.hacknife.authority.proxy.ProxyFragment

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */

fun AppCompatActivity.findProxyFragment(): ProxyFragment? {
    return supportFragmentManager.findFragmentByTag(ProxyFragment.TAG) as ProxyFragment?
}

fun AppCompatActivity.getProxyFragment(): ProxyFragment? {
    var proxyFragment: ProxyFragment? = findProxyFragment()
    val isNewInstance = proxyFragment == null
    if (isNewInstance) {
        proxyFragment = ProxyFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(proxyFragment, ProxyFragment.TAG).commitNow()
    }
    return proxyFragment
}