package com.srs.elearningtnd

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
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

        cardMenuDll.setOnClickListener {
            val intent = Intent(this@MainMenu, TentangKami::class.java)
            startActivity(intent)
        }

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    super.onPermissionsChecked(report)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    super.onPermissionRationaleShouldBeShown(permissions, token)
                }
            }).check()
    }

    override fun onBackPressed() {
        AlertDialogUtility.withTwoActions(this, "Batal", "Keluar", "Apakah anda yakin untuk keluar dari apikasi E-Learning?", "warning.json"){
            finishAffinity()
        }
    }
}