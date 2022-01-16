package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        ).allowMainThreadQueries()
            .build()


        db.ZakupyDao().insert(Zakupy(przedmiot = "sałata", ok = false))

        val lista_zakupow =db.ZakupyDao().getAll()

        binding.zakupyRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ZakupyAdapter(lista_zakupow)
        }
    }
}