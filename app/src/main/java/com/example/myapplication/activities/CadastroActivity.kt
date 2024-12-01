package com.example.myapplication.activities

import com.example.myapplication.databinding.ActivityCadastroBinding
import com.example.myapplication.api.ApiService
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CadastroActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var service: ApiService
    private var token: String? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val dialog = Dialog(this@CadastroActivity)
        dialog.setTitle("Erro")
        dialog.setContentView(TextView(this@CadastroActivity).apply {
            throwable.message
        })

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.getStringExtra("token")
        service = ApiService(this)

        binding.buttonAdicionarInformacao.setOnClickListener {
            val informacao = binding.textInputInformacao.editText?.text.toString()

            if (informacao.isNotEmpty()) {
                lifecycleScope.launch(exceptionHandler) {
                    adicionar(informacao)
                }
            }
            else {
                Toast.makeText(this, "Preencha o campo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonVisualizarInformacoes.setOnClickListener {
            val intent = Intent(this, InformacoesActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }

    private suspend fun adicionar(informacao: String) {
        token?.let {
            val response = service.addInformacao(it, informacao)
            if (response.isSuccessful) {
                Toast.makeText(this, "Informação adicionada com sucesso", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Erro ao adicionar informação", Toast.LENGTH_SHORT).show()
            }
        } ?:run {
            Toast.makeText(this, "Erro no token", Toast.LENGTH_SHORT).show()
        }
    }

}