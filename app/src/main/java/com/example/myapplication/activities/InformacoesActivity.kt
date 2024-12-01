package com.example.myapplication.activities

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.api.ApiService
import com.example.myapplication.databinding.ActivityInformacoesBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class InformacoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesBinding
    private lateinit var service: ApiService
    private var token: String? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val dialog = Dialog(this@InformacoesActivity)
        dialog.setTitle("Erro")
        dialog.setContentView(TextView(this@InformacoesActivity).apply {
            text = throwable.message
        })
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        service = ApiService(this)
        token = intent.getStringExtra("token")

        // Buscar informações ao iniciar a Activity
        token?.let {
            lifecycleScope.launch(exceptionHandler) {
                fetchInformacoes(it)
            }
        } ?: run {
            Toast.makeText(this, "Token inválido", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun fetchInformacoes(token: String) {
        val response = service.getInformacoes(token)
        if (response.isSuccessful) {
            val informacoes = response.body()
            informacoes?.let {
                // Exibir informações no TextView
                binding.textViewInformacoes.text = it.joinToString("\n")
            }
        } else {
            Toast.makeText(this, "Erro ao buscar informações", Toast.LENGTH_SHORT).show()
        }
    }
}
