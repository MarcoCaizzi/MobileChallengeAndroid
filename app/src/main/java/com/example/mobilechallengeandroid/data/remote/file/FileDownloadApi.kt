package com.example.mobilechallengeandroid.data.remote.file

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface FileDownloadApi {
    @GET
    suspend fun downloadFile(@Url fileUrl: String= "cities.json"): ResponseBody
}