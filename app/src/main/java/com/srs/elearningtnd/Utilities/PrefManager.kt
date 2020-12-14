package com.srs.elearningtnd.Utilities

import android.content.Context
import android.content.SharedPreferences

class PrefManager(_context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context? = null
    // shared pref mode
    var privateMode = 0
    // shared pref mode
    var PRIVATE_MODE = 0

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    var login: Int
        get() = pref.getInt(LOGIN, 0)
        set(isLogged) {
            editor.putInt(LOGIN, isLogged)
            editor.commit()
        }

    var session: Boolean
        get() = pref.getBoolean(SESSION, false)
        set(sessionActive) {
            editor.putBoolean(SESSION, sessionActive)
            editor.commit()
        }

    var name: String?
        get() = pref.getString(NAME, null)
        set(sureName) {
            editor.putString(NAME, sureName)
            editor.commit()
        }

    var userid: String?
        get() = pref.getString(USERID, null)
        set(userId) {
            editor.putString(USERID, userId)
            editor.commit()
        }

    var departemen: String?
        get() = pref.getString(DEPARTEMEN, null)
        set(dept) {
            editor.putString(DEPARTEMEN, dept)
            editor.commit()
        }

    var lokasi_kerja: String?
        get() = pref.getString(LOKASI, "")
        set(lokker) {
            editor.putString(LOKASI, lokker)
            editor.commit()
        }

    var jabatan: String?
        get() = pref.getString(JABATAN, null)
        set(jabtn) {
            editor.putString(JABATAN, jabtn)
            editor.commit()
        }

    var nohp: String?
        get() = pref.getString(NOHP, null)
        set(hp) {
            editor.putString(NOHP, hp)
            editor.commit()
        }

    var email: String?
        get() = pref.getString(EMAIL, null)
        set(mail) {
            editor.putString(EMAIL, mail)
            editor.commit()
        }

    var password: String?
        get() = pref.getString(PASSWORD, null)
        set(pass) {
            editor.putString(PASSWORD, pass)
            editor.commit()
        }

    var lokasi: String?
        get() = pref.getString(LOKASI, null)
        set(lokasi) {
            editor.putString(LOKASI, lokasi)
            editor.commit()
        }

    var akses: String?
        get() = pref.getString(AKSES, null)
        set(akses) {
            editor.putString(AKSES, akses)
            editor.commit()
        }

    var estate: String?
        get() = pref.getString(ESTATE, null)
        set(estate) {
            editor.putString(ESTATE, estate)
            editor.commit()
        }

    var gabungBlok: String?
        get() = pref.getString(GABUNG_BLOK, null)
        set(gabungblok) {
            editor.putString(GABUNG_BLOK, gabungblok)
            editor.commit()
        }

    var ver: String?
        get() = pref.getString(VER, null)
        set(ver) {
            editor.putString(VER, ver)
            editor.commit()
        }

    var afdeling: String?
        get() = pref.getString(AFDELING, null)
        set(afdeling) {
            editor.putString(AFDELING, afdeling)
            editor.commit()
        }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "sulungresearch"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val LOGIN = "Login"
        private const val SESSION = "Session"

        const val USERID = "user_id"
        const val NAME = "nama_lengkap"
        const val DEPARTEMEN = "departemen"
        const val JABATAN = "jabatan"
        const val NOHP = "no_hp"
        const val EMAIL = "email"
        const val LOKASI = "lokasi_kerja"
        const val AKSES = "akses_level"
        const val PASSWORD = "password"
        const val VER = "ver"
        const val ESTATE = "estate"
        const val GABUNG_BLOK = "gabung_blok"

        //DATA BLOK
        const val AFDELING = "afdeling"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, privateMode)
        editor = pref.edit()
    }

    fun prefManag(context: Context) {
        this. context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun timeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
}