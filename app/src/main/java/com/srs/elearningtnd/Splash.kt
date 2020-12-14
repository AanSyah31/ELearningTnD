package com.srs.elearningtnd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashDelay()
        animasiSplash()
        tampilanSplash()
    }

    private fun tampilanSplash(){
        //Glide icon
        val icon = findViewById<ImageView>(R.id.ivLogo)
        Glide.with(this).load(R.drawable.elearning_green).into(icon)

        //membuat activity menjadi fullscreen
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun animasiSplash(){
        //animasi parent dgn fade in
        YoYo.with(Techniques.FadeIn)
            .duration(1000)
            .repeat(0)
            .playOn(findViewById(R.id.anim))
    }

    //Memindahkan activity dengan delay
    private fun splashDelay(){
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(Intent(this@Splash, Login::class.java))
                    finish()
                }
            }
        }
        thread.start()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}