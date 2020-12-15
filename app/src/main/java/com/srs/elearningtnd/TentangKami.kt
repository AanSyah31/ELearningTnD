package com.srs.elearningtnd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class TentangKami : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_kami)

        val icon = findViewById<ImageView>(R.id.menuModul)
        Glide.with(this).load(R.drawable.rsz_icon).into(icon)
    }
}