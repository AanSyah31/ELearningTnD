@file:Suppress("DEPRECATION")

package com.srs.elearningtnd.Utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.srs.elearningtnd.ListVideo
import com.srs.elearningtnd.R
import kotlinx.android.synthetic.main.loader_layout.*
import java.io.File
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class Loading : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        if (intent != null) {
            loadFile()
        }
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
        val filePath = this.getExternalFilesDir(null)?.absolutePath + "/MAIN/"
        val f = File(filePath + "data_youtube.json")
        if ((!f.exists())) {
            loadAnim()
            val config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30000)
                .setConnectTimeout(30000)
                .build()
            PRDownloader.initialize(applicationContext, config)
            val url = "https://palmsentry.srs-ssms.com/data_youtube.json"
            @Suppress("UNUSED_VARIABLE") val downloadId = PRDownloader.download(url, filePath, "data_youtube.json")
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
                        val intent = Intent(this@Loading, ListVideo::class.java)
                        intent.putExtra("ViewType", viewType)
                        intent.putExtra("network", "Online")
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                    override fun onError(error: com.downloader.Error?) {
                        offlineLoading()
                    }
                })
        } else if (f.exists()) {
            val intent = Intent(this@Loading, ListVideo::class.java)
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
        val intent = Intent(this@Loading, ListVideo::class.java)
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
