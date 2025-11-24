package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityAddRepoBinding

class AddRepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRepoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupButtons()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
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

            // Aquí procesarías los datos (por ahora solo cerrar la activity)
            // TODO: Implementar la lógica para guardar el repositorio
            finish()
        }
    }
}

