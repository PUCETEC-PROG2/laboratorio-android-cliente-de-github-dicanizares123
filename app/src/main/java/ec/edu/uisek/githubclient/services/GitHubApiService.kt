package ec.edu.uisek.githubclient.services

import retrofit2.Call
import retrofit2.http.GET
import ec.edu.uisek.githubclient.models.Repo

interface GitHubApiService {
    @GET("/user/repos")
    fun getRepost(): Call<List<Repo>>
}