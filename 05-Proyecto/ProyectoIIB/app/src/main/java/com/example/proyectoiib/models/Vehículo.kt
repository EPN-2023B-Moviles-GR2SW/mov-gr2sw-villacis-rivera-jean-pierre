package com.example.proyectoiib.models

import com.google.firebase.firestore.DocumentReference

data class Vehículo(
    val id_auto: Int? = null,
    val marca: String? = null,
    val modelo: String? = null,
    val kilometraje: String? = null,
    val precio: String? = null,
    val ciudad: String? = null,
    val tipo_vehículo: String? = null,
    val imageUrl: String? = null
)
