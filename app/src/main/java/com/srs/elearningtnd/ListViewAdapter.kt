package com.srs.elearningtnd

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import es.dmoral.toasty.Toasty


class ListViewAdapter(private val context: Activity, private val tanggal: Array<String>, private val afdeling: Array<String>, private val blok: Array<String>, private val ancak: Array<String>, private val tbsKirim: Array<String>, private val id:Array<Int>)
    : ArrayAdapter<String>(context,
    R.layout.panen_list, blok) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.panen_list, null, true)

        val tvTanggalPanenList = rowView.findViewById(R.id.tvTanggalPanenList) as TextView
        val tvLokasiPanenList = rowView.findViewById(R.id.tvLokasiPanenList) as TextView
        val tvTbsPanenList = rowView.findViewById(R.id.tvTbsPanenList) as TextView
        val deleteListPanen = rowView.findViewById(R.id.deleteListPanen) as ImageView

        val linearLayoutPanenList = rowView.findViewById(R.id.linearLayoutPanenList) as LinearLayout

        if (position % 2 == 0){
            linearLayoutPanenList.setBackgroundResource(R.color.white)
        } else {
            linearLayoutPanenList.setBackgroundResource(R.color.colorGreenList)
        }

        tvTanggalPanenList.text = tanggal[position]
        tvLokasiPanenList.text = "${afdeling[position]} > ${blok[position]} > ${ancak[position]}"
        tvTbsPanenList.text = "${tbsKirim[position]} Buah"
        return rowView
    }

}