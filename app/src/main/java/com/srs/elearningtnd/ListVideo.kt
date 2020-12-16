@file:Suppress("DEPRECATION")

package com.srs.elearningtnd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.srs.elearningtnd.Utilities.AlertDialogUtility
import com.srs.elearningtnd.Utilities.Database
import com.srs.elearningtnd.Utilities.FileMan
import com.srs.elearningtnd.Utilities.UpdateMan
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_listview.*
import org.json.JSONObject

class ListVideo : AppCompatActivity() {

    var listViewV: ListView? = null
    var i = 0
    var search: String? = ""
    var arrayList = ArrayList<ModelList>()
    var judul = ArrayList<String>()
    var tag = ArrayList<String>()
    var id = ArrayList<Int>()
    var videoId = ArrayList<String>()
    lateinit var userDetail: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        listViewV = findViewById(R.id.listView)

        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsList)

        lottieList.setAnimation("loading_circle.json")
        lottieList.loop(true)
        lottieList.playAnimation()

        val network = intent.getStringExtra("network")
        val vt = intent.getStringExtra("ViewType")
        tv_list.text = vt
        FileMan().deleteFiles("CACHE", this)

        if (intent != null) {
            if (network == "Online") {
                if (intent != null) {
                    try {
                        online()
                    } catch (e:Exception){
                        //Toasty.error(this, "Database online error! $e").show()
                        try {
                            offline()
                        } catch (e:Exception){
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
                    AlertDialogUtility.alertDialog(this, "Hubungkan Perangkat anda ke jaringan yang stabil untuk memperbaharui data!", "network_error.json")
                    try {
                        online()
                    } catch (e:Exception){
                        Toasty.error(this, "Database offline error! $e").show()
                        val intent = Intent(this, MainMenu::class.java)
                        startActivity(intent)
                    }
                }
                Log.d("network", "Offline")
            }
        }

        bt_back.setOnClickListener {
            val intent = Intent(this@ListVideo, ModulDigital::class.java)
            startActivity(intent)
        }
    }

    private fun makeList(){
        val arrayJudul = Array(arrayList.size){"null"}
        val arrayTag = Array(arrayList.size){"null"}
        val arrayVideoId = Array(arrayList.size){"null"}
        val arrayId = Array(arrayList.size){0}
        for((index, e) in arrayList.withIndex()){
            arrayJudul[index] = e.judul
            arrayTag[index] = e.tag
            arrayVideoId[index] = e.videoId
            arrayId[index] = e.id
        }
        //creating custom ArrayAdapter
        val myListAdapter = ListViewAdapter(
            this,
            arrayId,
            arrayJudul,
            arrayTag,
            arrayVideoId
        )
        listView?.adapter = myListAdapter
    }

    private fun online(){
        when (intent.getStringExtra("ViewType")!!) {
            "Soft Skill" -> {
                Log.d("debugList", "soft_skill")
                parseJSONdata("Soft Skill")
                /*bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_softSkill, this, bt_update, tv_tanggal, progressBarHolderListView, "Soft Skill", "update")
                }*/
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
                print("x is neither 1 nor 2")
            }
        }
    }

    private fun offline(){
        when (intent.getStringExtra("ViewType")) {
            "soft_skill" -> {
                parseJSONdata("db_softskill")
            }
            "estate" -> {
                parseJSONdata("db_estate")
            }
            "admin" -> {
                parseJSONdata("db_admin")
            }
            "mill" -> {
                parseJSONdata("db_mill")
            }
            "traksi" -> {
                parseJSONdata("db_traksi")
            }
            "supporting" -> {
                parseJSONdata("db_supporting")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        /*searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                val filterChar: List<String>
                if (TextUtils.isEmpty(s)) {
                    adapter?.filter(filterChar)
                    listView!!.clearTextFilter()
                } else {
                    adapter!!.filter(s)
                }
                return true
            }
        })*/
        searchView.setQuery(search, true)
        Log.d("search", search!!)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, ModulDigital::class.java)
        startActivity(intent)
    }

    private fun parseJSONdata(string: String) {
        // since we have JSON object, so we are getting the object
        //here we are calling a function and that function is returning the JSON object
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val obj: JSONObject = try {
            JSONObject(FileMan().onlineInputStream(this))
        }catch (e:Exception){
            JSONObject(FileMan().offlineInputStream(this))
        }
        // fetch JSONArray named users by using getJSONArray
        val userArray = obj.getJSONArray("db_youtube")
        Log.d("debugList","userArray: $userArray")
        // implement for loop for getting users data i.e. name, email and contact
        for (i in 0 until userArray.length()) {
            // create a JSONObject for fetching single user data
            userDetail = userArray.getJSONObject(i)
            Log.d("debugList","userDetail: $userDetail")
            val s = try {
                userDetail.getString("kategori")
            } catch (e: Exception) {
                "0"
            }
            if (s == string){
                val j = try {
                    userDetail.getString("judul")
                } catch (e: Exception) {
                    "0"
                }
                judul.add(j)
                val t = try {
                    userDetail.getString("tag")
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
                    userDetail.getString("video_id")
                } catch (e: Exception) {
                    "0"
                }
                videoId.add(v)
                Log.d("debugList", "judul=$j || tag=$t || id=$d || video=$v")

                //bind all strings in an array
                arrayList.add(ModelList(id[i], judul[i], tag[i], videoId[i]))
            }

            /*
            //pass results to listViewAdapter class
            adapter = ListViewAdapter(this, id, judul, tag, videoId)
            //bind the adapter to the listview
            listView?.adapter = adapter
            */
        }
        if (arrayList.isNotEmpty()){
            val thread = Thread {
                runOnUiThread{
                    makeList()
                }
            }
            thread.start()
        }
        Log.d("debugList","id: $id")
        Log.d("debugList","judul: $judul")
        Log.d("debugList","tag: $tag")
        Log.d("debugList","videoId: $videoId")
    }
}