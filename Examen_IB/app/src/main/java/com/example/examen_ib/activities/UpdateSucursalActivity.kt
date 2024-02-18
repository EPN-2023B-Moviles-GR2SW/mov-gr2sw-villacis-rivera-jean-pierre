package com.example.examen_ib.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.example.examen_ib.R
import com.example.examen_ib.db.DB
import com.example.examen_ib.dtos.SucursalDto


class UpdateSucursalActivity : AppCompatActivity() {

    private val spinnerValues = arrayListOf<String>("Si", "No")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_sucursal)

        loadDataInEditText(intent)

        val goBackButton = findViewById<ImageButton>(
            R.id.ib_go_back_update_sucursal_to_sucursal_list
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val saveUpdatedData = findViewById<Button>(
            R.id.btn_update_sucursal
        )

        saveUpdatedData.setOnClickListener {
            updateSeries()
            finish()
        }
    }

    private fun updateSeries() {
        val inputCiudad = findViewById<EditText>(R.id.pt_update_ciudad)
        val inputDireccion = findViewById<EditText>(R.id.pt_sucursal_update_direccion)
        val inputFechaApertura = findViewById<EditText>(R.id.pt_sucursal_update_fecha_apertura)
        val spinnerServicioTecnico = findViewById<Spinner>(R.id.spinner_update_servicio_tecnico)
        val inputNumeroEmpleados = findViewById<EditText>(R.id.pt_sucursal_update_numero_empleados)


        val inputSupermercadoTitle = findViewById<EditText>(R.id.pt_supermercado_title)

        val ciudad = inputCiudad.text.toString()
        val direccion = inputDireccion.text.toString()
        val fechaApertura = inputFechaApertura.text.toString()
        val servicioTecnico = spinnerServicioTecnico.selectedItem.toString() == "Si"
        val numeroEmpleados = inputNumeroEmpleados.text.toString().toLong()
        val supermercadoTitle = inputSupermercadoTitle.text.toString()

        val supermercadoId = intent.getStringExtra("supermercadoId").toString()
        val sucursalId = intent.getStringExtra("sucursalId").toString()


        val updatedSucursal = SucursalDto(
            ciudad,
            direccion,
            servicioTecnico,
            numeroEmpleados,
            fechaApertura,
            supermercadoId
        )

        DB.sucursales!!.update(sucursalId, updatedSucursal)

        finish()
    }

    private fun loadDataInEditText(intent: Intent) {
        val supermercadoName = intent.getStringExtra("supermercadoName")
        val ciudad = intent.getStringExtra("ciudad")
        val direccion = intent.getStringExtra("direccion")
        val fechaApertura = intent.getStringExtra("fechaApertura")
        val servicioTecnico = intent.getBooleanExtra("servicioTecnico", false)
        val numeroEmpleados = intent.getLongExtra("numeroEmpleados", 0)

        val inputCiudad = findViewById<EditText>(R.id.pt_update_ciudad)
        val inputDireccion = findViewById<EditText>(R.id.pt_sucursal_update_direccion)
        val inputFechaApertura = findViewById<EditText>(R.id.pt_sucursal_update_fecha_apertura)
        val spinnerServicioTecnico = findViewById<Spinner>(R.id.spinner_update_servicio_tecnico)
        val inputNumeroEmpleados = findViewById<EditText>(R.id.pt_sucursal_update_numero_empleados)

        val inputSupermercado = findViewById<EditText>(R.id.pt_supermercado_title)

        inputSupermercado.setText(supermercadoName)
        inputCiudad.setText(ciudad)
        inputDireccion.setText(direccion)
        inputFechaApertura.setText(fechaApertura)
        inputNumeroEmpleados.setText(numeroEmpleados.toString())
        loadSpinner(servicioTecnico)

    }

    private fun loadSpinner(value: Boolean) {
        val spinnerIsSeriesFinished = findViewById<Spinner>(
            R.id.spinner_update_servicio_tecnico
        )

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerValues
        )

        arrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerIsSeriesFinished.adapter = arrayAdapter
        val selection = if (value) 0 else 1
        spinnerIsSeriesFinished.setSelection(selection)
    }
}