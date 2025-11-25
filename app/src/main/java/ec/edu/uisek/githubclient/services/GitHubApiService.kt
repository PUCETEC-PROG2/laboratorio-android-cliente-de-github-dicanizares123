package ec.edu.uisek.githubclient.services

import retrofit2.Call
import retrofit2.http.GET
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest

import retrofit2.http.Body
import retrofit2.http.POST

interface GitHubApiService {
    @GET("/user/repos")
    fun getRepost(): Call<List<Repo>>

    @POST("/user/repos")
    fun addRepository(
        @Body repoRequest: RepoRequest
    ): Call<Repo>

}