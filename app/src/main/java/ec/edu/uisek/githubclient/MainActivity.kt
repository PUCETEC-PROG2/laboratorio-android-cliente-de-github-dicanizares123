package ec.edu.uisek.githubclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()
        setupFabButton()
    }

    override fun onResume(){
        super.onResume()
        fetchRepositories()
    }

    private fun setupRecycleView(){
        reposAdapter = ReposAdapter(
            onEditClick = { repo -> openEditRepo(repo) },
            onDeleteClick = { repo -> deleteRepo(repo) }
        )
        binding.repoRecyclerView.adapter = reposAdapter
    }

    private fun fetchRepositories(){
        val apiService = RetrofitClient.gitHubApiService
        val call = apiService.getRepost()

        call.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos != null) {
                        reposAdapter.updateRepositories(repos)
                    }else {
                        showMessage("No se encontraron repositorios")
                    }

                } else  {
                   val errorMsg = when (response.code()){
                       401 -> "No autorizado"
                       403 -> "Prohibido"
                       404 -> "No encontrado"
                        else -> "Error desconocido"
                   }
                    Log.e("MainActivity", errorMsg)
                    showMessage(errorMsg)
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                showMessage("Fallo en la red")
                Log.e("MainActivity", "Fallo en la red ${t.message}")
            }
        })
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupFabButton(){
        binding.btAdd.setOnClickListener{
            val intent = Intent(this, AddRepoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openEditRepo(repo: Repo) {
        val intent = Intent(this, AddRepoActivity::class.java)
        intent.putExtra("REPO_NAME", repo.name)
        intent.putExtra("REPO_DESCRIPTION", repo.description)
        intent.putExtra("REPO_OWNER", repo.owner.Login)
        intent.putExtra("IS_EDIT_MODE", true)
        startActivity(intent)
    }

    private fun deleteRepo(repo: Repo) {
        val apiService = RetrofitClient.gitHubApiService
        val call = apiService.deleteRepository(repo.owner.Login, repo.name)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showMessage("Repositorio ${repo.name} eliminado exitosamente")
                    fetchRepositories()
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "No autorizado"
                        403 -> "Prohibido"
                        404 -> "No encontrado"
                        else -> "Error al eliminar: ${response.code()}"
                    }
                    Log.e("MainActivity", errorMsg)
                    showMessage(errorMsg)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showMessage("Fallo en la red al eliminar")
                Log.e("MainActivity", "Fallo en la red ${t.message}")
            }
        })
    }
}
