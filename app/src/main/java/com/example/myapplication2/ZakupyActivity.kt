package com.example.myapplication2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.dodaj
import kotlinx.android.synthetic.main.activity_main.zakupyRecyclerview
import kotlinx.android.synthetic.main.activity_zakupy.*

class ZakupyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zakupy)


        val nrlisty: Int = pobierznrlisty()

        Log.d("XX", "ZakupyActivity nrlisty:" + nrlisty.toString())

        val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych

        val jakalista = db?.ListaDao()?.getlistabynr(nrlisty) //pobieram z bazy danych wiersz z listy nadrzednej ( tytul i data)
        if (jakalista != null && jakalista.isNotEmpty()) tytul.setText(jakalista[0].nazwa)  // W textview ustawiam nazwe listy, "jakalista" to tablica jednoelementowa

        refresh_liste_z(nrlisty) //odswierzanie, tutaj utworzenie recyclerView

        dodaj.setOnClickListener {
            val builder = AlertDialog.Builder(this)  //dodaje gotowy element Okna dialogowego
            builder.setTitle("Dodaj pozycje zakupów")

            val input = EditText(this) //zmienna do przechowywania wpisanego tekstu

            input.inputType =
                InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", // przycisk OK
                DialogInterface.OnClickListener { dialog, which ->
                    db?.ZakupyDao()?.insert(
                        Zakupy(
                            przedmiot = input.text.toString(),
                            ok = false,
                            lista = nrlisty
                        )
                    ) //dodanie do bazy danych
                    refresh_liste_z(nrlisty) //odswierzanie widoku recyclerview

                })
            builder.setNegativeButton("Cancel", // przycisk Cancel
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.zakupy_menu, menu)
        return true
        //return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.wyslij) {

            val db = ZakupyDB.getInstance(applicationContext) //otwarcie bazy danych
            val nrlisty=pobierznrlisty()
            val pozycje=db?.ZakupyDao()?.getzakupyzlisty(nrlisty)
            val nazwalisty= db?.ListaDao()?.getlistabynr(nrlisty)?.get(0)?.nazwa ?: ""
            var tekstout:String
            tekstout=nazwalisty+":" //zamiesc tytul
            pozycje?.forEach { tekstout=tekstout+it.przedmiot+"," } //zamiesc elementy oddzielone przecinkami

            val wyslijactivity = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,tekstout)
                this.type="text/plain"
            }
            startActivity(wyslijactivity)

        } else return super.onOptionsItemSelected(item)

        return true
    }

    private fun refresh_liste_z(nrlisty: Int) {
        var lista_zakupow =
            ZakupyDB.getInstance(this@ZakupyActivity)?.ZakupyDao()?.getzakupyzlisty(nrlisty)

        if (lista_zakupow == null) Log.d("XX", "refresh null") else Log.d("XX", "refresh not null")

        //znaki zapytania odpowiadają za to czy zmienna może mieć wartość null
        this.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@ZakupyActivity)
            adapter = lista_zakupow?.let { ZakupyAdapter(it, nrlisty) }
        }
    }

    private fun pobierznrlisty():Int{
        val nrlistystring = this.intent.getStringExtra("nrlisty")
        return if (nrlistystring == null) 0 else nrlistystring.toInt() } //konwertuje string na int, a gdy jest null daje 0
}