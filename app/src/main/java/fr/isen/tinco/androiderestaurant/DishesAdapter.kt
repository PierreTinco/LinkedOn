package fr.isen.tinco.androiderestaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.tinco.androiderestaurant.databinding.CellDishBinding
import fr.isen.tinco.androiderestaurant.network.Dish

class DishesAdapter(val items: List<Dish>,
                    val itemClickListener: (Dish) -> Unit)
    : RecyclerView.Adapter<DishesAdapter.DishViewHolder>() {

    class DishViewHolder(binding: CellDishBinding): RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.title
        val price: TextView = binding.price
        val image: ImageView = binding.imageView
        val layout: CardView = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = CellDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.price.text = "${item.prices.first().price} €"
        //holder.price.text = item.prices.first().price + " €"
        Picasso
            .get()
            .load(item.getThumbnailURL())
            .placeholder(R.drawable.android_logo_restaurant)
            .into(holder.image)
        holder.layout.setOnClickListener {
            itemClickListener.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}