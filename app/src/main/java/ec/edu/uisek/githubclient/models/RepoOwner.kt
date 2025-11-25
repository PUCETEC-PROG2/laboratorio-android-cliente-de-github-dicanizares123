package ec.edu.uisek.githubclient.models

import com.google.gson.annotations.SerializedName

data class RepoOwner(
    val id: Long,
    @SerializedName("login")
    val Login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
