package fr.isen.tinco.androiderestaurant.basket

import android.content.Context
import com.google.gson.GsonBuilder
import fr.isen.tinco.androiderestaurant.network.Dish
import java.io.File
import java.io.Serializable

class Basket(val items: MutableList<BasketItem>): Serializable {
    var itemsCount: Int = 0
        get() {
            // compter tous les items
            // exemple : 3 salades, 5 tartares doit retourner 8

            /*
            var count = 0
            items.forEach {
                count += it.quantity
            }
            return count
            */

            val count = items.map {
                it.quantity
            }.reduceOrNull { acc, i -> acc + i }
            return count ?: 0
        }

    var totalPrice : Float = 0f
        get() {
           return items.map {
               it.quantity * it.dish.prices.first().price.toFloat()
           }.reduceOrNull { acc, fl -> acc + fl } ?: 0f
        }

    fun addItem(item: Dish, quantity: Int) {
        val existingItem = items.firstOrNull { it.dish.name == item.name }
        //val existingItem = items.filter { it.dish.name == item.name }.firstOrNull()
        existingItem?.let {
            existingItem.quantity += quantity
        } ?: run {
            val basketItem = BasketItem(item, quantity)
            items.add(basketItem)
        }
    }

    fun removeItem(basketItem: BasketItem) {
        items.remove(basketItem)
    }

    fun clear() {
        items.removeAll { true }
    }

    fun save(context: Context) {
        val jsonFile = File(context.cacheDir.absolutePath + BASKET_FILE)
        jsonFile.writeText(toJson())
        upddateCounter(context)
    }

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }

    private fun upddateCounter(context: Context) {
        val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(ITEMS_COUNT, itemsCount)
        editor.apply()
    }

    companion object {
        fun getBasket(context: Context): Basket {
            val jsonFile = File(context.cacheDir.absolutePath + BASKET_FILE)
            if(jsonFile.exists()) {
                val json = jsonFile.readText()
                return GsonBuilder().create().fromJson(json, Basket::class.java)
            } else {
                return Basket(mutableListOf())
            }
        }

        const val BASKET_FILE = "basket.json"
        const val ITEMS_COUNT = "ITEMS_COUNT"
        const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
    }
}

class BasketItem(val dish: Dish, var quantity: Int): Serializable {}