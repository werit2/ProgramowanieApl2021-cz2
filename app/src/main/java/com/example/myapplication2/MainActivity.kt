package com.example.myapplication2


import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
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

        when {
            intent?.action == Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") importujliste(intent) // Handle text being sent
            }
        }
        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych
        if (db?.ListaDao()?.getAll()?.size == 0) {
            dodaj.performClick()
        }

        refresh_liste_z() //odswierzanie, tutaj utworzenie recyclerView

        dodaj.setOnClickListener {
            val builder = AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Dodaj nową listę zakupów")

            val input = EditText(this) //zmienna do przechowywania wpisanego tekstu

            input.inputType =
                InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    var nr = dodaj_liste(input.text.toString()) //dodanie do bazy danych
                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

    }

    private fun refresh_liste_z() {

        var lista_list = ZakupyDB.getInstance(this@MainActivity)?.ListaDao()?.getAll()!!
        //znaki zapytania odpowiadają za to czy zmienna może mieć wartość null
        this.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = Listaadapter(lista_list)
        }
    }

    private fun dodaj_liste(nazwalisty: String): Int? {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych
        db?.ListaDao()
            ?.insert(Lista(nazwa = nazwalisty, data = currentDate)) //dodanie do bazy danych
        refresh_liste_z() //odswierzanie widoku recyclerview
        return db?.ListaDao()?.getlistamax()
    }

    private fun dodaj_pozycje(pozycja: String, nrlisty: Int) {
        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych
        db?.ZakupyDao()?.insert(
            Zakupy(
                przedmiot = pozycja,
                ok = false,
                lista = nrlisty
            )
        ) //dodanie do bazy danych
    }

    private fun importujliste(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            var kombledu: String = ""
            var blad: Boolean = false
            Log.d("XX", "import:" + it)
            Log.d("XX", "indexof:" + it.indexOf(":").toString())
            if (it.indexOf(":") <= 0) {
                blad = true
                kombledu =
                    "W importowanej liście brak nazwy listy. Nazwa listy mus być na początku tekstu i musi kończyć się dwukropkiem"
            }

            if (blad == false) {
                var lista1 = it.split(":")
                var lista2 = lista1[1].split(",")
                Log.d("XX", "przedmiot:" + lista1[0])
                lista2.forEach {
                    if (it.count() > 50) {
                        blad = true
                        kombledu = "Za długa pozycja listy"
                    }
                }
                if (blad == false) {
                    var nr = dodaj_liste(lista1[0])
                    if (nr != null) {
                        lista2.forEach {
                            Log.d("XX", "pozycja:" + it)
                            dodaj_pozycje(it, nr)
                        }
                    kombledu="Zaimportowano listę: "+  lista1[0]+" oraz "+lista2.size.toString()+" pozycje"
                    } else {
                        blad=true
                        kombledu="inny blad"
                    }
                }
            }

            val builder = AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle(kombledu)
            builder.show()


        }
    }
}