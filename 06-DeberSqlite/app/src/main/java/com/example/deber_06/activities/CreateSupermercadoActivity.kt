package com.example.deber_06.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.deber_06.R
import com.example.deber_06.db.DB
import com.example.deber_06.dtos.SupermercadoDto
import com.example.deber_06.models.Supermercado


class CreateSupermercadoActivity : AppCompatActivity() {
    private var supermercados: ArrayList<Supermercado>? = null
    private val spinnerValues = arrayListOf<String>("Si", "No")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_supermercado)

        // database
        supermercados = DB.supermercados!!.getAll()

        val goBackButton = findViewById<ImageButton>(R.id.btn_go_back)

        goBackButton.setOnClickListener{
            finish()
        }

        loadSpinner()

        val saveNewStreamingServiceButton = findViewById<Button>(
            R.id.btn_new_supermercado
        )

        saveNewStreamingServiceButton.setOnClickListener {
            createSupermercado()
        }
    }

    private fun createSupermercado() {
        val inputRUC = findViewById<EditText>(R.id.txt_supermercado_ruc)
        val inputName = findViewById<EditText>(R.id.txt_supermercado_name)
        val inputTelefono = findViewById<EditText>(R.id.txt_supermercado_telefono)
        val inputVendeTech = findViewById<Spinner>(R.id.spinner_vende_tecnologia)

        val ruc = inputRUC.text.toString()
        val name = inputName.text.toString()
        val telefono = inputTelefono.text.toString()
        val vendeTech = inputVendeTech.selectedItem.toString() == "Si"

        val newSupermercado = SupermercadoDto(
            ruc,
            name,
            telefono,
            vendeTech
        )

        DB.supermercados!!.create(newSupermercado)

        finish()
    }

    private fun loadSpinner() {
        val spinnerIsSeriesFinished = findViewById<Spinner>(
            R.id.spinner_vende_tecnologia
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