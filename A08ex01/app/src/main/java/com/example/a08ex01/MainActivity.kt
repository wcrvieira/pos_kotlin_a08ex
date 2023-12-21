package com.example.a08ex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // cria o objeto 'service', especificando a URL e a interface
        val service = Retrofit.Builder()
            .baseUrl("https://randomuser.me/") // URL usada
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserService::class.java) // nossa interface

        // busca os usuários via WS
        service.getUsers().enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("TAG_", "Houve um erro!")
                t.printStackTrace()
            }
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                // cria uma lista de usuários
                val users: List<User> = response.body()!!.results

                // busca os emails de todos os usuários
                var usuarios = "Usuários:\n"
                for (u in users)
                    usuarios += "${u.name.first} ${u.name.last} (${u.email})\n"

                // preenche o TextView
                findViewById<TextView>(R.id.txtMostra).text = usuarios
            }
        })

    }
}

// controller
data class UserResponse(val results: List<User>)

// models
data class User(val name: Name, val email: String, val phone: String)
data class Name(val title: String, val first: String, val last: String)

// interface de mapeamento do WS
interface UserService {
    @GET("/api/?results=10")
    fun getUsers(): Call<UserResponse>
}