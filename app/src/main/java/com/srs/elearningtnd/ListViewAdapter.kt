package com.srs.elearningtnd

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File


class ListViewAdapter(
    private val context: Activity,
    private val id: Array<Int>,
    private val judul: Array<String>,
    private val tag: Array<String>,
    private val link: Array<String>,
    private val media: Array<String>,
    private val thumbnail: Array<String>,
    private val kategori: Array<String>
)
    : ArrayAdapter<String>(
    context,
    R.layout.row,
    judul
) {

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.row, null, true)
        val image = rowView.findViewById(R.id.iv_modul) as ImageView
        val tvJudul = rowView.findViewById(R.id.tv_judulModul) as TextView
        val tvTag = rowView.findViewById(R.id.tv_tagModul) as TextView

        val image_media = rowView.findViewById(R.id.iv_media) as ImageView
        val image_info = rowView.findViewById(R.id.iv_info) as ImageView

        //image.maxHeight = image.width / 2
        //Glide.with(context).load("https://palmsentry.srs-ssms.com/thumbnail/${thumbnail[position]}.jpg").into(image)

        Glide.with(context)
            .load("https://e-learning.tnd-ssms.com/thumbnail/${thumbnail[position]}.jpg")
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .centerCrop()
            .into(image)

        //val yt = rowView.findViewById(R.id.id_youtube) as YouTubePlayerView


        when (media[position]) {
            "video" -> {
                image.setOnClickListener {
                    val intent = Intent(context, VideoYT::class.java)
                    intent.putExtra("link", link[position])
                    context.startActivity(intent)
                }
                image_media.visibility = View.VISIBLE
                Glide.with(context)
                    .load(R.drawable.ic_video)
                    .into(image_media)
            }
            "pdf" -> {
                image.setOnClickListener {
                val intent = Intent(context, ViewPDF::class.java)
                intent.putExtra("link", link[position])
                context.startActivity(intent)
                }
                image_media.visibility = View.VISIBLE
                Glide.with(context)
                    .load(R.drawable.ic_pdf)
                    .into(image_media)
                val dir = context.getExternalFilesDir(null)?.absolutePath!!.dropLast(6)
                val fileName = "$dir/${link[position]}/${link[position]}.pdf"
                val f = File(fileName)
                //Log.d("cekfile", "nama file $fileName")
                if (f.exists()) {
                    //Log.d("cekfile", "file ada${link[position]}")
                    image_info.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(R.drawable.ic_checklist)
                        .into(image_info)
                } else {
                    image_info.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(R.drawable.ic_download)
                        .into(image_info)
                    //Log.d("cekfile", "file gak ada ${link[position]}")
                }
            }
            else -> {

            }
        }

        tvJudul.text = judul[position]
        tvTag.text = tag[position]
        /*yt.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val video = link[position]
                youTubePlayer.cueVideo(video, 0f) }
        })
        yt.isEnabled = false*/
        return rowView
    }

}