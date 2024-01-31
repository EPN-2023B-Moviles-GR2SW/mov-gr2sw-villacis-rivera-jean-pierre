package com.example.deber03_jeanvillacis

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter;

        val botonVer = findViewById<ImageView>(R.id.button_prueba)
        botonVer
            .setOnClickListener{
                irActividad(SecondActivity::class.java)
            }

    }

    fun irActividad(
        clase:Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }

    }

