@file:Suppress("DEPRECATION")

package com.srs.elearningtnd.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.google.android.material.button.MaterialButton
import com.srs.elearningtnd.R
import kotlinx.android.synthetic.main.dialog_layout_success.view.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat


@Suppress("DEPRECATION")
class UpdateMan {

    @SuppressLint("SimpleDateFormat")
    fun setLastUpdateText(tv_tanggal: TextView, fileDir: String) {
        val fileMain = File(fileDir)
        val sdf = SimpleDateFormat("dd-MMM-yyyy HH:mm")
        val lastModDate = sdf.format(fileMain.lastModified())
        tv_tanggal.text = lastModDate.toString()
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun checkUpdate(context: Context, urlType: String, tv_tanggal: TextView, bt_update: MaterialButton, progressBarHolder: ConstraintLayout) {
        progressBarHolder.visibility = View.VISIBLE

        val filePath = context.getExternalFilesDir(null)?.absolutePath + "/CACHE/"
        val connected: Boolean
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state === NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state === NetworkInfo.State.CONNECTED
        val f = File(filePath + urlType)
        if ((connected && !f.exists())) {
            val config = PRDownloaderConfig.newBuilder()
                    .setReadTimeout(30000)
                    .setConnectTimeout(30000)
                    .build()
            PRDownloader.initialize(context as Activity, config)

            @Suppress("UNUSED_VARIABLE") val downloadId = PRDownloader.download("${Database.server}/files/header$urlType", filePath, "header$urlType")
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .start(object : OnDownloadListener {
                        @SuppressLint("SetTextI18n", "InflateParams")
                        override fun onDownloadComplete() {
                            lateinit var cacheCheck: String
                            progressBarHolder.visibility = View.GONE
                            val mainCheck = md5Checksum(context.getExternalFilesDir(null)?.absolutePath + "/MAIN/$urlType")
                            Log.d("checkreport", "local: $mainCheck")

                            try {
                                val charset: Charset = Charsets.UTF_8
                                val `is`: InputStream = FileInputStream(context.getExternalFilesDir(null)?.absolutePath + "/CACHE/header$urlType")
                                val size = `is`.available()
                                val buffer = ByteArray(size)
                                `is`.read(buffer)
                                `is`.close()
                                val json = String(buffer, charset)

                                val objCache = JSONObject(json)
                                val userArrayCache = objCache.getJSONArray("md5Hex")

                                cacheCheck = (userArrayCache.getJSONObject(0).getJSONArray("hex").getString(0))

                                Log.d("checkreport", "online: $cacheCheck")
                            } catch (e: Exception) {
                                Log.d("checkreport", e.toString())
                            }

                            if (cacheCheck == mainCheck) {
                                Log.d("checkreport", "SAMA")
                                UpdateMan().setLastUpdateText(tv_tanggal, context.getExternalFilesDir(null)?.absolutePath + "/MAIN/" + urlType)
                                tv_tanggal.highlightColor
                                bt_update.text = "TERBARU!!"
                                bt_update.setTextColor(Color.RED)
                                bt_update.setBackgroundColor(Color.WHITE)

                                AlertDialogUtility.alertDialog(context, "DATA TERBARU!!", "success.json")

                                val fDeleteDBC = File(context.getExternalFilesDir(null)?.absolutePath + "/CACHE/" + "header$urlType")
                                if (fDeleteDBC.exists()) {
                                    fDeleteDBC.delete()
                                }

                            } else if (cacheCheck != mainCheck) {
                                Log.d("checkreport", "BEDA")
                                tv_tanggal.text = "Terdapat Update Baru"
                                tv_tanggal.setTextColor(Color.RED)
                                bt_update.text = "UNDUH"

                                val fDeleteDBC = File(context.getExternalFilesDir(null)?.absolutePath + "/CACHE/" + "header$urlType")
                                if (fDeleteDBC.exists()) {
                                    fDeleteDBC.delete()
                                }

                                val layoutBuilder = LayoutInflater.from(context).inflate(R.layout.dialog_layout_success, null)
                                val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(layoutBuilder)
                                val alertDialog: AlertDialog = builder.show()
                                alertDialog.window?.setBackgroundDrawableResource(R.drawable.background_white)
                                layoutBuilder.tv_alert.text = "TERDAPAT DATA UPDATE TERBARU"
                                layoutBuilder.lottie_anim.setAnimation("download.json")
                                layoutBuilder.lottie_anim.loop(true)
                                layoutBuilder.lottie_anim.playAnimation()
                                layoutBuilder.btn_action.visibility = View.VISIBLE
                                layoutBuilder.space.visibility = View.VISIBLE
                                layoutBuilder.btn_dismiss.text = "LAIN KALI"
                                layoutBuilder.btn_dismiss.setOnClickListener {
                                    alertDialog.dismiss()
                                }
                                layoutBuilder.btn_action.setOnClickListener {
                                    bt_update.performClick()
                                    alertDialog.dismiss()
                                    val fDelete = File(context.getExternalFilesDir(null)?.absolutePath + "/MAIN/" + urlType)
                                    if (fDelete.exists()) {
                                        fDelete.delete()
                                    }
                                }
                            }
                        }

                        override fun onError(error: com.downloader.Error?) {
                            AlertDialogUtility.alertDialog(context, "Terjadi kesalahan!!, $error", "warning.json")
                        }
                    })
        } else {
            AlertDialogUtility.alertDialog(context, "Jaringan anda tidak stabil, mohon hubungkan ke jaringan yang stabil!", "network_error.json")
        }

    }

    fun md5Checksum(data: String): String {
        var digest = ""
        try { // Define the data file path and create an InputStream object.
            val file = File(data)
            val `is`: InputStream = FileInputStream(file)
            // Calculates the MD5 digest of the given InputStream object.
            // It will generate a 32 characters hex string.
            digest = String(Hex.encodeHex(DigestUtils.md5(`is`)))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return digest
    }

    fun updater(jsonFile: String, context: Context, bt_update: MaterialButton, tv_tanggal: TextView, progressBarHolder: ConstraintLayout, viewType: String, view: String?) {
        if (bt_update.text == "UPDATE") {
            UpdateMan().checkUpdate(context, jsonFile, tv_tanggal, bt_update, progressBarHolder)
        } else if (bt_update.text == "UNDUH") {
            tv_tanggal.setTextColor(Color.RED)
            bt_update.text = context.getString(R.string.terbaru)

            val intent = Intent(context, Loading::class.java)
            intent.putExtra("ViewType", viewType)
            intent.putExtra("View", view)
            Log.d("viewtype", "Viewtype=$viewType View=$view")
            (context as Activity).startActivity(intent)
        }
    }

}