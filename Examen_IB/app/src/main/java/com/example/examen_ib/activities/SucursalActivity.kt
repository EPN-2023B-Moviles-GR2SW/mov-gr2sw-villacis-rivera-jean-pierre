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
import com.example.examen_ib.models.Sucursal


class SucursalActivity : AppCompatActivity() {

    private var sucursales: ArrayList<Sucursal>? = null
    var selectedSucursalId = ""
    var selectedItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucursal)

        selectedSucursalId = intent.getStringExtra("id").toString()
        println("streamingServiceId: $selectedSucursalId")

        // load sucursales
        loadSucursales(selectedSucursalId)

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
            goToActivity(CreateSucursalActivity::class.java, Bundle().apply {
                val supermercado = DB.supermercados!!.getOne(selectedSucursalId)
                if (supermercado.getId() == selectedSucursalId) {
                    putString("sucursalId", selectedSucursalId)
                    putString("supermercadoId", supermercado.getId())
                    putString("supermercadoName", supermercado.getNombre())
                }
            })
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
            val supermercado = DB.supermercados!!.getOne(supermercadoId)

            if (supermercado.getId() == supermercadoId) {
                sucursales = DB.sucursales!!.getAllBySupermercado(supermercadoId)

                val tvTitle = findViewById<TextView>(R.id.tv_supermercado)
                tvTitle.text = supermercado.getNombre()
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
                val supermercado = DB.supermercados!!.getOne(selectedSucursalId)
                goToActivity(
                    UpdateSucursalActivity::class.java,
                    Bundle().apply {
                        putString("supermercadoId", selectedSucursalId)
                        putString("supermercadoName", supermercado.getNombre())
                        putString("sucursalId", selectedSucursal.getId())
                        putString("ciudad", selectedSucursal.getCiudad())
                        putString("direccion", selectedSucursal.getDireccion())
                        putBoolean("servicioTecnico", selectedSucursal.getServicioTecnico())
                        putInt("numeroEmpleados", selectedSucursal.getNumeroEmpleados())
                        putString("fechaApertura", selectedSucursal.getFechaApertura())
                    }
                )
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