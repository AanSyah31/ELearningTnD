package com.srs.elearningtnd

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.PDFView.Configurator
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.github.chrisbanes.photoview.PhotoView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import es.dmoral.toasty.Toasty
import java.io.File


class ViewPDF : AppCompatActivity() {

    var pdfView: PDFView? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_p_d_f)

        pdfView = findViewById(R.id.pdf_viewer)
        progressBar = findViewById(R.id.progress_bar)

        YoYo.with(Techniques.FadeIn)
            .duration(1500)
            .repeat(0)
            .playOn(findViewById(R.id.anim))

        if (intent != null) {
            val namaFile = intent.getStringExtra("link")
            if (namaFile != null || !TextUtils.isEmpty(namaFile)) {
                setContentPDF(
                        "https://palmsentry.srs-ssms.com/modul/$namaFile",
                        "Android/data/com.srs.elearningtnd/$namaFile"
                    )
            }
        }
    }

    fun setContentPDF(http: String?, directory: String?) {
        Log.d("modul", "http: $http")
        Log.d("modul", "directory: $directory")
        pdfView!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE //Show Progress Bar
        Toasty.info(this@ViewPDF, "Sedang mengunduh/memuat...", Toast.LENGTH_SHORT, true)
            .show()
        FileLoader.with(this)
            .load("${http}.pdf")
            .fromDirectory(directory, FileLoader.DIR_INTERNAL)
            .checkFileintegrity(true)
            .asFile(object : FileRequestListener<File?>() {
                override fun onLoad(
                    fileLoadRequest: FileLoadRequest?,
                    fileResponse: FileResponse<File?>
                ) {
                    progressBar!!.visibility = View.GONE
                    val pdfFile: File? = fileResponse.getBody()
                    pdfView!!.fromFile(pdfFile)
                        .password(null)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(true)
                        .enableDoubletap(true)
                        .enableAnnotationRendering(true)
                        .enableAntialiasing(true)
                        .pageFling(true)
                        .pageFitPolicy(FitPolicy.HEIGHT)
                        .pageSnap(true)
                        .autoSpacing(true)
                        .load()
                }

                override fun onError(
                    fileLoadRequest: FileLoadRequest?,
                    throwable: Throwable?
                ) {
                    Toasty.error(
                        this@ViewPDF,
                        "Download gagal, mohon cek koneksi..",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            })
    }

    fun setLocalPDF(asset: String?) {
        pdfView!!.visibility = View.VISIBLE
        pdfView!!.fromAsset(asset)
            .enableSwipe(true)
            .swipeHorizontal(true)
            .enableAntialiasing(true)
            .pageFling(true)
            .load()
    }

    fun setImg(http: String?, directory: String?) {
        progressBar!!.visibility = View.VISIBLE
        Toasty.info(this@ViewPDF, "Sedang mengunduh/memuat...", Toast.LENGTH_SHORT, true)
            .show()
        FileLoader.with(this)
            .load(http)
            .fromDirectory(directory, FileLoader.DIR_INTERNAL)
            .checkFileintegrity(true)
            .asFile(object : FileRequestListener<File?>() {
                @SuppressLint("CheckResult")
                override fun onLoad(
                    fileLoadRequest: FileLoadRequest?,
                    fileResponse: FileResponse<File?>
                ) {
                    progressBar!!.visibility = View.GONE
                }

                override fun onError(
                    fileLoadRequest: FileLoadRequest?,
                    throwable: Throwable?
                ) {
                    Toasty.error(
                        this@ViewPDF,
                        "Download gagal, mohon cek koneksi..",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            })
    }
}