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
    var id = ArrayList<String>()
    var videoId = ArrayList<String>()
    lateinit var userDetail: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        listView = findViewById(R.id.listView)

        Glide.with(this)
            .load(R.drawable.logo_png_white)
            .into(logo_ssmsList)

        lottieList.setAnimation("loading_circle.json")
        lottieList.loop(true)
        lottieList.playAnimation()

        val viewType = intent.getStringExtra("network")
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
                            val intent = Intent(this, Menu::class.java)
                            startActivity(intent)
                        }
                    }
                }
                Log.d("network", "Online")
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
                Log.d("network", "Offline")
            }
        }
    }

    fun online(){
        when (intent.getStringExtra("ViewType")!!) {
            "soft_skill" -> {
                Log.d("debugList", "soft_skill")
                parseJSONdata(0,"db_softskill")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_softSkill, this, bt_update, tv_tanggal, progressBarHolderListView, "db_softskill", "update")
                }
            }
            "estate" -> {
                parseJSONdata(1,"db_estate")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_estate, this, bt_update, tv_tanggal, progressBarHolderListView, "db_estate", "update")
                }
            }
            "admin" -> {
                parseJSONdata(2,"db_admin")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_admin, this, bt_update, tv_tanggal, progressBarHolderListView, "db_admin", "update")
                }
            }
            "mill" -> {
                parseJSONdata(3,"db_mill")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_mill, this, bt_update, tv_tanggal, progressBarHolderListView, "db_mill", "update")
                }
            }
            "traksi" -> {
                parseJSONdata(4,"db_traksi")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_traksi, this, bt_update, tv_tanggal, progressBarHolderListView, "db_traksi", "update")
                }
            }
            "supporting" -> {
                parseJSONdata(5,"db_supporting")
                bt_update.setOnClickListener {
                    UpdateMan().updater(Database.data_supporting, this, bt_update, tv_tanggal, progressBarHolderListView, "db_supporting", "update")
                }
            }
        }
    }

    fun offline(){
        when (intent.getStringExtra("ViewType")) {
            "soft_skill" -> {
                parseJSONdata(0,"db_softskill")
            }
            "db_estate" -> {
                parseJSONdata(0,"db_softskill")
            }
            "db_admin" -> {
                parseJSONdata(0,"db_softskill")
            }
            "db_mill" -> {
                parseJSONdata(0,"db_softskill")
            }
            "db_traksi" -> {
                parseJSONdata(0,"db_softskill")
            }
            "db_supporting" -> {
                parseJSONdata(0,"db_softskill")
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

    fun parseJSONdata(int: Int, string: String) {
        // since we have JSON object, so we are getting the object
        //here we are calling a function and that function is returning the JSON object
        var obj: JSONObject
        try {
            obj = JSONObject(FileMan().onlineInputStream(this))
        }catch (e:Exception){
            obj = JSONObject(FileMan().offlineInputStream(this))
        }
        // fetch JSONArray named users by using getJSONArray
        val userArray = obj.getJSONArray("db_youtube")
        // implement for loop for getting users data i.e. name, email and contact
        val objContent =  userArray.getJSONObject(int)
        val contentArray = objContent.getJSONArray(string)
        for (i in 0 until userArray.length()) {
            // create a JSONObject for fetching single user data
            userDetail = userArray.getJSONObject(i)

            val m = MathFunc()
            judul.add(try {
                userDetail.getString("judul")
            } catch (e: Exception) {
                "0"
            })
            tag.add(try {
                userDetail.getString("tag")
            } catch (e: Exception) {
                "0"
            })
            id.add(try {
                userDetail.getString("id")
            } catch (e: Exception) {
                "0"
            })
            videoId.add(try {
                userDetail.getString("video_id")
            } catch (e: Exception) {
                "0"
            })

            val model = ModelList(id[i], judul[i], tag[i], videoId[i])
            //bind all strings in an array
            arrayList.add(model)

            //pass results to listViewAdapter class
            adapter = ListViewAdapter(this, arrayList)
            //bind the adapter to the listview
            listView?.adapter = adapter

        }

    }
}