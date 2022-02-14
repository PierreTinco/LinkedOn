package fr.isen.tinco.androiderestaurant.basket

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.tinco.androiderestaurant.R
import fr.isen.tinco.androiderestaurant.databinding.CellBasketBinding

class BasketAdapter(private val items: List<BasketItem>,
                    val deleteClickListener: (BasketItem) -> Unit): RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {
    lateinit var context: Context
    class BasketViewHolder(binding: CellBasketBinding): RecyclerView.ViewHolder(binding.root) {
        val dishName: TextView = binding.name
        val price: TextView = binding.price
        val quantity: TextView = binding.quantity
        val delete: ImageButton = binding.deleteButton
        val imageView: ImageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        context = parent.context
        return BasketViewHolder(CellBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val basketItem = items[position]
        holder.dishName.text = basketItem.dish.name
        holder.quantity.text = "${context.getString(R.string.quantity)} ${basketItem.quantity.toString()}"
        holder.price.text = "${basketItem.dish.prices.first().price} €"

        holder.delete.setOnClickListener {
            deleteClickListener.invoke(basketItem)
        }

        Picasso.get()
            .load(basketItem.dish.getThumbnailURL())
            .placeholder(R.drawable.android_logo_restaurant)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}