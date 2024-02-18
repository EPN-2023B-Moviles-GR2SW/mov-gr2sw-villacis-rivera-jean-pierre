package com.example.examen_ib.db
import com.example.examen_ib.dtos.SupermercadoDto
import com.example.examen_ib.models.Sucursal
import com.example.examen_ib.models.Supermercado
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class SupermercadoFirestore {


    val db = Firebase.firestore

    companion object {
        fun createSupermercadoFromDocument(document: QueryDocumentSnapshot): Supermercado {
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

        fun createSupermercadoFromDocument(document: DocumentSnapshot): Supermercado {
            val id = document.id
            val ruc = document.data?.get("ruc") as String?
            val name = document.data?.get("nombre") as String?
            val phone = document.data?.get("telefono") as String?
            val vendeTecnologia = document.data?.get("vendeTecnologia") as Boolean?
            val sucursal = mutableListOf<Sucursal>()

            if (id == null || name == null || ruc == null || phone == null || vendeTecnologia == null) {
                return Supermercado()
            }

            return Supermercado(id, ruc, name, phone, vendeTecnologia, sucursal)
        }
    }

    fun getAll(): Task<QuerySnapshot> {
        return db.collection("supermercados")
            .get()
    }

    fun getOne(id: String): Task<DocumentSnapshot> {
        return db.collection("supermercados")
            .document(id)
            .get()
    }

    fun create(supermercado: SupermercadoDto) {
        val supermercadoData = hashMapOf(
            "ruc" to supermercado.ruc,
            "nombre" to supermercado.nombre,
            "telefono" to supermercado.telefono,
            "vendeTecnologia" to supermercado.vendeTecnologia
        )

        db.collection("supermercados")
            .add(supermercadoData)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    fun update(id: String, supermercado: SupermercadoDto) {
        val supermercadoData = hashMapOf(
            "ruc" to supermercado.ruc,
            "nombre" to supermercado.nombre,
            "telefono" to supermercado.telefono,
            "vendeTecnologia" to supermercado.vendeTecnologia
        )

        db.collection("supermercados")
            .document(id)
            .set(supermercadoData)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                println("Error updating document: $e")
            }
    }

    fun remove(id: String) {
        db.collection("supermercados")
            .document(id)
            .delete()
            .addOnSuccessListener {
                println("DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                println("Error deleting document: $e")
            }
    }

}