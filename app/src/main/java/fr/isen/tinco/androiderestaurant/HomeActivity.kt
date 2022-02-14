package fr.isen.tinco.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import fr.isen.tinco.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenClick()

        Log.d("life cycle", "HomeActivity onCreate")
    }

    private fun listenClick() {
        binding.buttonStarter.setOnClickListener {
            showCategory(LunchType.STARTER)
        }
        binding.buttonMain.setOnClickListener {
            showCategory(LunchType.MAIN)
        }
        binding.buttonFinish.setOnClickListener {
            showCategory(LunchType.FINISH)
        }
    }

    private fun showCategory(item: LunchType) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryType, item)
        startActivity(intent)
    }

    override fun onDestroy() {
        Log.d("life cycle", "HomeActivity onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("life cycle", "HomeActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("life cycle", "HomeActivity onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d("life cycle", "HomeActivity onStart")
    }

    override fun onStop() {
        Log.d("life cycle", "HomeActivity onStop")
        super.onStop()
    }

    companion object {
        const val CategoryType = "CategoryType"
    }
}