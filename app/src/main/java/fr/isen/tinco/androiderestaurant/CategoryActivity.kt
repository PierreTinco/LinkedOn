package fr.isen.tinco.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.tinco.androiderestaurant.Detail.DetailActivity
import fr.isen.tinco.androiderestaurant.databinding.ActivityCategoryBinding
import fr.isen.tinco.androiderestaurant.network.Dish
import fr.isen.tinco.androiderestaurant.network.MenuResult
import fr.isen.tinco.androiderestaurant.network.NetworkConstants
import org.json.JSONObject

enum class LunchType {
    STARTER, MAIN, FINISH;

    companion object {
        fun getResString(type: LunchType): Int {
            return when(type) {
                STARTER -> R.string.starters
                MAIN -> R.string.main
                FINISH -> R.string.finish
            }
        }

        fun getCategoryTitle(type: LunchType): String {
            return when(type) {
                STARTER -> "Entrées"
                MAIN -> "Plats"
                FINISH -> "Desserts"
            }
        }
    }
}

class CategoryActivity : BaseActivity() {
    lateinit var binding: ActivityCategoryBinding
    lateinit var currentCategory: LunchType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentCategory = intent.getSerializableExtra(HomeActivity.CategoryType) as? LunchType ?: LunchType.STARTER
        setupTitle()
        makeRequest()
        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun makeRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = NetworkConstants.BASE_URL + NetworkConstants.MENU
        val paramters = JSONObject()
        paramters.put(NetworkConstants.KEY_SHOP, NetworkConstants.SHOP)

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            paramters,
            {
                parseResult(it.toString())
            },
            {
                Log.d("Volley error", "$it")
            }
        )

        queue.add(request)
    }

    private fun parseResult(response: String) {
        val result = GsonBuilder().create().fromJson(response, MenuResult::class.java)
        val items = result.data.firstOrNull {
            it.name == LunchType.getCategoryTitle(currentCategory)
        }?.items
        items?.let {
            loadList(it)
        }
    }

    private fun setupTitle() {
        binding.title.text = getString(LunchType.getResString(currentCategory))
    }

    private fun loadList(items: List<Dish>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DishesAdapter(items) { selectedItem ->
            showDetail(selectedItem)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun showDetail(item: Dish) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(CategoryActivity.SELECTED_ITEM, item)
        startActivity(intent)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    companion object {
        const val SELECTED_ITEM = "SELECTED_ITEM"
    }
}