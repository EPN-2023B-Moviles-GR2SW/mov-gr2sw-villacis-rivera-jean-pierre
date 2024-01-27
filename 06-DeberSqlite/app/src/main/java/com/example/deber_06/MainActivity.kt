package com.example.deber_06

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.deber_06.activities.CreateSupermercadoActivity
import com.example.deber_06.activities.SucursalActivity
import com.example.deber_06.activities.UpdateSupermercadoActivity
import com.example.deber_06.db.DB
import com.example.deber_06.db.SqliteHelperSucursal
import com.example.deber_06.db.SqliteHelperSupermercado
import com.example.deber_06.models.Supermercado


class MainActivity : AppCompatActivity() {
    private var supermercados: ArrayList<Supermercado>? = null
    var selectedItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // database
        DB.supermercados = SqliteHelperSupermercado(this)
        DB.sucursales = SqliteHelperSucursal(this)

        // load streaming services

        loadSupermercados()

        val btnCreate = findViewById<Button>(
            R.id.btn_supermercados
        )

        btnCreate.setOnClickListener {
            goToActivity(CreateSupermercadoActivity::class.java)
        }
    }


    override fun onResume() {
        super.onResume()
        loadSupermercados()
    }

    private fun loadSupermercados() {
        val listView = findViewById<ListView>(
            R.id.lv_supermercados
        )
        supermercados = DB.supermercados!!.getAll()

        if (supermercados != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                supermercados!!
            )
            listView.adapter = adapter

            adapter.notifyDataSetChanged()
            registerForContextMenu(listView)
        }

    }


    private fun goToActivity(activity: Class<*>, params: Bundle? = null) {
        val intent = Intent(this, activity)
        if (params != null) {
            intent.putExtras(params)
        }
        startActivity(intent)
    }

    private fun showConfirmDeleteDialog(supermercado: Supermercado) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Estás seguro de eliminar: ${supermercado.getNombre()}?")
        builder.setMessage("Una vez eliminado no se podrá recuperar.")
        builder.setPositiveButton("Sí") { _, _ ->
            val removed = supermercados!!.removeAt(selectedItemId)
            DB.supermercados!!.remove(removed.getId())
            loadSupermercados()
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
        inflater.inflate(R.menu.menu_supermercados, menu)

        //position
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        selectedItemId = position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val supermercado = supermercados!![selectedItemId]
        return when(item.itemId) {
            R.id.mi_show_series -> {
                "Hacer algo con: ${selectedItemId}"
                if (supermercados == null) return false

                val params = Bundle()
                params.putString("id", supermercado.getId())
                params.putString("ruc", supermercado.getRuc())
                params.putString("nombre", supermercado.getNombre())
                params.putString("telefono", supermercado.getTelefono())
                params.putBoolean("vendeTecnologia", supermercado.getVendeTecnologia())

                goToActivity(SucursalActivity::class.java, params)
                return true
            }
            R.id.mi_update -> {
                "Hacer algo con: ${selectedItemId}"
                val params = Bundle()
                params.putString("id", supermercado.getId())
                params.putString("ruc", supermercado.getRuc())
                params.putString("nombre", supermercado.getNombre())
                params.putString("telefono", supermercado.getTelefono())
                params.putBoolean("vendeTecnologia", supermercado.getVendeTecnologia())

                goToActivity(UpdateSupermercadoActivity::class.java, params)
                return true
            }
            R.id.mi_delete -> {
                showConfirmDeleteDialog(supermercado)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}