package com.example.proyectoiib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class FourthActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private lateinit var storage: FirebaseStorage

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourth_layout)

        storage = FirebaseStorage.getInstance()

        val icono_regresar = findViewById<ImageView>(R.id.icono_regresar)
        icono_regresar.setOnClickListener {
            finish()
        }

        val galeria = findViewById<ImageView>(R.id.galeria)
        galeria.setOnClickListener {
            abrirGaleria()
        }

        val camara = findViewById<ImageView>(R.id.camara)
        camara.setOnClickListener {
            abrirCamara()
        }

        val botonHome = findViewById<LinearLayout>(R.id.home)
        botonHome.setOnClickListener {
            irActividad(MainActivity::class.java)
        }

        val botonFinalizar = findViewById<Button>(R.id.finalizar)
        botonFinalizar.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imageUri = obtenerUriDesdeBitmap(imageBitmap)
                    imageUri?.let {
                        guardarImagenEnStorage(imageBitmap, it)
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                        guardarImagenEnStorage(imageBitmap, it)
                    }
                }
            }
        }
    }

    private fun obtenerUriDesdeBitmap(bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }


    @SuppressLint("WrongViewCast")
    private fun mostrarImagenEnTabla(bitmap: Bitmap) {
        val imageView = ImageView(this)
        imageView.setImageBitmap(bitmap)

        val tableRow = findViewById<LinearLayout>(R.id.foto1)
        tableRow.addView(imageView)
    }

    private fun guardarImagenEnStorage(bitmap: Bitmap, imageUri: Uri) {
        val storageRef = storage.reference

        val imageName = obtenerNombreArchivo(imageUri)

        val imagesRef = storageRef.child("images/$imageName")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                val lastDocumentRef = FirebaseFirestore.getInstance().collection("Vehículo").orderBy("imageUrl", Query.Direction.ASCENDING).limit(1)

                Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val resizedBitmap = resizeBitmap(resource)
                            mostrarImagenEnTabla(resizedBitmap)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Handle resource cleared
                        }
                    })

                lastDocumentRef.get().addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val lastDocument = querySnapshot.documents[0]
                        lastDocument.reference.update("imageUrl", imageUrl)
                            .addOnSuccessListener {
                            }.addOnFailureListener { exception ->
                                Log.e("Firestore", "Error updating document", exception)
                            }
                    }
                }.addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting documents: ", exception)
                }
            }.addOnFailureListener {
            }
        }
    }


    @SuppressLint("Range")
    private fun obtenerNombreArchivo(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                cursor.close()
                return displayName
            }
        }
        return ""
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val newWidth = 550
        val newHeight = 550

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}