package com.srs.elearningtnd

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class ListViewAdapter(
    private val context: Activity,
    private val id: Array<Int>,
    private val judul: Array<String>,
    private val tag: Array<String>,
    private val link: Array<String>
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
        //val yt = rowView.findViewById(R.id.id_youtube) as YouTubePlayerView
        image.setOnClickListener {
            val intent = Intent(context, VideoYT::class.java)
            intent.putExtra("video", link[position])
            context.startActivity(intent)
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