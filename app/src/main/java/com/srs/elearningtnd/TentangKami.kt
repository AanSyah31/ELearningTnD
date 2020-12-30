package com.srs.elearningtnd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class TentangKami : AppCompatActivity(), View.OnClickListener {

    private var library: CardView? = null
    private var sawitpedia: CardView? = null
    private var palmsentry: CardView? = null
    private var infoaplikasi: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_kami)

        library = findViewById<View>(R.id.library_card) as CardView
        sawitpedia = findViewById<View>(R.id.sawitpedia_card) as CardView
        palmsentry = findViewById<View>(R.id.palmsentry_card) as CardView
        infoaplikasi = findViewById<View>(R.id.infoaplikasi_card) as CardView

        library!!.setOnClickListener(this)
        sawitpedia!!.setOnClickListener(this)
        palmsentry!!.setOnClickListener(this)
        infoaplikasi!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        val i: Intent
        when (v.id) {
            R.id.library_card -> {
                Toast.makeText(applicationContext, "Sedang Memuat...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=srs.app.bukusaku")))
            }
            R.id.sawitpedia_card -> {
                Toast.makeText(applicationContext, "Sedang Memuat...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.srs.sidewinderz.ensiklopediasrs")))
            }
            R.id.palmsentry_card -> {
                Toast.makeText(applicationContext, "Sedang Memuat...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.srs.palmsentry")))
            }

            R.id.infoaplikasi_card -> {
                i = Intent(this, InfoAplikasi::class.java)
                startActivity(i)
            }
        }
    }
}