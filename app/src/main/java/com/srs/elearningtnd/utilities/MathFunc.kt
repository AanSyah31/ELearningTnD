package com.srs.elearningtnd.utilities

import org.json.JSONObject
import kotlin.Exception

class MathFunc {

    var digits = 0

    fun arrayAdder(userDetail: JSONObject, int: Int):String{
        return try { userDetail.getString(int.toString()) }catch (e:Exception){ "null" }
    }

    fun arrayAdderFloat(userDetail: JSONObject, int: Int):Float{
        return try { userDetail.getString(int.toString()).toFloat() }catch (e:Exception){ 0f }
    }

    fun arrayAdderDB(userDetail: JSONObject, int: Int, arrayString: Array<String>): String{
        return try{arrayString[userDetail.getString(int.toString()).toInt()]}catch (e: Exception){"null"}
    }
}