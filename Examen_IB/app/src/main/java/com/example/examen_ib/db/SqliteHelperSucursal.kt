package com.example.examen_ib.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen_ib.dtos.SucursalDto
import com.example.examen_ib.models.Sucursal


class SqliteHelperSucursal(
    context: Context
): SQLiteOpenHelper(
    context,
    "sucursal",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCreateStreamingServiceTable =
            """
                CREATE TABLE SUCURSALES(
                    ID VARCHAR(40) PRIMARY KEY,
                    CITY VARCHAR(60),
                    ADDRESS VARCHAR(60),
                    TECH_SERVICE INTEGER,
                    EMPLOYEES_NUMBER INTEGER,
                    OPEN_DATE VARCHAR(100),
                    SUPERMERCADO_ID VARCHAR(40),
                    FOREIGN KEY(SUPERMERCADO_ID) REFERENCES SUPERMERCADO(ID)
                )
            """.trimIndent()

        db?.execSQL(scriptCreateStreamingServiceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Code
    }

    // generate an id
    private fun generateId(): String {
        val id = (0..1000000).random()
        return id.toString()
    }

    fun getAll(): ArrayList<Sucursal> {
        val readableDatabase = readableDatabase

        val script = "SELECT * FROM SUCURSALES".trimIndent()
        val result = readableDatabase.rawQuery(
            script,
            null
        )
        // logica busqueda
        val exists = result.moveToFirst()

        val allSucursales = arrayListOf<Sucursal>()
        if (exists) {
            do {
                val id = result.getString(0) // Columna indice 0 -> ID
                val city = result.getString(1) // Columna indice 1 -> NAME
                val address = result.getString(2) // Columna indice 2 -> GENRE
                val techService = result.getInt(3) == 1 // Columna indice 3 -> IS_FINISHED
                val employeesNumber = result.getInt(4) // Columna indice 4 -> SEASONS
                val openDate = result.getString(5) // Columna indice 5 -> EMISSION_DATE
                val sucursalId = result.getString(6) // Columna indice 6 -> STREAMING_SERVICE_ID

                if (id != null) {
                    val series = Sucursal(
                        id,
                        city,
                        address,
                        techService,
                        employeesNumber,
                        openDate,
                        sucursalId
                    )
                    allSucursales.add(series)
                }
            } while (result.moveToNext())
        }

        return allSucursales
    }

    fun getAllBySupermercado(supermercadoId: String): ArrayList<Sucursal> {
        val readableDatabase = readableDatabase

        val script = "SELECT * FROM SUCURSALES WHERE SUPERMERCADO_ID = ?".trimIndent()

        val result = readableDatabase.rawQuery(
            script,
            arrayOf(supermercadoId.toString())
        )

        val exists = result.moveToFirst()

        val allSucursales = arrayListOf<Sucursal>()

        if (exists) {
            do {
                val id = result.getString(0) // Columna indice 0 -> ID
                val city = result.getString(1) // Columna indice 1 -> NAME
                val address = result.getString(2) // Columna indice 2 -> GENRE
                val techService = result.getInt(3) == 1 // Columna indice 3 -> IS_FINISHED
                val employeesNumber = result.getInt(4) // Columna indice 4 -> SEASONS
                val openDate = result.getString(5) // Columna indice 5 -> EMISSION_DATE
                val sucursalId = result.getString(6) // Columna indice 6 -> STREAMING_SERVICE_ID

                if (id != null) {
                    val series = Sucursal(
                        id,
                        city,
                        address,
                        techService,
                        employeesNumber,
                        openDate,
                        sucursalId
                    )
                    allSucursales.add(series)
                }
            } while (result.moveToNext())
        }

        result.close()
        readableDatabase.close()

        return allSucursales
    }


    fun getOne(id: String): Sucursal {
        val readableDatabase = readableDatabase

        val script = "SELECT * FROM SUCURSALES WHERE ID = ?".trimIndent()

        val result = readableDatabase.rawQuery(
            script,
            arrayOf(id.toString())
        )

        val exists = result.moveToFirst()

        val sucursal = Sucursal()

        if (exists) {
            val id = result.getString(0) // Columna indice 0 -> ID
            val city = result.getString(1) // Columna indice 1 -> NAME
            val address = result.getString(2) // Columna indice 2 -> GENRE
            val techService = result.getInt(3) == 1 // Columna indice 3 -> IS_FINISHED
            val employeesNumber = result.getInt(4) // Columna indice 4 -> SEASONS
            val openDate = result.getString(5) // Columna indice 5 -> EMISSION_DATE
            val sucursalId = result.getString(6) // Columna indice 6 -> STREAMING_SERVICE_ID

            if (id != null) {
                sucursal.setId(id)
                sucursal.setCiudad(city)
                sucursal.setDireccion(address)
                sucursal.setServicioTecnico(techService)
                sucursal.setNumeroEmpleados(employeesNumber)
                sucursal.setFechaApertura(openDate)
                sucursal.setSupermercadoId(sucursalId)
            }
        }

        result.close()
        readableDatabase.close()

        return sucursal
    }

    fun create(data: SucursalDto): Boolean {
        val writableDatabase = writableDatabase

        val values = ContentValues()

        values.put("ID", generateId())
        values.put("CITY", data.ciudad)
        values.put("ADDRESS", data.direccion)
        values.put("TECH_SERVICE", data.servicioTecnico)
        values.put("EMPLOYEES_NUMBER", data.numeroEmpleados)
        values.put("OPEN_DATE", data.fechaApertura)
        values.put("SUPERMERCADO_ID", data.supermercadoId)

        val result = writableDatabase.insert(
            "SUCURSALES",
            null,
            values
        )

        writableDatabase.close()

        return result.toInt() != -1
    }

    fun update(id: String, changes: SucursalDto): Boolean {
        val writableDatabase = writableDatabase

        val values = ContentValues()

        values.put("CITY", changes.ciudad)
        values.put("ADDRESS", changes.direccion)
        values.put("TECH_SERVICE", changes.servicioTecnico)
        values.put("EMPLOYEES_NUMBER", changes.numeroEmpleados)
        values.put("OPEN_DATE", changes.fechaApertura)
        values.put("SUPERMERCADO_ID", changes.supermercadoId)

        val result = writableDatabase.update(
            "SUCURSALES",
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
            "SUCURSALES",
            "ID = ?",
            arrayOf(id.toString())
        )

        writableDatabase.close()

        return result != -1
    }
}