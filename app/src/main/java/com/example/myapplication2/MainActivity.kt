package com.example.myapplication2


import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.przedmiot_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych

        refresh_liste_z() //odswierzanie, tutaj utworzenie recyclerView

        dodaj.setOnClickListener {
            val builder= AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Dodaj nową listę zakupów")

            val input = EditText(this) //zmienna do przechowywania wpisanego tekstu

            input.inputType =
                InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    val sdf = SimpleDateFormat("dd/M/yyyy")
                    val currentDate = sdf.format(Date())
                    db?.ListaDao()?.insert(Lista(nazwa = input.text.toString(), data = currentDate)) //dodanie do bazy danych
                    refresh_liste_z() //odswierzanie widoku recyclerview

                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

    }
    private fun refresh_liste_z(){

        var lista_list =ZakupyDB.getInstance(this@MainActivity)?.ListaDao()?.getAll()!!
        //znaki zapytania odpowiadają za to czy zmienna może mieć wartość null
        this.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = Listaadapter(lista_list)
        }
    }
}