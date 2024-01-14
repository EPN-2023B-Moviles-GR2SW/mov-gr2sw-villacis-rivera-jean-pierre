package com.example.examen_ib.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen_ib.dtos.SupermercadoDto
import com.example.examen_ib.models.Supermercado


class SqliteHelperSupermercado(
    context: Context?
): SQLiteOpenHelper(
    context,
    "supermercados",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCreateSupermercadoTable =
            """
                CREATE TABLE SUPERMERCADOS(
                    ID VARCHAR(40) PRIMARY KEY,
                    RUC VARCHAR(40),
                    NAME VARCHAR(40),
                    PHONE VARCHAR(40),
                    SELL_TECH INTEGER
                )
            """.trimIndent()

        db?.execSQL(scriptCreateSupermercadoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Code
    }

    // generate an id
    private fun generateId(): String {
        val id = (0..1000000).random()
        return id.toString()
    }

    fun getAll(): ArrayList<Supermercado> {
        val readableDatabase = readableDatabase

        val script = "SELECT * FROM SUPERMERCADOS".trimIndent()
        val result = readableDatabase.rawQuery(
            script,
            null
        )
        // logica busqueda
        val exists = result.moveToFirst()

        val supermercados = arrayListOf<Supermercado>()
        if (exists) {
            do {
                val id = result.getString(0) // Columna indice 0 -> ID
                val ruc = result.getString(1) // Columna indice 1 -> RUC
                val name = result.getString(2) // Columna indice 2 -> NAME
                val phone = result.getString(3) // Columna indice 3 -> PHONE
                val sellTech = result.getInt(4) == 1 // Columna indice 3 -> SELL_TECH

                if (id != null) {
                    val supermercado = Supermercado(id, ruc, name, phone, sellTech, mutableListOf())
                    supermercados.add(supermercado)
                }
            } while (result.moveToNext())
        }

        result.close()
        readableDatabase.close()

        return supermercados
    }

    fun getOne(id: String): Supermercado {
        val readableDatabase = readableDatabase

        val script = "SELECT * FROM SUPERMERCADOS WHERE ID = ?".trimIndent()
        val result = readableDatabase.rawQuery(
            script,
            arrayOf(id.toString())
        )
        // logica busqueda
        val exists = result.moveToFirst()

        val found = Supermercado("", "", "", "", true, mutableListOf())
        if (exists) {
            do {
                val id = result.getString(0) // Columna indice 0 -> ID
                val ruc = result.getString(1) // Columna indice 1 -> RUC
                val name = result.getString(2) // Columna indice 2 -> NAME
                val phone = result.getString(3) // Columna indice 3 -> PHONE
                val sellTech = result.getInt(4) == 1 // Columna indice 3 -> SELL_TECH

                if (id != null) {
                    found.setId(id)
                    found.setRuc(ruc)
                    found.setNombre(name)
                    found.setTelefono(phone)
                    found.setVendeTecnologia(sellTech)
                }
            } while (result.moveToNext())
        }


        result.close()
        readableDatabase.close()

        return found
    }

    fun create(data: SupermercadoDto): Boolean {
        val writableDatabase = writableDatabase

        val values = ContentValues()

        values.put("ID", generateId())
        values.put("RUC", data.ruc)
        values.put("NAME", data.nombre)
        values.put("PHONE", data.telefono)
        values.put("SELL_TECH", data.vendeTecnologia)

        val result = writableDatabase.insert(
            "SUPERMERCADOS",
            null,
            values
        )

        writableDatabase.close()

        return result.toInt() != -1
    }

    fun update(id: String, data: SupermercadoDto): Boolean {
        val writableDatabase = writableDatabase

        val values = ContentValues()

        values.put("RUC", data.ruc)
        values.put("NAME", data.nombre)
        values.put("PHONE", data.telefono)
        values.put("SELL_TECH", data.vendeTecnologia)

        val result = writableDatabase.update(
            "SUPERMERCADOS",
            values,
            "ID = ?",
            arrayOf(id.toString())
        )

        writableDatabase.close()

        return result != -1
    }

    fun remove(id: String): Boolean {
        val writableDatabase = writableDatabase

        val result = writableDatabase.delete(
            "SUPERMERCADOS",
            "ID = ?",
            arrayOf(id.toString())
        )

        writableDatabase.close()

        return result != -1
    }
}