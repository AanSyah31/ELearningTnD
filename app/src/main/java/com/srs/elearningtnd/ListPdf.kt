@file:Suppress("DEPRECATION")

package com.srs.elearningtnd

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.srs.elearningtnd.utilities.AlertDialogUtility
import com.srs.elearningtnd.utilities.FileMan
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_listview.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ListPdf : AppCompatActivity() {

    var i = 0
    private var arrayList = ArrayList<ModelListModul>()
    private var arrayListFilter = ArrayList<ModelListModul>()
    private var judul = ArrayList<String>()
    private var tag = ArrayList<String>()
    var id = ArrayList<Int>()
    private var namaFile = ArrayList<String>()
    lateinit var userDetail: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsList)

        lottieList.setAnimation("loading_circle.json")
        lottieList.loop(true)
        lottieList.playAnimation()

        val network = intent.getStringExtra("network")
        val kategori = intent.getStringExtra("kategori")
        Log.d("testloading", "list pdf net: $network")
        Log.d("testloading", "list pdf kat: $kategori")
        et_list.setText(kategori)
        FileMan().deleteFiles("CACHE", this)
        Log.d("yt", "list masuk list")
        Log.d("yt", "list $network")
        et_list.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s=="") {
                    Log.d("search","text changed kosong")
                    makeList("")
                } else {
                    makeList(et_list.text.toString())
                }
            }
        })
        if (intent != null) {
            if (network == "Online") {
                if (intent != null) {
                    try {
                        online()
                    } catch (e: Exception){
                        //Toasty.error(this, "Database online error! $e").show()
                        try {
                            offline()
                        } catch (e: Exception){
                            Toasty.error(this, "Database offline error! $e").show()
                            val intent = Intent(this, MainMenu::class.java)
                            startActivity(intent)
                        }
                    }
                }
                Log.d("network", "Online")
            } else if (network == "Offline") {
                if (intent != null) {
                    linear_listView.visibility = View.GONE
                    AlertDialogUtility.alertDialog(
                        this,
                        "Hubungkan Perangkat anda ke jaringan yang stabil untuk memperbaharui data!",
                        "network_error.json"
                    )
                    try {
                        online()
                    } catch (e: Exception){
                        Toasty.error(this, "Database offline error! $e").show()
                        val intent = Intent(this, MainMenu::class.java)
                        startActivity(intent)
                    }
                }
                Log.d("network", "Offline")
            }
        }

        bt_back.setOnClickListener {
            val intent = Intent(this@ListPdf, ModulDigital::class.java)
            startActivity(intent)
        }
    }

    private fun makeList(search: String){
        val s = search.toLowerCase(Locale.getDefault())
        arrayListFilter = arrayList
        val arrayJudul = ArrayList<String>()
        val arrayTag = ArrayList<String>()
        val arrayNamaFile = ArrayList<String>()
        val arrayId = ArrayList<Int>()
        Log.d("search","isi filter ${arrayList.toTypedArray().contentToString()}")

        for((index, e) in arrayListFilter.withIndex()){
            if (search == ""){
                arrayJudul.add(e.judul)
                arrayTag.add(e.tag)
                arrayNamaFile.add(e.namaFile)
                arrayId.add(e.id)
            } else if (search != "" && e.judul.toLowerCase(Locale.getDefault()).contains(s) || e.tag.toLowerCase(Locale.getDefault()).contains(s)){
                arrayJudul.add(e.judul)
                arrayTag.add(e.tag)
                arrayNamaFile.add(e.namaFile)
                arrayId.add(e.id)
            }
        }
        //creating custom ArrayAdapter
        val myListAdapter = ListViewAdapterModul(
            this,
            arrayId.toTypedArray(),
            arrayJudul.toTypedArray(),
            arrayTag.toTypedArray(),
            arrayNamaFile.toTypedArray()
        )
        listView?.adapter = myListAdapter
    }

    private fun online(){
        when (intent.getStringExtra("kategori")!!) {
            "Soft Skill" -> {
                parseJSONdata("Soft Skill")
            }
            "Estate" -> {
                parseJSONdata("Estate")
            }
            "Admin" -> {
                parseJSONdata("Admin")
            }
            "Mill" -> {
                parseJSONdata("Mill")
            }
            "Traksi" -> {
                parseJSONdata("Traksi")
            }
            "Supporting" -> {
                parseJSONdata("Supporting")
            }
            else -> {
                parseJSONdata("semua")
            }
        }
    }

    private fun offline(){
        when (intent.getStringExtra("kategori")) {
            "Soft Skill" -> {
                parseJSONdata("Soft Skill")
            }
            "Estate" -> {
                parseJSONdata("Estate")
            }
            "Admin" -> {
                parseJSONdata("Admin")
            }
            "Mill" -> {
                parseJSONdata("Mill")
            }
            "Traksi" -> {
                parseJSONdata("Traksi")
            }
            "Supporting" -> {
                parseJSONdata("Supporting")
            }
            else -> {
                parseJSONdata("semua")
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ModulDigital::class.java)
        startActivity(intent)
    }

    private fun parseJSONdata(string: String) {
        Log.d("modul","list $string")
        // since we have JSON object, so we are getting the object
        //here we are calling a function and that function is returning the JSON object
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val obj: JSONObject = try {
            JSONObject(FileMan().onlineInputStream(this))
        }catch (e:Exception){
            JSONObject(FileMan().offlineInputStream(this))
        }
        // fetch JSONArray named users by using getJSONArray
        val userArray = obj.getJSONArray("db_modul")
        //Log.d("yt","userArray: $userArray")
        // implement for loop for getting users data i.e. name, email and contact
        for (i in 0 until userArray.length()) {
            // create a JSONObject for fetching single user data
            userDetail = userArray.getJSONObject(i)
            val kategori = try {
                userDetail.getString("kategori")
            } catch (e: Exception) {
                "0"
            }
            val media = try {
                userDetail.getString("db_media")
            } catch (e: Exception) {
                "0"
            }
            //Log.d("yt", "list s $s")
            if (kategori == string && media == "pdf"){
                et_list.isEnabled = false
                isiArray()
            } else if (string == "semua"){
                //Log.d("yt", "list else semua")
                isiArray()
            }
        }
        if (arrayList.isNotEmpty()){
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val thread = Thread {
                runOnUiThread{
                    if (string == "semua"){
                        makeList(intent.getStringExtra("kategori"))
                    }else{
                        search_vid.visibility = View.GONE
                        makeList("")
                    }
                }
            }
            thread.start()
        }
        Log.d("debugList", "id: $id")
        Log.d("debugList", "judul: $judul")
        Log.d("debugList", "tag: $tag")
        Log.d("debugList", "videoId: $namaFile")
    }

    private fun isiArray(){
        val j = try {
            userDetail.getString("judul")
        } catch (e: Exception) {
            "0"
        }
        //Log.d("yt", "list j $j")
        judul.add(j)
        val t = try {
            var arrayListStr = ArrayList<String>()
            val string = userDetail.getString("db_tag")
            val jSONArray = JSONArray(string)
            for (i in 0 until jSONArray.length()){
                arrayListStr.add(jSONArray.getJSONObject(i).getString("tag"))
            }
            arrayListStr.toTypedArray().contentToString()
        } catch (e: Exception) {
            "0"
        }
        tag.add(t)
        val d = try {
            userDetail.getString("id").toInt()
        } catch (e: Exception) {
            0
        }
        id.add(d)
        val v = try {
            userDetail.getString("link")
        } catch (e: Exception) {
            "0"
        }
        namaFile.add(v)
        Log.d("debugList", "judul=$j || tag=$t || id=$d || namaFile=$v")

        Log.d("debugList", "id: $d")
        Log.d("debugList", "judul: $j")
        Log.d("debugList", "tag: $t")
        Log.d("debugList", "namaFile: $v")

        arrayList.add(ModelListModul(d, j, t, v))
    }
}