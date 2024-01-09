package com.example.b2023gr2sw

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.b2023gr2sw.R.id.btn_ciclo_vida
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    val callbackContenidoIntetExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if (result.resultCode ==Activity.RESULT_OK){
                if(result.data!= null) {
                    //Logica Negocio
                    val data = result.data
                    mostrarSnackbar(
                        "${data?.getStringArrayExtra("nombreModificado")}"
                    )
                }
            }
        }

    fun mostrarSnackbar(texto:String){
     Snackbar
         .make(
             findViewById(R.id.id_layou_main),//view
            texto, //texto
             Snackbar.LENGTH_LONG) //tiempo

    }

    val callbackIntentPickUri =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if (result.resultCode == RESULT_OK){
                    if (result.data != null){
                        if (result.data !!.data != null){
                            val uri:Uri = result.data!!.data!!
                            val cursor = contentResolver.query(
                                uri,null,null,null,null)
                            cursor?.moveToFirst()
                            val indiceTelefono = cursor?.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                            val telefono = cursor?.getString(indiceTelefono!!)
                            cursor?.close()
                            mostrarSnackbar("Telefono ${telefono}")
                        }
                    }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView (R.layout.activity_main)
       EBaseDatos.tablaEntrenador=ESqliteHelperEntrenador(
           this
       )

        val botonCicloVida = findViewById<Button>(btn_ciclo_vida)
        botonCicloVida
            .setOnClickListener{
                irActividad(ACicloVida::class.java)
            }
        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView
            .setOnClickListener {
                irActividad(BListView::class.java)
            }
        val botonIntentImplicito = findViewById<Button>(
            R.id.btn_ir_intent_implicito)
        botonIntentImplicito
            .setOnClickListener{
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackIntentPickUri.launch(intentConRespuesta)
            }

        val botonIntentExplicito = findViewById<Button>(
            R.id.btn_ir_intent_explicito)
        botonIntentExplicito
            .setOnClickListener {
                abrirActividadConParametros(
                    CIntentExplicitoParametros::class.java)

            }
        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite
            .setOnClickListener{
                irActividad(ECrudEntrenador::class.java)
            }
        val botonRView = findViewById<Button>(R.id.btn_revcycle_view)
        botonRView
            .setOnClickListener{
                irActividad(FRecyclerView::class.java)
            }
    }
   fun abrirActividadConParametros(
       clase: Class<*>
   ){
       val intentExplicito = Intent(this,clase)
       //Enviar parametros solamente variables primitivas
       intentExplicito.putExtra("nombre","Adrian")
       intentExplicito.putExtra("apellido","Eguez")
       intentExplicito.putExtra("edad",34)
       callbackContenidoIntetExplicito.launch(intentExplicito)
   }

    fun irActividad(
        clase:Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}
