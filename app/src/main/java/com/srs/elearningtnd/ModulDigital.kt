package com.srs.elearningtnd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.srs.elearningtnd.Utilities.Loading
import kotlinx.android.synthetic.main.activity_modul_digital.*
import kotlinx.android.synthetic.main.list_menu.view.*

class ModulDigital : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modul_digital)

        softSkill.tvListMenu.text = "Soft Skill"
        estate.tvListMenu.text = "Estate"
        admin.tvListMenu.text = "Admin"
        mill.tvListMenu.text = "Mill"
        traksi.tvListMenu.text = "Traksi"
        supporting.tvListMenu.text = "Supporting"

        softSkill.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_soft_skill))
        estate.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_estate))
        admin.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_admin))
        mill.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_mill))
        traksi.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_traksi))
        supporting.iconListMenu.setImageDrawable(resources.getDrawable(R.drawable.rsz_supporting))

        tentangKamiModul.setOnClickListener {
            val intent = Intent(this@ModulDigital, TentangKami::class.java)
            startActivity(intent)
        }

        softSkill.cardMenu.setOnClickListener {
            val intent = Intent(this@ModulDigital, Loading::class.java)
            intent.putExtra("ViewType", "soft_skill")
            startActivity(intent)
        }
    }
}