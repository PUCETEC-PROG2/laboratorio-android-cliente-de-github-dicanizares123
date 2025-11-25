package ec.edu.uisek.githubclient.services

import retrofit2.Call
import retrofit2.http.GET
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubApiService {
    @GET("/user/repos")
    fun getRepost(): Call<List<Repo>>

    @POST("/user/repos")
    fun addRepository(
        @Body repoRequest: RepoRequest
    ): Call<Repo>

    @PATCH("/repos/{owner}/{repo}")
    fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Body repoRequest: RepoRequest
    ): Call<Repo>

    @DELETE("/repos/{owner}/{repo}")
    fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Call<Void>
}