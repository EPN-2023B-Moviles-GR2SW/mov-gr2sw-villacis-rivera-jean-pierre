package com.example.examen_ib.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.example.examen_ib.R
import com.example.examen_ib.db.DB
import com.example.examen_ib.dtos.SupermercadoDto


class UpdateSupermercadoActivity : AppCompatActivity() {
    private val spinnerValues = arrayListOf<String>("Si", "No")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_supermercado)

        loadDataInEditText()

        val goBackButton = findViewById<ImageButton>(R.id.btn_go_back)

        goBackButton.setOnClickListener {
            finish()
        }
        val saveUpdateDataButton = findViewById<Button>(R.id.btn_update_supermercado)

        saveUpdateDataButton.setOnClickListener {
            saveUpdateData()
        }
    }

    private fun loadDataInEditText() {
        val inputRUC = findViewById<EditText>(R.id.txt_supermercado_update_ruc)
        val inputTelefono = findViewById<EditText>(R.id.txt_supermercado_update_telefono)
        val inputName = findViewById<EditText>(R.id.txt_supermercado_update_nombre)

        val ruc = intent.getStringExtra("ruc")
        val nombre = intent.getStringExtra("nombre")
        val telefono = intent.getStringExtra("telefono")
        val vendeTecnologia =intent.getBooleanExtra("vendeTecnologia", false)
        println("Vende tech:" + vendeTecnologia)

        inputRUC.setText(ruc)
        inputName.setText(nombre)
        inputTelefono.setText(telefono)
        loadSpinner(vendeTecnologia)
    }

    private fun saveUpdateData() {
        val inputRuc = findViewById<EditText>(R.id.txt_supermercado_update_ruc)
        val inputTelefono = findViewById<EditText>(R.id.txt_supermercado_update_telefono)
        val inputName = findViewById<EditText>(R.id.txt_supermercado_update_nombre)
        val inputVendeTech = findViewById<Spinner>(R.id.spinner_update_vende_tecnologia)

        val ruc = inputRuc.text.toString()
        val name = inputName.text.toString()
        val telefono = inputTelefono.text.toString()
        val vendeTech = inputVendeTech.selectedItem.toString() == "Si"

        val supermercadoId = intent.getStringExtra("id")

        val changes = SupermercadoDto(
            ruc = ruc,
            nombre = name,
            telefono = telefono,
            vendeTecnologia = vendeTech
        )

        if (supermercadoId != null) {
            DB.supermercados!!.update(supermercadoId, changes)
        }


        finish()
    }

    private fun loadSpinner(value: Boolean) {
        val spinnerVendeTech = findViewById<Spinner>(
            R.id.spinner_update_vende_tecnologia
        )
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerValues
        )
        arrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerVendeTech.adapter = arrayAdapter
        val selection = if (value) 0 else 1
        spinnerVendeTech.setSelection(selection)
    }
}