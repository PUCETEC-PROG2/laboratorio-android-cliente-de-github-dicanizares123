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
    private var isEditMode: Boolean = false
    private var repoName: String = ""
    private var repoOwner: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadIntentData()
        setupButtons()
    }

    private fun loadIntentData() {
        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false)

        if (isEditMode) {
            repoName = intent.getStringExtra("REPO_NAME") ?: ""
            repoOwner = intent.getStringExtra("REPO_OWNER") ?: ""
            val repoDescription = intent.getStringExtra("REPO_DESCRIPTION") ?: ""

            binding.etRepoName.setText(repoName)
            binding.etRepoDescription.setText(repoDescription)

            // Deshabilitar edición del nombre
            binding.etRepoName.isEnabled = false
            binding.etRepoName.alpha = 0.6f
        }
    }


    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (isEditMode) {
                updateRepository()
            } else {
                createRepository()
            }
        }
    }

    private fun createRepository() {
        val repoName = binding.etRepoName.text.toString().trim()
        val repoDescription = binding.etRepoDescription.text.toString().trim()

        if (repoName.isEmpty()) {
            binding.etRepoName.error = "El nombre del repositorio es obligatorio"
            return
        }

        if (repoName.contains(" ")) {
            binding.etRepoName.error = "El nombre del repositorio no puede contener espacios"
            return
        }

        val repoRequest = RepoRequest(
            name = repoName,
            description = repoDescription
        )

        val apiService = RetrofitClient.gitHubApiService
        val call = apiService.addRepository(repoRequest)

        call.enqueue(object : Callback<Repo> {
            override fun onResponse(call: Call<Repo?>, response: Response<Repo?>) {
                if (response.isSuccessful) {
                    Log.d("Repoform", "Repositorio creado: $repoName")
                    showMesage("Repositorio $repoName creado exitosamente")
                    finish()
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "No autorizado"
                        403 -> "Prohibido"
                        404 -> "No encontrado"
                        422 -> "El repositorio ya existe"
                        else -> "Error desconocido: ${response.code()}"
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

    private fun updateRepository() {
        val repoDescription = binding.etRepoDescription.text.toString().trim()

        val repoRequest = RepoRequest(
            name = repoName,
            description = repoDescription
        )

        val apiService = RetrofitClient.gitHubApiService
        val call = apiService.updateRepository(repoOwner, repoName, repoRequest)

        call.enqueue(object : Callback<Repo> {
            override fun onResponse(call: Call<Repo?>, response: Response<Repo?>) {
                if (response.isSuccessful) {
                    Log.d("Repoform", "Repositorio actualizado: $repoName")
                    showMesage("Repositorio $repoName actualizado exitosamente")
                    finish()
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "No autorizado"
                        403 -> "Prohibido"
                        404 -> "Repositorio no encontrado"
                        else -> "Error desconocido: ${response.code()}"
                    }
                    Log.e("Repoform", errorMsg)
                    showMesage(errorMsg)
                }
            }

            override fun onFailure(call: Call<Repo?>, t: Throwable) {
                Log.e("Repoform", "Error al actualizar el repositorio: ${t.message}")
                showMesage("Fallo en la red: ${t.message}")
            }
        })
    }
    private fun showMesage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}


