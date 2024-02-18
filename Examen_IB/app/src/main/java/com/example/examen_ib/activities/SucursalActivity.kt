package com.example.examen_ib.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.examen_ib.R
import com.example.examen_ib.db.DB
import com.example.examen_ib.db.SucursalFirestore
import com.example.examen_ib.db.SupermercadoFirestore
import com.example.examen_ib.models.Sucursal
import com.example.examen_ib.models.Supermercado


class SucursalActivity : AppCompatActivity() {
    private var sucursales: ArrayList<Sucursal>? = null
    var selectedSucursalId = ""
    var selectedItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucursal)

        selectedSucursalId = intent.getStringExtra("id").toString()
        println("streamingServiceId: $selectedSucursalId")

        // load series
//        loadSucursales(selectedSucursalId)



        // Buttons and Listeners
        val goBackButton = findViewById<ImageButton>(
            R.id.btn_go_back_to_supermecados
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val createSucursalButton = findViewById<Button>(
            R.id.btn_create_sucursal
        )

        createSucursalButton.setOnClickListener {
            DB.supermercados!!.getOne(selectedSucursalId)
                .addOnSuccessListener {
                    val bundle = Bundle()
                    val supermercado = SupermercadoFirestore.createSupermercadoFromDocument(it)
                    if (supermercado.getId() == selectedSucursalId) {
                        bundle.putString("sucursalId", selectedSucursalId)
                        bundle.putString("supermercadoId", supermercado.getId())
                        bundle.putString("supermercadoName", supermercado.getNombre())
                    }
                    goToActivity(CreateSucursalActivity::class.java, bundle)
                }
        }

    }

    override fun onResume() {
        super.onResume()

        loadSucursales(selectedSucursalId)
    }

    private fun goToActivity(activity: Class<*>, params: Bundle? = null) {
        val intent = Intent(this, activity)
        if (params != null) {
            intent.putExtras(params)
        }
        startActivity(intent)
    }

    private fun loadSucursales(supermercadoId: String) {
        if (supermercadoId != "") {
            val supermercado = Supermercado()
            DB.supermercados!!.getOne(supermercadoId)
                .addOnSuccessListener {
                    val foundSupermercado = SupermercadoFirestore.createSupermercadoFromDocument(it)
                    supermercado.setId(foundSupermercado.getId())
                    supermercado.setNombre(foundSupermercado.getNombre())
                    supermercado.setRuc(foundSupermercado.getRuc())
                    supermercado.setVendeTecnologia(foundSupermercado.getVendeTecnologia())

                    if (supermercado.getId() == supermercadoId) {
                        val tvTitle = findViewById<TextView>(R.id.tv_supermercado)
                        tvTitle.text = supermercado.getNombre()

                        sucursales = arrayListOf<Sucursal>()
                        DB.sucursales!!.getAllBySupermercado(supermercadoId)
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    sucursales!!.add(SucursalFirestore.createSucursalFromDocument(document))
                                }

                                val sucursalesList = findViewById<ListView>(R.id.lv_sucursales)

                                val adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_list_item_1,
                                    sucursales!!
                                )

                                sucursalesList.adapter = adapter
                                adapter.notifyDataSetChanged()
                                registerForContextMenu(sucursalesList)
                            }
                            .addOnFailureListener {
                                println("Error getting documents: $it")
                            }
                    }
                }
        }
    }

    private fun showConfirmDialog(sucursal: Sucursal) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Deseas eliminar la sucursal de: ${sucursal.getCiudad()}?")
        builder.setMessage("Una vez eliminado, no lo podrás recuperar")
        builder.setPositiveButton("Sí, eliminar") { dialog, which ->
            DB.sucursales!!.remove(sucursal.getId())
            loadSucursales(selectedSucursalId)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(
            R.menu.menu_sucursales,
            menu
        )

        // position
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        selectedItemId = position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedSucursal = sucursales!![selectedItemId]
        return when(item.itemId) {
            R.id.mi_edit_series -> {
                DB.supermercados!!.getOne(selectedSucursalId)
                    .addOnSuccessListener {
                        val supermercado = SupermercadoFirestore.createSupermercadoFromDocument(it)
                        goToActivity(
                            UpdateSucursalActivity::class.java,
                            Bundle().apply {
                                putString("supermercadoId", selectedSucursalId)
                                putString("supermercadoName", supermercado.getNombre())
                                putString("sucursalId", selectedSucursal.getId())
                                putString("ciudad", selectedSucursal.getCiudad())
                                putString("direccion", selectedSucursal.getDireccion())
                                putBoolean("servicioTecnico", selectedSucursal.getServicioTecnico())
                                putLong("numeroEmpleados", selectedSucursal.getNumeroEmpleados())
                                putString("fechaApertura", selectedSucursal.getFechaApertura())
                            }
                        )
                    }
                true
            }
            R.id.mi_delete_series -> {
                showConfirmDialog(selectedSucursal)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}