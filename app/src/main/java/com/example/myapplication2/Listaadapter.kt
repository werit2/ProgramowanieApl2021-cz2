package com.example.myapplication2

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R.layout.lista_row
import kotlinx.android.synthetic.main.lista_row.view.*
import kotlinx.android.synthetic.main.przedmiot_row.view.*
import kotlinx.android.synthetic.main.przedmiot_row.view.usun

class Listaadapter(private var listaList: List<Lista>) :

    RecyclerView.Adapter<Listaadapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(lista_row, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db = ZakupyDB.getInstance(holder.view.context) //otwarcie bazy danych
        var tekst=listaList[position].nazwa +"\n"+listaList[position].data

        holder.view.nazwalisty.text = tekst

        if(position %2==0) // co druga pozycja ma szare tło
            holder.view.setBackgroundColor(Color.LTGRAY)
        else holder.view.setBackgroundColor(Color.WHITE)

        holder.view.usun.setOnClickListener{
            val builder= AlertDialog.Builder(holder.view.context)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Potwierdź usunięcie całej listy zakupów") //Potwierdzenie usuniecia całej listy
            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    var pozycjedousuniecia:List<Zakupy> //deklaruje zmienna
                    pozycjedousuniecia=db?.ZakupyDao()?.getzakupyzlisty(listaList[position].id)!! //przypisuje do zmiennej wszystkie elementy o identyfikatorze listy
                    pozycjedousuniecia.forEach {
                        //Log.d("X1","Recycler usuwanie listy "+ it.przedmiot + ", z listy " + it.lista)
                        db?.ZakupyDao().delete(it) //usuwam elementy z listy

                    }

                    db?.ListaDao()?.delete(listaList[position]) //usuwam sama liste

                    listaList=(db?.ListaDao()?.getAll()!!) //znaki zapytania oznaczaj ze zmienna moze przyjmowac null, a wykrzykini konwertuja na zmienna nie-null
                    update()
                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        holder.view.nazwalisty.setOnClickListener{
            var zakupyintent: Intent = Intent(holder.view.context, ZakupyActivity::class.java)
                zakupyintent.putExtra("nrlisty",listaList[position].id.toString()) //przekazanie parametru numer listy do nowej aktywnosci, musi byc typu string
            startActivity(holder.view.context,zakupyintent, Bundle())
        }

    }

    override fun getItemCount() = listaList.size

    private fun update() {
        this.notifyDataSetChanged()
    }

}