package com.example.myapplication.activities

import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.api.ApiService
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var service: ApiService

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val dialog = Dialog(this@LoginActivity)
        dialog.setTitle("Erro")
        dialog.setContentView(TextView(this@LoginActivity).apply {
            throwable.message
        })

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        service = ApiService(this)

        binding.buttonLogin.setOnClickListener {
            lifecycleScope.launch(exceptionHandler) {
                login()
            }
        }
    }

    private suspend fun login() {
        val username = binding.textInputUsername.editText?.text.toString()
        val response = service.postLogin(username)
        if (response.isSuccessful) {
            response.body()?.let {
                val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putString("token", it.token).apply()
                val intent = Intent(this, CadastroActivity::class.java)
                intent.putExtra("token", it.token)
                startActivity(intent)
                finish()
            }
        }
        else {
            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
        }
    }
}