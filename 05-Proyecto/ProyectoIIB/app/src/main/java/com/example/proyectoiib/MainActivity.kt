package com.example.proyectoiib

import CustomAdapter
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    var adapter: CustomAdapter? = null
    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        query = FirebaseFirestore.getInstance().collection("Vehículo")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = CustomAdapter(query!!)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val botonVer = findViewById<ImageView>(R.id.button_prueba)
        botonVer.setOnClickListener {
            irActividad(SecondActivity::class.java)
        }
        val botonPublicar = findViewById<LinearLayout>(R.id.publicar)
        botonPublicar.setOnClickListener {
            irActividad(ThirdActivity::class.java)
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.stopListening()
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}

