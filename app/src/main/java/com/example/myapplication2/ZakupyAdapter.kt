package com.example.myapplication2

import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication2.R.layout.przedmiot_row
import com.example.myapplication2.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.przedmiot_row.view.*

class ZakupyAdapter(private var listaZakupow: List<Zakupy>) :

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

        holder.view.usun.setOnClickListener{
            //holder.view.przedmiot.performClick()

            //ZakupyDB.getInstance(holder.view.context)?.ZakupyDao()?.delete(listaZakupow[position])
            db?.ZakupyDao()?.delete(listaZakupow[position])
            //listaZakupow=(ZakupyDB.getInstance(holder.view.context)?.ZakupyDao()?.getAll()!!)
            listaZakupow=(db?.ZakupyDao()?.getAll()!!)
            update()
            //MainActivity().kasuj(listaZakupow[position])
        }
        holder.view.przedmiot.setOnClickListener{
            //var costam : Zakupy = Zakupy(przedmiot = "costam", ok = false)
            //ZakupyDB.getInstance(holder.view.context)?.ZakupyDao()?.done(costam)

            if (listaZakupow[position].ok == true){
                listaZakupow[position].ok = false
            }
            else {
                listaZakupow[position].ok = true
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