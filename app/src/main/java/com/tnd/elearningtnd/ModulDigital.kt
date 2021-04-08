package com.tnd.elearningtnd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.tnd.elearningtnd.utilities.Loading
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_modul_digital.*
import kotlinx.android.synthetic.main.list_menu.view.*

class ModulDigital : AppCompatActivity() {

    private val softskill = "Soft Skill"
    private val estate = "Estate"
    private val admin = "Admin"
    private val mill = "Mill"
    private val traksi = "Traksi"
    private val supporting = "Supporting"

    private val vid = "video"
    private val pdf = "pdf"

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modul_digital)

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
        btOnClickMenu(cvSoftSkill, softskill)
        btOnClickMenu(cvEstate, estate)
        btOnClickMenu(cvAdmin, admin)
        btOnClickMenu(cvMill, mill)
        btOnClickMenu(cvTraksi, traksi)
        btOnClickMenu(cvSupporting, supporting)
    }

    private fun btOnClickMenu(view: View, kategori: String) {
        view.iv_youtube.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            Log.d("testloading", "click menu: video")
            Log.d("testloading", "click menu kat: $kategori")
            intent.putExtra("kategori", kategori)
            intent.putExtra("media", vid)
            startActivity(intent)
        }
        view.iv_pdf.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            Log.d("testloading", "click menu: pdf")
            Log.d("testloading", "click menu kat: $kategori")
            intent.putExtra("kategori", kategori)
            intent.putExtra("media", pdf)
            startActivity(intent)
        }
    }

    private fun searchModul() {
        val searchStr = etModulSearch.text.toString()
        if (searchStr.replace(" ", "").isNotEmpty()) {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("search", searchStr)
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