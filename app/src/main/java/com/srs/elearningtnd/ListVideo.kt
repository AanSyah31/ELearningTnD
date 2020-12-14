package com.srs.elearningtnd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.srs.elearningtnd.Utilities.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_listview.*
import org.json.JSONObject

class ListVideo : AppCompatActivity() {

    var listView: ListView? = null
    var adapter: ListViewAdapter? = null
    var i = 0
    var search: String? = ""
    var arrayList = ArrayList<ModelList>()
    var judul = ArrayList<String>()
    var tag = ArrayList<String>()
    var id = java.util.ArrayList<String>()
    lateinit var userDetail: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soft_skill)

        listView = findViewById(R.id.listView)

        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsList)

        lottieList.setAnimation("loading_circle.json")
        lottieList.loop(true)
        lottieList.playAnimation()

        val viewType = intent.getStringExtra("ViewType")
        FileMan().deleteFiles("CACHE", this)

        if (intent != null) {
            if (viewType == "Online") {
                if (intent != null) {
                    try {
                        online()
                    } catch (e:Exception){
                        Toasty.error(this, "Database online error! $e").show()
                        try {
                            offline()
                        } catch (e:Exception){
                            Toasty.error(this, "Database offline error! $e").show()
                            val intent = Intent(this@ListVideo, Menu::class.java)
                            startActivity(intent)
                        }
                    }
                }
                Log.d("viewtype", "Online")
            } else if (viewType == "Offline") {
                if (intent != null) {
                    linear_listView.visibility = View.GONE
                    AlertDialogUtility.alertDialog(this, "Hubungkan Perangkat anda ke jaringan yang stabil untuk memperbaharui data!", "network_error.json")
                    try {
                        online()
                    } catch (e:Exception){
                        Toasty.error(this, "Database offline error! $e").show()
                        val intent = Intent(this, Menu::class.java)
                        startActivity(intent)
                    }
                }
                Log.d("viewtype", "Offline")
            }
        }
    }

    fun online(){
        when (intent.getStringExtra("View")!!) {
            "db_softskill" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_softSkill, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_softSkill, this, bt_update, tv_tanggal, progressBarHolderListView, "db_softskill", "update")
                }
            }
            "db_estate" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_estate, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_estate, this, bt_update, tv_tanggal, progressBarHolderListView, "db_estate", "update")
                }
            }
            "db_admin" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_admin, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_admin, this, bt_update, tv_tanggal, progressBarHolderListView, "db_admin", "update")
                }
            }
            "db_mill" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_mill, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_mill, this, bt_update, tv_tanggal, progressBarHolderListView, "db_mill", "update")
                }
            }
            "db_traksi" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_traksi, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_traksi, this, bt_update, tv_tanggal, progressBarHolderListView, "db_traksi", "update")
                }
            }
            "db_supporting" -> {
                parseJSONdata(FileMan().onlineInputStream(Database.data_supporting, this, tv_tanggal))
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_supporting, this, bt_update, tv_tanggal, progressBarHolderListView, "db_supporting", "update")
                }
            }
        }
    }

    fun offline(){
        when (intent.getStringExtra("View")!!) {
            "db_softskill" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_softSkill, this))
            }
            "db_estate" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_estate, this))
            }
            "db_admin" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_admin, this))
            }
            "db_mill" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_mill, this))
            }
            "db_traksi" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_traksi, this))
            }
            "db_supporting" -> {
                parseJSONdata(FileMan().offlineInputStream(Database.data_supporting, this))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (TextUtils.isEmpty(s)) {
                    adapter?.filter("")
                    listView!!.clearTextFilter()
                } else {
                    adapter!!.filter(s)
                }
                return true
            }
        })
        searchView.setQuery(search, true)
        Log.d("search", search!!)
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this, ModulDigital::class.java)
        startActivity(intent)
    }

    fun parseJSONdata(json: String?) {
        // since we have JSON object, so we are getting the object
        //here we are calling a function and that function is returning the JSON object
        val obj = JSONObject(json!!)
        // fetch JSONArray named users by using getJSONArray
        val userArray = obj.getJSONArray("Blok")
        // implement for loop for getting users data i.e. name, email and contact
        val userArrayLabel = obj.getJSONArray("DataLabelBlok")
        for (i in 0 until userArray.length()) {
            // create a JSONObject for fetching single user data
            userDetail = userArray.getJSONObject(i)

            val m = MathFunc()
            judul.add(m.arrayAdder(userDetail, 1))
            tag.add(m.arrayAdder(userDetail, 1))
            id.add(try {
                userDetail.getString("0")
            } catch (e: Exception) {
                "0"
            })

            val model = ModelList(id[i], judul[i], tag[i])
            //bind all strings in an array
            arrayList.add(model)

            //pass results to listViewAdapter class
            adapter = ListViewAdapter(this, arrayList)
            //bind the adapter to the listview
            listView?.adapter = adapter

        }

    }
}