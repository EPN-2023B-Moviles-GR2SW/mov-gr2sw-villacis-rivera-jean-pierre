package com.example.proyectoiib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class ThirdActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_layout)

        val bntSiguiente = findViewById<Button>(R.id.siguiente)
        bntSiguiente.setOnClickListener {

            val tipo = findViewById<EditText>(R.id.Tipo_vehiculo).text.toString()
            val marca = findViewById<EditText>(R.id.Marca_vehiculo).text.toString()
            val modelo = findViewById<EditText>(R.id.Modelo_vehiculo).text.toString()
            val anio = findViewById<EditText>(R.id.Año_vehiculo).text.toString()
            val kilometraje = findViewById<EditText>(R.id.Kilometraje_vehiculo).text.toString()
            val medidaKilometraje = findViewById<EditText>(R.id.Medida_kilometraje).text.toString()
            val precio = findViewById<EditText>(R.id.Precio_vehiculo).text.toString()
            val ciudad = findViewById<EditText>(R.id.Ciudad_vehiculo).text.toString()

            val datosVehiculo = hashMapOf(
                "tipo" to tipo,
                "marca" to marca,
                "modelo" to modelo,
                "año" to anio,
                "kilometraje" to "$kilometraje $medidaKilometraje",
                "precio" to "$$precio/Negociable",
                "ciudad" to ciudad,
                "imageUrl" to ""
            )

            db.collection("Vehículo")
                .add(datosVehiculo)
                .addOnSuccessListener {
                    irActividad(FourthActivity::class.java)
                }
                .addOnFailureListener {
                }

            val botonHome = findViewById<LinearLayout>(R.id.home)
            botonHome.setOnClickListener {
                irActividad(MainActivity::class.java)
            }
        }
    }

    fun irActividad(
        clase:Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}