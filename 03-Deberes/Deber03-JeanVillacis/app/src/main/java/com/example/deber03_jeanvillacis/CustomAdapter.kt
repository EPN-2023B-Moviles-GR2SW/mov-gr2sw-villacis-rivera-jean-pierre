package com.example.deber03_jeanvillacis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
val titles = arrayOf("Toyota","Kia","Chevrolet","Volkswagen")

    val details = arrayOf("4 Runner-2006","Carnival-2020","Spark Lige - 2018","Amarok -2011")
    val  imagens = intArrayOf(R.mipmap.runner,
        R.mipmap.carnival,
        R.mipmap.spark,
        R.mipmap.amarok,)
    val kilometraje = arrayOf("103.000 Kms","217.000 kms","25.000 kms","30.000 kms")
    val precio = arrayOf("$30.000/Negociable","$8.000/Negociable","10.000/Negociable","11.000/Negociable")
    val ciudad = arrayOf("Quito","Ibarra","Ambato","Guaranda")

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView
        var itemTitle:TextView
        var itemDetails:TextView
        var itemKilometraje:TextView
        var itemPrecio:TextView
        var itemCiudad:TextView
        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetails = itemView.findViewById(R.id.item_detal)
            itemKilometraje = itemView.findViewById(R.id.item_kilometraje)
            itemPrecio= itemView.findViewById(R.id.item_precio)
            itemCiudad = itemView.findViewById(R.id.item_ciudad)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout,viewGroup,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder:ViewHolder, i: Int) {
        viewHolder.itemTitle.text=titles[i]
        viewHolder.itemDetails.text=details[i]
        viewHolder.itemImage.setImageResource(imagens[i])
        viewHolder.itemKilometraje.text=kilometraje[i]
        viewHolder.itemPrecio.text=precio[i]
        viewHolder.itemCiudad.text=ciudad[i]

    }
    override fun getItemCount(): Int {
        return titles.size

    }

}
