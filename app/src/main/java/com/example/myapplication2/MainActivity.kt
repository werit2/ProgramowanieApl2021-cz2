package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.myapplication2.databinding.ActivityMainBinding

//import com.example.myapplication2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        binding = ActivityMainBinding.inflate(layoutInflater)



        val db = Room.databaseBuilder(
            applicationContext,
            ZakupyDB::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()


        db.ZakupyDao().insert(Zakupy(przedmiot = "jajko", ok = false))

        val lista_zakupow =db.ZakupyDao().getAll()

        lista_zakupow.forEach {
            binding.ZakupytextView.append(it.przedmiot + "-" + it.ok + "\n")
        }

    }
}