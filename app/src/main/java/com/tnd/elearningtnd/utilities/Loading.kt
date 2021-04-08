@file:Suppress("DEPRECATION")

package com.tnd.elearningtnd.utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.tnd.elearningtnd.List
import com.tnd.elearningtnd.MainMenu
import com.tnd.elearningtnd.R
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.loader_layout.*
import kotlinx.android.synthetic.main.loader_layout.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class Loading : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        checkData()
    }

    private fun checkData(){
        loading.tv_hint_loading.text = "Update data..." //setting text hint progressbar
        //method volley buat login (POST data to PHP)
        @Suppress("UNUSED_ANONYMOUS_PARAMETER") val strReq: StringRequest =
            object : StringRequest(
                Method.POST,
                "https://e-learning.tnd-ssms.com/md5_elearning.php",
                Response.Listener { response ->
                    try {
                        val jObj = JSONObject(response)
                        val hex = jObj.getString("hex")
                        val mainCheck = try {
                            UpdateMan().md5Checksum(this.getExternalFilesDir(null)?.absolutePath + "/MAIN/data_modul.json")
                        } catch (e: Exception) {
                            ""
                        }
                        // Check for error node in json
                        if (hex == mainCheck) {
                            Log.d("yt", "loading sama")
                            loadFile()
                        } else {
                            Log.d("yt", "loading beda")
                            val fDelete =
                                File(this.getExternalFilesDir(null)?.absolutePath + "/MAIN/data_modul.json")
                            if (fDelete.exists()) {
                                Log.d("yt", "loading deleted")
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
                        "Terjadi kesalahan koneksi (loading)",
                        "network_error.json"
                    ) {
                        loadFile()
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
        val filePath = this.getExternalFilesDir(null)?.absolutePath + "/MAIN/"
        val f = File(filePath + "data_modul.json")
        if ((!f.exists())) {
            loadAnim()
            val config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30000)
                .setConnectTimeout(30000)
                .build()
            PRDownloader.initialize(applicationContext, config)
            val url = "https://e-learning.tnd-ssms.com/data_modul.json"
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
                        loadingIntentMgr("Online")
                    }
                    override fun onError(error: com.downloader.Error?) {
                        Log.d("yt","loading dl error")
                        offlineLoading()
                    }
                })
        } else if (f.exists()) {
            loadingIntentMgr("Online")
        }
    }

    private fun getBytesToMBString(bytes: Long): String? {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun offlineLoading() {
        loadingIntentMgr("Offline")
    }

    private fun loadingIntentMgr(network: String){
        val media = intent.getStringExtra("media")
        val kategori = intent.getStringExtra("kategori")
        val search = intent.getStringExtra("search")
        Log.d("testloading", "loading media intent: $media")
        Log.d("testloading", "loading kat intent: $kategori")
        Log.d("network","Intent test $network")
        intent = Intent(this, List::class.java)
        intent.putExtra("kategori", kategori)
        intent.putExtra("network", network)
        intent.putExtra("search", search)
        intent.putExtra("media", media)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        AlertDialogUtility.alertDialog(this, "Mohon tunggu...", "wait.json")
    }
}
