package com.hacknife.authority

import androidx.appcompat.app.AppCompatActivity

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : 授权
 * version: 1.0
 */
abstract class Authorization(protected  val auth: Auth) {


    abstract fun auth()

}