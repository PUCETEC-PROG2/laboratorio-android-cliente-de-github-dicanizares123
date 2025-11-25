package ec.edu.uisek.githubclient

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityAddRepoBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRepoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }


    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val repoName = binding.etRepoName.text.toString().trim()
            val repoDescription = binding.etRepoDescription.text.toString().trim()

            if (repoName.isEmpty()) {
                binding.etRepoName.error = "El nombre del repositorio es obligatorio"
                return@setOnClickListener
            }

            if (repoName.contains(" ")){
                binding.etRepoName.error = "El nombre del repositorio no puede contener espacios"
                return@setOnClickListener
            }

            val repoRequest: RepoRequest = RepoRequest(
                name = repoName,
                description = repoDescription
            )

            val apiService = RetrofitClient.gitHubApiService

            val call = apiService.addRepository(repoRequest)

            call.enqueue(object : Callback<Repo>{
                override fun onResponse(call: Call<Repo?>, response: Response<Repo?>) {
                    if(response.isSuccessful){
                        Log.d("Repoform", "Repositorio creado:${repoName}")
                        showMesage("Repositorio ${repoName} creado exitosamente")
                        finish()
                    }else {
                        val errorMsg = when (response.code()){
                            401 -> "No autorizado"
                            403 -> "Prohibido"
                            404 -> "No encontrado"
                            else -> "Error desconocido"
                        }
                        Log.e("Repoform", errorMsg)
                        showMesage(errorMsg)
                    }
                }

                override fun onFailure(call: Call<Repo?>, t: Throwable) {
                    Log.e("Repoform", "Error al crear el repositorio: ${t.message}")
                    showMesage("Fallo en la red: ${t.message}")

                }
            })


        }


    }
    private fun showMesage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}


