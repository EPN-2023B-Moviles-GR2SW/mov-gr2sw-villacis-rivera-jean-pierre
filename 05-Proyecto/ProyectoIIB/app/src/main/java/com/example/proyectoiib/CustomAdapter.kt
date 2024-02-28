
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoiib.R
import com.example.proyectoiib.models.Vehículo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class CustomAdapter(private val query: Query) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var listenerRegistration: ListenerRegistration? = null
    private var data: List<DocumentSnapshot> = emptyList()

    init {
        startListening()
    }

    private fun startListening() {
        listenerRegistration = query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshots != null) {
                data = snapshots.documents
                notifyDataSetChanged()
            }
        }
    }

    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = data[position]
        val car = documentSnapshot.toObject(Vehículo::class.java)

        holder.itemMarca.text = car?.marca
        holder.itemModelo.text = car?.modelo
        holder.itemKilometraje.text = car?.kilometraje
        holder.itemPrecio.text = car?.precio
        holder.itemCiudad.text = car?.ciudad

        val imageUrl = car?.imageUrl

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemMarca: TextView = itemView.findViewById(R.id.item_title)
        var itemModelo: TextView = itemView.findViewById(R.id.item_detal)
        var itemKilometraje: TextView = itemView.findViewById(R.id.item_kilometraje)
        var itemPrecio: TextView = itemView.findViewById(R.id.item_precio)
        var itemCiudad: TextView = itemView.findViewById(R.id.item_ciudad)
    }
}