package com.example.examen_ib.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.examen_ib.R
import com.example.examen_ib.db.DB
import com.example.examen_ib.dtos.SucursalDto


class CreateSucursalActivity : AppCompatActivity() {
    private val spinnerValues = arrayListOf<String>("Si", "No")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sucursal)

        val goBackButton = findViewById<ImageButton>(
            R.id.go_back_to_supermercados_list
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val txtSucursalSupermercado = findViewById<EditText>(R.id.pt_sucursal_supermercado)

        val supermercadoName = intent.getStringExtra("supermercadoName").toString()
        txtSucursalSupermercado.setText(supermercadoName)


        val saveNewSucursalButton = findViewById<Button>(
            R.id.btn_new_sucursal
        )
        loadSpinner()

        val supermercadoId = intent.getStringExtra("supermercadoId").toString()

        saveNewSucursalButton.setOnClickListener {
            createSucursal(supermercadoId)
        }

    }

    private fun createSucursal(supermercadoId: String) {
        val inputCiudad = findViewById<EditText>(R.id.pt_sucursal_ciudad)
        val inputDireccion = findViewById<EditText>(R.id.pt_sucursal_direccion)
        val inputFechaApertura = findViewById<EditText>(R.id.pt_sucursal_fecha_apertura)
        val spinnerServicioTecnico = findViewById<Spinner>(R.id.spinner_servicio_tecnico)
        val inputNumeroEmpleados = findViewById<EditText>(R.id.pt_sucursal_numero_empleados)

        val ciudad = inputCiudad.text.toString()
        val direccion = inputDireccion.text.toString()
        val fechaApertura = inputFechaApertura.text.toString()
        val servicioTecnico = spinnerServicioTecnico.selectedItem.toString() == "Si"
        val numeroEmpleados = inputNumeroEmpleados.text.toString().toLong()


        val newSucursal = SucursalDto(
            ciudad,
            direccion,
            servicioTecnico,
            numeroEmpleados,
            fechaApertura,
            supermercadoId
        )

        DB.sucursales!!.create(newSucursal)

        finish()
    }

    private fun loadSpinner() {
        val spinnerIsSeriesFinished = findViewById<Spinner>(
            R.id.spinner_servicio_tecnico
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
    }
}