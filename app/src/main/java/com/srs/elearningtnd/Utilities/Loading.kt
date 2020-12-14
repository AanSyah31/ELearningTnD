@file:Suppress("DEPRECATION")

package com.srs.elearningtnd.Utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.srs.elearningtnd.R
import com.srs.elearningtnd.ListVideo
import kotlinx.android.synthetic.main.loader_layout.*
import java.io.File
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class Loading : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        if (intent != null) {
            val viewType = intent.getStringExtra("ViewType")
            val viewActivity = intent.getStringExtra("View")

            if (viewType == "db_softskill" && viewActivity == null) {
                loadFile(Database.data_softSkill, ListVideo::class.java, "View", "test", "")
            } else if (viewType == "db_softskill" && viewActivity != null) {
                loadFile(Database.data_softSkill, ListVideo::class.java, "View", viewActivity, "")
            }
            else if (viewType == "db_estate" && viewActivity == null) {
                loadFile(Database.data_estate, ListVideo::class.java, "View", "test", "")
            } else if (viewType == "db_estate" && viewActivity != null) {
                loadFile(Database.data_estate, ListVideo::class.java, "View", viewActivity, "")
            }
            else if (viewType == "db_admin" && viewActivity != null) {
                loadFile(Database.data_admin, ListVideo::class.java, "View", "db_admin", viewActivity)
            } else if (viewType == "db_admin" && viewActivity != null && viewActivity != "update") {
                loadFile(Database.data_admin, ListVideo::class.java, "View", "db_admin", viewActivity)
            }
            else if (viewType == "db_mill" && viewActivity != null) {
                loadFile(Database.data_mill, ListVideo::class.java, "View", "db_mill", viewActivity)
            } else if (viewType == "db_mill" && viewActivity != null && viewActivity != "update") {
                loadFile(Database.data_mill, ListVideo::class.java, "View", "db_mill", viewActivity)
            }
            else if (viewType == "db_traksi" && viewActivity != null) {
                loadFile(Database.data_traksi, ListVideo::class.java, "View", "db_traksi", viewActivity)
            } else if (viewType == "db_traksi" && viewActivity != null && viewActivity != "update") {
                loadFile(Database.data_traksi, ListVideo::class.java, "View", "db_traksi", viewActivity)
            }
            else if (viewType == "db_supporting" && viewActivity != null) {
                loadFile(Database.data_supporting, ListVideo::class.java, "View", "db_supporting", viewActivity)
            } else if (viewType == "db_supporting" && viewActivity != null && viewActivity != "update") {
                loadFile(Database.data_supporting, ListVideo::class.java, "View", "db_supporting", viewActivity)
            }
            else {
                Log.d("viewtype", "NOT OK")
            }
        }
    }

    fun loadAnim(){
        Glide.with(this)
                .load(R.drawable.logo_png_white)
                .into(logo_ssmsLoading)

        lottieLoading.setAnimation("loading_circle.json")
        lottieLoading.loop(true)
        lottieLoading.playAnimation()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun loadFile(urlCategory: String, destination: Class<*>, viewActivity: String, charViewActivity: String, search: String?){
        val filePath = this.getExternalFilesDir(null)?.absolutePath + "/MAIN/"
        val f = File(filePath+urlCategory)
        if ((!f.exists())) {
            loadAnim()
            val config = PRDownloaderConfig.newBuilder()
                    .setReadTimeout(30000)
                    .setConnectTimeout(30000)
                    .build()
            PRDownloader.initialize(applicationContext, config)
            val zipFile = "$urlCategory.zip"
            val url = "${Database.server}/files/$urlCategory".subSequence(0, ("${Database.server}files/$urlCategory".length-3)).toString() + ".zip"
            Log.d("testzip","zipfile: $zipFile || url: $url")
            @Suppress("UNUSED_VARIABLE") val downloadId = PRDownloader.download(url, filePath, zipFile)
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .setOnProgressListener { progress ->
                        val progressPercent: Long = progress.currentBytes * 100 / progress.totalBytes
                        progressBarOne.progress = progressPercent.toInt()
                        textViewProgressOne.text = "${getBytesToMBString(progress.currentBytes)} / ${getBytesToMBString(progress.totalBytes)}"
                        progressBarOne.isIndeterminate = false
                    }
                    .start(object : OnDownloadListener {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onDownloadComplete() {
                            try {
                                FileMan().unzip(filePath+zipFile, filePath)
                            } finally {
                                File(filePath+zipFile).delete()
                                val intent = Intent(this@Loading, destination)
                                intent.putExtra(viewActivity, charViewActivity)
                                intent.putExtra("ViewType", "Online")
                                intent.putExtra("search", search)
                                startActivity(intent)
                                overridePendingTransition(0,0)
                            }
                        }
                        override fun onError(error: com.downloader.Error?) {
                            Log.d("testzip", error.toString())
                            offlineLoading(destination, viewActivity, charViewActivity, search)
                        }
                    })
        }
        else if(f.exists()){
            val intent = Intent(this@Loading, destination)
            intent.putExtra(viewActivity, charViewActivity)
            intent.putExtra("ViewType", "Online")
            intent.putExtra("search", search)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }

    private fun getBytesToMBString(bytes: Long): String? {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun offlineLoading(destination: Class<*>, viewActivity: String, charViewActivity: String, search: String?){
        val intent = Intent(this@Loading, destination)
        intent.putExtra(viewActivity, charViewActivity)
        intent.putExtra("ViewType", "Offline")
        intent.putExtra("search", search)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    override fun onBackPressed() {
        AlertDialogUtility.alertDialog(this, "Mohon tunggu...", "wait.json")
    }
}
