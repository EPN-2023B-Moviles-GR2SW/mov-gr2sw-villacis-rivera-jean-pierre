package com.example.examen_ib.dtos



class SucursalDto(
    val ciudad:String,
    val direccion: String,
    val servicioTecnico: Boolean,
    val numeroEmpleados: Long,
    val fechaApertura: String,
    val supermercadoId: String
) {
}