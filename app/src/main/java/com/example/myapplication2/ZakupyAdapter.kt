package com.example.myapplication2

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R.layout.przedmiot_row
import kotlinx.android.synthetic.main.przedmiot_row.view.*

class ZakupyAdapter(private var listaZakupow: List<Zakupy>,val nrlisty:Int) :

    RecyclerView.Adapter<ZakupyAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(przedmiot_row, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db = ZakupyDB.getInstance(holder.view.context) //otwarcie bazy danych

        holder.view.przedmiot.text = listaZakupow[position].przedmiot
        holder.view.przedmiot.isChecked = listaZakupow[position].ok!!
        if (listaZakupow[position].ok==true) holder.view.przedmiot.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)

        holder.view.usun.setOnClickListener{

            db?.ZakupyDao()?.delete(listaZakupow[position])
            listaZakupow=(db?.ZakupyDao()?.getzakupyzlisty(nrlisty)!!)
            update()
        }
        holder.view.przedmiot.setOnClickListener{
            //var costam : Zakupy = Zakupy(przedmiot = "costam", ok = false)
            //ZakupyDB.getInstance(holder.view.context)?.ZakupyDao()?.done(costam)

            if (listaZakupow[position].ok == true){
                listaZakupow[position].ok = false
                holder.view.przedmiot.setPaintFlags(0)
            }
            else {
                listaZakupow[position].ok = true
                holder.view.przedmiot.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            }
            db?.ZakupyDao()?.done(listaZakupow[position])
            //db?.ZakupyDao()?.insert(Zakupy(przedmiot = input.text.toString(), ok = false)
        }

    }

    override fun getItemCount() = listaZakupow.size

    private fun update() {
        this.notifyDataSetChanged()
    }

}