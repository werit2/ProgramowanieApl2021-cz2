package com.example.myapplication2

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplication2.databinding.ActivityMainBinding

//import com.example.myapplication2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            ZakupyDB::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()


        //db.ZakupyDao().insert(Zakupy(przedmiot = "sałata", ok = false))

        val lista_zakupow =db.ZakupyDao().getAll()

        binding.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ZakupyAdapter(lista_zakupow)
        }


        binding.dodaj.setOnClickListener {
            val builder= AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Dodaj pozycje")

            val input = EditText(this) //zmienna do przechowywania wpisanego tekstu

            input.inputType =
                InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    db.ZakupyDao().insert(Zakupy(przedmiot = input.text.toString(), ok = false)) //dodanie do bazy danych
                    refresh_liste_z(db.ZakupyDao().getAll()) //odswierzanie widoku recyclerview

                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

    }
    private fun refresh_liste_z( lista: List<Zakupy>){


        binding.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ZakupyAdapter(lista)}
    }
    fun kasuj( pozycja:Zakupy ) {
        //val db:Zakupy = Zakupy(this@MainActivity)
        //db.ZakupyDao().delete(pozycja)
    }
}