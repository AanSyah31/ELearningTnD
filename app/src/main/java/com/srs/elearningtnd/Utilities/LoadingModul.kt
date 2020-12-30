@file:Suppress("DEPRECATION")

package com.srs.elearningtnd.Utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.srs.elearningtnd.*
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loader_layout.*
import kotlinx.android.synthetic.main.loader_layout.tv_hint_loading
import kotlinx.android.synthetic.main.loader_layout.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class LoadingModul : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        checkdata()
    }

    private fun checkdata(){
        loading.tv_hint_loading.text = "Update data..." //setting text hint progressbar
        //method volley buat login (POST data to PHP)
        @Suppress("UNUSED_ANONYMOUS_PARAMETER") val strReq: StringRequest =
            object : StringRequest(
                Method.POST,
                "https://palmsentry.srs-ssms.com/test_modul_md5.php",
                Response.Listener { response ->
                    try {
                        val jObj = JSONObject(response)
                        val hex = jObj.getString("hex")
                        val mainCheck = try {
                            UpdateMan().md5Checksum(this.getExternalFilesDir(null)?.absolutePath + "/MAIN/data_modul.json")
                        }catch (e:Exception){
                            ""
                        }
                        // Check for error node in json
                        if (hex == mainCheck) {
                            Log.d("yt","loading sama")
                            loadFile()
                        } else {
                            Log.d("yt","loading beda")
                            val fDelete = File(this.getExternalFilesDir(null)?.absolutePath + "/MAIN/data_modul.json")
                            if (fDelete.exists()) {
                                Log.d("yt","loading deleted")
                                fDelete.delete()
                            }
                            loadFile()
                        }
                    } catch (e: JSONException) {
                        AlertDialogUtility.withSingleAction(
                            this, "Ulang", "Data error, hubungi pengembang: $e", "warning.json"
                        ) {
                            val intent = Intent(this, MainMenu::class.java)
                            startActivity(intent)
                        }
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    AlertDialogUtility.withSingleAction(
                        this,
                        "Ulang",
                        "Terjadi kesalahan koneksi",
                        "network_error.json"
                    ) {
                    val intent = Intent(this, ModulDigital::class.java)
                    startActivity(intent)
                    }
                }) {
                override fun getParams(): Map<String, String> {
                    return HashMap()
                }
            }
        Volley.newRequestQueue(this).add(strReq)
    }

    private fun loadAnim() {
        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsLoading)

        lottieLoading.setAnimation("loading_circle.json")
        lottieLoading.loop(true)
        lottieLoading.playAnimation()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun loadFile() {
        val viewType = intent.getStringExtra("ViewType")
        Log.d("yt","loading $viewType")
        val filePath = this.getExternalFilesDir(null)?.absolutePath + "/MAIN/"
        val f = File(filePath + "data_modul.json")
        if ((!f.exists())) {
            loadAnim()
            val config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30000)
                .setConnectTimeout(30000)
                .build()
            PRDownloader.initialize(applicationContext, config)
            val url = "https://palmsentry.srs-ssms.com/data_modul.json"
            @Suppress("UNUSED_VARIABLE") val downloadId = PRDownloader.download(url, filePath, "data_modul.json")
                .build()
                .setOnStartOrResumeListener { }
                .setOnPauseListener { }
                .setOnCancelListener { }
                .setOnProgressListener { progress ->
                    val progressPercent: Long = progress.currentBytes * 100 / progress.totalBytes
                    progressBarOne.progress = progressPercent.toInt()
                    textViewProgressOne.text =
                        "${getBytesToMBString(progress.currentBytes)} / ${getBytesToMBString(
                            progress.totalBytes
                        )}"
                    progressBarOne.isIndeterminate = false
                }
                .start(object : OnDownloadListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDownloadComplete() {
                        Log.d("yt","loading dl komplit")
                        val intent = Intent(this@LoadingModul, ListModul::class.java)
                        intent.putExtra("ViewType", viewType)
                        intent.putExtra("network", "Online")
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                    override fun onError(error: com.downloader.Error?) {
                        Log.d("yt","loading dl error")
                        offlineLoading()
                    }
                })
        } else if (f.exists()) {
            //Log.d("yt","loading ke list video")
            val intent = Intent(this@LoadingModul, ListModul::class.java)
            intent.putExtra("ViewType", viewType)
            intent.putExtra("network", "Online")
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun getBytesToMBString(bytes: Long): String? {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun offlineLoading() {
        val intent = Intent(this@LoadingModul, ListModul::class.java)
        val viewType = intent.getStringExtra("ViewType")
        intent.putExtra("ViewType", viewType)
        intent.putExtra("network", "Offline")
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        AlertDialogUtility.alertDialog(this, "Mohon tunggu...", "wait.json")
    }
}
