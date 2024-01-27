package com.example.deber_06.models

class Supermercado(
    private var id: String,
    private var ruc:String,
    private var nombre: String,
    private var telefono: String,
    private var vendeTecnologia: Boolean,
    private var sucursales: MutableList<Sucursal>,
){
    constructor():this("","","","", true,  mutableListOf())

    fun getId(): String {
        return id
    }

    fun getRuc(): String {
        return ruc
    }
    fun getNombre(): String {
        return nombre
    }
    fun getTelefono(): String {
        return telefono
    }
    fun getVendeTecnologia(): Boolean{
        return vendeTecnologia
    }
    fun getSucursales(): MutableList<Sucursal>{
        return sucursales
    }
    fun  agregarSucursales(sucursales: Sucursal){
        this.sucursales.add(sucursales)
    }
    fun removerSucursales(sucursales: Sucursal){
        this.sucursales = this.sucursales.filter { it.getId() != sucursales.getId() }.toMutableList()
    }

    fun setId(id: String) {
        this.id = id
    }

    fun setRuc(ruc: String) {
        this.ruc = ruc
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun setTelefono(telefono: String) {
        this.telefono = telefono
    }

    fun setVendeTecnologia(vendeTecnologia: Boolean) {
        this.vendeTecnologia = vendeTecnologia
    }

    fun setSucursales(sucursales: MutableList<Sucursal>) {
        this.sucursales = sucursales
    }

    override fun toString(): String {
        return this.nombre
    }
    fun getListOfStringFromData(): List<String>{
        return listOf(
            "Nombre: $nombre",
            "Telefono: $telefono",
            "Vende Tecnologia: $vendeTecnologia",
            "Sucursales: ${sucursales.map { it.getCiudad() }}",
        )
    }
}