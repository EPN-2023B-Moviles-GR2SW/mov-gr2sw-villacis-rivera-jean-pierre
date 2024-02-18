package com.example.examen_ib.db
import com.example.examen_ib.dtos.SucursalDto
import com.example.examen_ib.models.Sucursal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class SucursalFirestore {

        val db = Firebase.firestore

        companion object {
            fun createSucursalFromDocument(document: QueryDocumentSnapshot): Sucursal {
                val id = document.id
                val ciudad = document.data["ciudad"] as String?
                val direccion = document.data["direccion"] as String?
                val servicioTecnico = document.data["servicioTecnico"] as Boolean?
                val numeroEmpleados = document.data["numeroEmpleados"] as Long?
                val fechaApertura = document.data["fechaApertura"] as String?
                val supermercadoId = document.data["supermercadoId"] as String?

                if (
                    id == null || ciudad == null || direccion == null ||
                    servicioTecnico == null || numeroEmpleados == null ||
                    fechaApertura == null || supermercadoId == null
                ) {
                    return Sucursal()
                }

                return Sucursal(id, ciudad, direccion, servicioTecnico, numeroEmpleados, fechaApertura, supermercadoId)
            }

            fun createSucursalFromDocument(document: DocumentSnapshot): Sucursal {
                val id = document.id
                val ciudad = document.data?.get("ciudad") as String?
                val direccion = document.data?.get("direccion") as String?
                val servicioTecnico = document.data?.get("servicioTecnico") as Boolean?
                val numeroEmpleados = document.data?.get("numeroEmpleados") as Long?
                val fechaApertura = document.data?.get("fechaApertura") as String?
                val supermercadoId = document.data?.get("supermercadoId") as String?

                if (
                    id == null || ciudad == null || direccion == null ||
                    servicioTecnico == null || numeroEmpleados == null ||
                    fechaApertura == null || supermercadoId == null
                ) {
                    return Sucursal()
                }

                return Sucursal(id, ciudad, direccion, servicioTecnico, numeroEmpleados, fechaApertura, supermercadoId)
            }
        }

        fun getAllBySupermercado(supermercadoId: String): Task<QuerySnapshot> {
            return db.collection("sucursales")
                .whereEqualTo("supermercadoId", supermercadoId)
                .get()
        }

        fun create(data: SucursalDto): Boolean {
            val sucursalData = hashMapOf(
                "ciudad" to data.ciudad,
                "direccion" to data.direccion,
                "servicioTecnico" to data.servicioTecnico,
                "numeroEmpleados" to data.numeroEmpleados,
                "fechaApertura" to data.fechaApertura,
                "supermercadoId" to data.supermercadoId
            )
            var result = false
            db.collection("sucursales")
                .add(sucursalData)
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener {
                    result = false
                }
            return result
        }

        fun update(id: String, data: SucursalDto): Boolean {
            val sucursalData = hashMapOf(
                "ciudad" to data.ciudad,
                "direccion" to data.direccion,
                "servicioTecnico" to data.servicioTecnico,
                "numeroEmpleados" to data.numeroEmpleados,
                "fechaApertura" to data.fechaApertura,
                "supermercadoId" to data.supermercadoId
            )
            var result = false
            db.collection("sucursales")
                .document(id)
                .set(sucursalData)
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener {
                    result = false
                }
            return result
        }

        fun remove(id: String): Boolean {
            var result = false
            db.collection("sucursales")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener {
                    result = false
                }
            return result
        }
    }