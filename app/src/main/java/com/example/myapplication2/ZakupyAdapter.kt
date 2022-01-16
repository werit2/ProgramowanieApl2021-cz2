package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R.layout.przedmiot_row
import kotlinx.android.synthetic.main.przedmiot_row.view.*

class ZakupyAdapter(private val listaZakupow: List<Zakupy>) :
    RecyclerView.Adapter<ZakupyAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(przedmiot_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.przedmiot.text = listaZakupow[position].przedmiot
    }

    override fun getItemCount() = listaZakupow.size


}