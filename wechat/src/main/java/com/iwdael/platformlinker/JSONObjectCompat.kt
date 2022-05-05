package com.iwdael.platformlinker

import org.json.JSONObject


fun JSONObject.copyTo(json: JSONObject): JSONObject {
    this.keys().forEach {
        json.put(it, this[it])
    }
    return json
}