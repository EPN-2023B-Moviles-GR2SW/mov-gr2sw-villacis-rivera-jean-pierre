import java.util.Date

fun main(){
    println("Hello World!")
    //INMUTABLES (No se reasignan "=")
    val inmutable: String = "Jean";
    //inmutable = "Pierre"

    // Mutables (Re asignar)
    var mutable: String = "Pierre";
    mutable = "Adrian";

// val > var
    // Duck Typing
    var ejemploVariable = "Jean Villacis"
    val edadEjemplo: Int = 12
    ejemploVariable.trim()
    //ejemploVariable = edadEjemplo;

    // Varieble primitiva
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char ='C'
    val mayorEdad: Boolean = true

    //Clases Java
    val fechaNacimiento: Date = Date()
}
