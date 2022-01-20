package com.example.myapplication2

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.przedmiot_row.view.*


class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych

        refresh_liste_z() //odswierzanie, tutaj utworzenie recyclerView


        binding.dodaj.setOnClickListener {
            val builder= AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Dodaj pozycje")

            val input = EditText(this) //zmienna do przechowywania wpisanego tekstu

            input.inputType =
                InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    db?.ZakupyDao()?.insert(Zakupy(przedmiot = input.text.toString(), ok = false)) //dodanie do bazy danych
                    refresh_liste_z() //odswierzanie widoku recyclerview

                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

    }
    private fun refresh_liste_z(){

        var lista_zakupow =ZakupyDB.getInstance(this@MainActivity)?.ZakupyDao()?.getAll()
        //znaki zapytania odpowiadają za to czy zmienna może mieć wartość null
        binding.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = lista_zakupow?.let { ZakupyAdapter(it) }
        }
    }
}