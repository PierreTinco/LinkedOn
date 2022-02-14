package fr.isen.tinco.androiderestaurant.User

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.tinco.androiderestaurant.R
import fr.isen.tinco.androiderestaurant.databinding.ActivityUserBinding
import fr.isen.tinco.androiderestaurant.network.NetworkConstants
import fr.isen.tinco.androiderestaurant.network.User
import fr.isen.tinco.androiderestaurant.network.UserResult
import org.json.JSONObject

interface UserActivityFragmentInteraction {
    fun showLogin()
    fun showRegister()
    fun makeRequest(email: String?, password: String?, firstname: String?, lastname: String?, isFromLogin: Boolean)
}

class UserActivity : AppCompatActivity(), UserActivityFragmentInteraction {
    lateinit var binding: ActivityUserBinding
    val loginFragment = LoginFragment()
    val registerFragment = RegisterFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add parce que pas de fragment existant dans le container
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, registerFragment).commit()
    }

    override fun showLogin() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit()
    }

    override fun showRegister() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, registerFragment).commit()
    }

    override fun makeRequest(
        email: String?,
        password: String?,
        firstname: String?,
        lastname: String?,
        isFromLogin: Boolean
    ) {
        if(verifyInformation(email, password, firstname, lastname, isFromLogin)) {
            launchRequest(email, password, firstname, lastname, isFromLogin)
        } else {
            Toast.makeText(this, getString(R.string.invalidForms), Toast.LENGTH_LONG).show()
        }
    }

    private fun launchRequest(email: String?,
                              password: String?,
                              firstname: String?,
                              lastname: String?,
                              isFromLogin: Boolean) {
        val queue = Volley.newRequestQueue(this)
        var requestPath = NetworkConstants.BASE_URL
        if(isFromLogin) {
            requestPath += NetworkConstants.LOGIN
        } else {
            requestPath += NetworkConstants.REGISTER
        }

        val parameters = JSONObject()
        parameters.put(NetworkConstants.KEY_SHOP, NetworkConstants.SHOP)
        parameters.put(NetworkConstants.KEY_EMAIL, email)
        parameters.put(NetworkConstants.KEY_PASSWORD, password)
        if(!isFromLogin) {
            parameters.put(NetworkConstants.KEY_FIRSTNAME, firstname)
            parameters.put(NetworkConstants.KEY_LASTNAME, lastname)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            requestPath,
            parameters,
            {
                Log.d("request", it.toString(2))
                val userResult = GsonBuilder().create().fromJson(it.toString(), UserResult::class.java)
                if (userResult.data != null) {
                    saveUser(userResult.data)
                } else {
                    Toast.makeText(this, "mauvais login", Toast.LENGTH_LONG).show()
                }
            },
            {
                // failure
                Log.d("request", it.message ?: "une erreur s'est produite")
            }
        )
        queue.add(request)
    }

    private fun saveUser(user: User) {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(ID_USER, user.id)
        editor.apply()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun verifyInformation(
        email: String?,
        password: String?,
        firstname: String?,
        lastname: String?,
        isFromLogin: Boolean
    ): Boolean {
        var verified = (email?.isNotEmpty() == true && password?.count() ?: 0 >= 6)

        if(!isFromLogin) {
            verified = verified && (firstname?.isNotEmpty() == true && lastname?.isNotEmpty() == true)
        }
        return verified
    }

    companion object {
        const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
        const val ID_USER = "ID_USER"
    }
}