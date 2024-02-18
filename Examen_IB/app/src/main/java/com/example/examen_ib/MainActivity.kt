package com.example.examen_ib

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
import com.example.examen_ib.activities.CreateSupermercadoActivity
import com.example.examen_ib.activities.SucursalActivity
import com.example.examen_ib.activities.UpdateSupermercadoActivity
import com.example.examen_ib.db.DB
import com.example.examen_ib.db.SqliteHelperSucursal
import com.example.examen_ib.db.SqliteHelperSupermercado
import com.example.examen_ib.db.SucursalFirestore
import com.example.examen_ib.db.SupermercadoFirestore
import com.example.examen_ib.models.Sucursal
import com.example.examen_ib.models.Supermercado
import com.google.firebase.firestore.QueryDocumentSnapshot


class MainActivity : AppCompatActivity() {
    private var supermercados: ArrayList<Supermercado>? = null
    var selectedItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // database
        DB.supermercados = SupermercadoFirestore()
        DB.sucursales = SucursalFirestore()

        // load streaming services

//        loadSupermercados()

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

    private fun createSupermercadoFromDocument(document: QueryDocumentSnapshot): Supermercado {
        val id = document.id
        val ruc = document.data["ruc"] as String?
        val name = document.data["nombre"] as String?
        val phone = document.data["telefono"] as String?
        val vendeTecnologia = document.data["vendeTecnologia"] as Boolean?
        val sucursal = mutableListOf<Sucursal>()

        if (id == null || name == null || ruc == null || phone == null || vendeTecnologia == null) {
            return Supermercado()
        }

        return Supermercado(id, ruc, name, phone, vendeTecnologia, sucursal)
    }

    private fun loadSupermercados() {
        val listView = findViewById<ListView>(
            R.id.lv_supermercados
        )

        supermercados = arrayListOf<Supermercado>()
        DB.supermercados!!.getAll()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val supermercado = createSupermercadoFromDocument(document)
                    supermercados!!.add(supermercado)
                }
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