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


class List : AppCompatActivity() {

    var i = 0
    private var arrayList = ArrayList<ModelList>()
    private var arrayListFilter = ArrayList<ModelList>()

    private var judulList = ArrayList<String>()
    private var tagList = ArrayList<String>()
    private var idList = ArrayList<Int>()
    private var linkList = ArrayList<String>()
    private var mediaList = ArrayList<String>()
    private var thumbnailList = ArrayList<String>()

    private lateinit var userDetail: JSONObject

    private var mediaIntent = ""
    private var kategoriIntent = ""
    private var networkIntent = ""
    private var searchIntent = ""

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsList)

        lottieList.setAnimation("loading_circle.json")
        lottieList.loop(true)
        lottieList.playAnimation()

        networkIntent = intent.getStringExtra("network")
        kategoriIntent = try {
            intent.getStringExtra("kategori")
        } catch (e: Exception){
            ""
        }
        searchIntent = try {
            intent.getStringExtra("search")
        } catch (e: Exception){
            ""
        }
        mediaIntent = try {
            intent.getStringExtra("media")
        } catch (e: Exception) {
            ""
        }
        Log.d("network","Intent test $networkIntent")
        if (searchIntent == ""){
            et_list.setText(kategoriIntent)
        } else {
            et_list.setText(searchIntent)
        }
        FileMan().deleteFiles("CACHE", this)
        Log.d("yt", "list masuk list")
        Log.d("yt", "list $networkIntent")
        et_list.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == "") {
                    Log.d("search", "text changed kosong")
                    makeList("")
                } else {
                    makeList(et_list.text.toString())
                }
            }
        })
        if (intent != null) {
            if (networkIntent == "Online") {
                if (intent != null) {
                    try {
                        online()
                    } catch (e: Exception) {
                        //Toasty.error(this, "Database online error! $e").show()
                        try {
                            offline()
                        } catch (e: Exception) {
                            Toasty.error(this, "Database offline error! $e").show()
                            val intent = Intent(this, MainMenu::class.java)
                            startActivity(intent)
                        }
                    }
                }
                Log.d("network", "Online")
            } else if (networkIntent == "Offline") {
                if (intent != null) {
                    linear_listView.visibility = View.GONE
                    AlertDialogUtility.alertDialog(
                        this,
                        "Hubungkan Perangkat anda ke jaringan yang stabil untuk memperbaharui data!",
                        "network_error.json"
                    )
                    try {
                        online()
                    } catch (e: Exception) {
                        Toasty.error(this, "Database offline error! $e").show()
                        val intent = Intent(this, MainMenu::class.java)
                        startActivity(intent)
                    }
                }
                Log.d("network", "Offline")
            }
        }

        bt_back.setOnClickListener {
            val intent = Intent(this@List, ModulDigital::class.java)
            startActivity(intent)
        }
    }

    private fun makeList(search: String){
        val s = search.toLowerCase(Locale.getDefault())
        arrayListFilter = arrayList
        val arrayJudul = ArrayList<String>()
        val arrayTag = ArrayList<String>()
        val arrayVideoId = ArrayList<String>()
        val arrayId = ArrayList<Int>()
        val arrayMedia = ArrayList<String>()
        val arrayThumbnail = ArrayList<String>()
        val arrayKategori = ArrayList<String>()

        Log.d("search", "isi filter ${arrayList.toTypedArray().contentToString()}")

        for (e in arrayListFilter) {
            if (search == "") {
                arrayJudul.add(e.judul)
                arrayTag.add(e.tag)
                arrayVideoId.add(e.link)
                arrayId.add(e.id)
                arrayThumbnail.add(e.thumbnail)
                arrayMedia.add(e.media)
            } else if (search != "" && e.judul.toLowerCase(Locale.getDefault()).contains(s) || e.tag.toLowerCase(Locale.getDefault()).contains(s)) {
                arrayJudul.add(e.judul)
                arrayTag.add(e.tag)
                arrayVideoId.add(e.link)
                arrayId.add(e.id)
                arrayThumbnail.add(e.thumbnail)
                arrayMedia.add(e.media)
            }
        }
        //creating custom ArrayAdapter
        val myListAdapter = ListViewAdapter(
            this,
            arrayId.toTypedArray(),
            arrayJudul.toTypedArray(),
            arrayTag.toTypedArray(),
            arrayVideoId.toTypedArray(),
            arrayMedia.toTypedArray(),
            arrayThumbnail.toTypedArray(),
            arrayKategori.toTypedArray()
        )
        listView?.adapter = myListAdapter
    }

    private fun online(){
        Log.d("network","Intent online")
        when (kategoriIntent) {
            "Soft Skill" -> {
                Log.d("debugList", "soft_skill")
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
        Log.d("network","Intent offline")
        when (kategoriIntent) {
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
        Log.d("yt","list $string")
        // since we have JSON object, so we are getting the object
        //here we are calling a function and that function is returning the JSON object
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val obj: JSONObject = try {
            Log.d("network","Coba Online")
            JSONObject(FileMan().onlineInputStream(this))
        }catch (e:Exception){
            Log.d("network","Coba Offline")
            JSONObject(FileMan().offlineInputStream(this))
        }
        Log.d("yt","json object list video: $obj")
        // fetch JSONArray named users by using getJSONArray
        val userArray = obj.getJSONArray("db_modul")
        Log.d("yt","userArray: $userArray")
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
            if (kategori == string && media == mediaIntent){
                et_list.isEnabled = false
                isiArray()
            }else if (string == "semua"){
                //Log.d("yt", "list else semua")
                isiArray()
            }
        }
        if (arrayList.isNotEmpty()){
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val thread = Thread {
                runOnUiThread{
                    if (string == "semua") {
                        Log.d("searchtest","search intent=$searchIntent")
                        makeList(searchIntent)
                    } else {
                        Log.d("searchtest","search intent= kosong")
                        search_vid.visibility = View.GONE
                        makeList("")
                    }
                }
            }
            thread.start()
        }
        Log.d("debugList", "id: $idList")
        Log.d("debugList", "judul: $judulList")
        Log.d("debugList", "tag: $tagList")
        Log.d("debugList", "videoId: $linkList")
    }

    private fun isiArray(){
        val judul = try {
            userDetail.getString("judul")
        } catch (e: Exception) {
            "0"
        }
        Log.d("yt", "list j $judul")
        judulList.add(judul)

        val tag = try {
            val arrayListStr = ArrayList<String>()
            val string = userDetail.getString("db_tag")
            val jSONArray = JSONArray(string)
            for (i in 0 until jSONArray.length()){
                arrayListStr.add(jSONArray.getJSONObject(i).getString("tag"))
            }
            arrayListStr.toTypedArray().contentToString()
        } catch (e: Exception) {
            "0"
        }
        tagList.add(tag)

        val id = try {
            userDetail.getString("id").toInt()
        } catch (e: Exception) {
            0
        }
        idList.add(id)
        val link = try {
            userDetail.getString("link")
        } catch (e: Exception) {
            "0"
        }
        linkList.add(link)

        val thumbnail = try {
            userDetail.getString("thumbnail")
        } catch (e: Exception) {
            "0"
        }
        thumbnailList.add(thumbnail)

        val media = try {
            userDetail.getString("db_media")
        } catch (e: Exception) {
            "0"
        }
        mediaList.add(thumbnail)

        Log.d("debugList", "judul=$judul || tag=$tag || id=$id || video=$link")
        //bind all strings in an array
        arrayList.add(ModelList(id, link, thumbnail, judul, tag, media))

        //arrayList.filter { a -> !a.toString().contains("how") }
        //arrayList.removeIf { a -> !a.toString().contains("how") }
    }
}