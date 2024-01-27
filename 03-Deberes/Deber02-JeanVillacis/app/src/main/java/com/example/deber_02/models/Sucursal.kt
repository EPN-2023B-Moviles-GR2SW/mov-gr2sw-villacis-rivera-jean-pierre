package com.example.deber_02.models




class Sucursal (
    private var id: String,
    private var ciudad:String,
    private var direccion: String,
    private var servicioTecnico: Boolean,
    private var numeroEmpleados: Int,
    private var fechaApertura: String,
    private var supermercadoId: String,
){
    constructor(): this("","","",false,0, "", "")

    public fun getId(): String{
        return id
    }
    fun getCiudad():String{
        return ciudad
    }
    fun  getDireccion():String{
        return direccion
    }
    fun  getServicioTecnico():Boolean{
        return servicioTecnico
    }
    fun  getNumeroEmpleados(): Int {
        return numeroEmpleados
    }

    fun getFechaApertura(): String{
        return fechaApertura
    }

    fun setId(id: String){
        this.id = id
    }
    fun setCiudad(ciudad: String){
        this.ciudad = ciudad
    }

    fun setDireccion(direccion: String){
        this.direccion = direccion
    }

    fun setServicioTecnico(servicioTecnico: Boolean){
        this.servicioTecnico = servicioTecnico
    }

    fun setNumeroEmpleados(numeroEmpleados: Int){
        this.numeroEmpleados = numeroEmpleados
    }

    fun setFechaApertura(fechaApertura: String){
        this.fechaApertura = fechaApertura
    }

    fun getSupermercadoId(): String{
        return supermercadoId
    }

    fun setSupermercadoId(supermercadoId: String){
        this.supermercadoId = supermercadoId
    }

    override fun toString(): String {
        return "$ciudad"
    }
    fun getListOfStringFromData(): List<String> {
        return listOf(
            "Ciudad: $ciudad",
            "Direccion: $direccion",
            "Servicio Tecnico: $servicioTecnico",
            "Numero de Empleados: $numeroEmpleados",
            "Fecha Apertura: $fechaApertura",
        )
    }

}