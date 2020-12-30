package com.srs.elearningtnd

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.srs.elearningtnd.Utilities.Loading
import com.srs.elearningtnd.Utilities.LoadingModul
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_modul_digital.*
import kotlinx.android.synthetic.main.list_menu.view.*

class ModulDigital : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modul_digital)

        val softskill = "Soft Skill"
        val estate = "Estate"
        val admin = "Admin"
        val mill = "Mill"
        val traksi = "Traksi"
        val supporting = "Supporting"

        cvSoftSkill.tvListMenu.text = softskill
        cvEstate.tvListMenu.text = estate
        cvAdmin.tvListMenu.text = admin
        cvMill.tvListMenu.text = mill
        cvTraksi.tvListMenu.text = traksi
        cvSupporting.tvListMenu.text = supporting

        cvSoftSkill.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_soft_skill))
        cvEstate.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_estate))
        cvAdmin.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_admin))
        cvMill.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_mill))
        cvTraksi.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_traksi))
        cvSupporting.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_supporting))

        btSearchModul.setOnClickListener {
            searchModul()
        }

        etModulSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                searchModul()
                true
            } else {
                false
            }
        }

        cvSoftSkill.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", softskill)
            startActivity(intent)
        }
        cvSoftSkill.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", softskill)
            startActivity(intent)
        }
        cvEstate.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", estate)
            startActivity(intent)
        }
        cvEstate.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", estate)
            startActivity(intent)
        }
        cvAdmin.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", admin)
            startActivity(intent)
        }
        cvAdmin.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", admin)
            startActivity(intent)
        }
        cvMill.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", mill)
            startActivity(intent)
        }
        cvMill.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", mill)
            startActivity(intent)
        }
        cvTraksi.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", traksi)
            startActivity(intent)
        }
        cvTraksi.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", traksi)
            startActivity(intent)
        }
        cvSupporting.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", supporting)
            startActivity(intent)
        }
        cvSupporting.iv_modul.setOnClickListener {
            val intent = Intent(this@ModulDigital, LoadingModul::class.java)
            intent.putExtra("ViewType", supporting)
            startActivity(intent)
        }
    }

    fun searchModul(){
        val searchStr = etModulSearch.text.toString()
        if (searchStr.replace(" ","").isNotEmpty()){
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", searchStr)
            startActivity(intent)
        } else {
            Toasty.info(this, "Mohon isi kolom search sebelum melakukan pencarian").show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}