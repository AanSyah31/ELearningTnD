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


class ListViewAdapterModul(
    private val context: Activity,
    private val id: Array<Int>,
    private val judul: Array<String>,
    private val tag: Array<String>,
    private val link: Array<String>
)
    : ArrayAdapter<String>(
    context,
    R.layout.row_modul,
    judul
) {

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.row_modul, null, true)
        /*val icon = rowView.findViewById(R.id.iv_Icon) as ImageView*/
        val tvJudul = rowView.findViewById(R.id.tv_Title) as TextView
        val tvTag = rowView.findViewById(R.id.tv_Category) as TextView
        val id = rowView.findViewById(R.id.id_modul) as LinearLayout
        id.setOnClickListener {
            val intent = Intent(context, ViewPDF::class.java)
            intent.putExtra("link", link[position])
            context.startActivity(intent)
        }

        tvJudul.text = judul[position]
        tvTag.text = tag[position]

        return rowView
    }

}