package com.srs.elearningtnd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.srs.elearningtnd.Utilities.AlertDialogUtility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_modul_digital.*

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardMenuModul.setOnClickListener {
            val intent = Intent(this@MainMenu, ModulDigital::class.java)
            startActivity(intent)
        }

        tentangKamiMenu.setOnClickListener {
            val intent = Intent(this@MainMenu, TentangKami::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        AlertDialogUtility.withTwoActions(this, "Batal", "Keluar", "Apakah anda yakin untuk keluar dari apikasi E-Learning?", "warning.json"){
            finishAffinity()
        }
    }
}