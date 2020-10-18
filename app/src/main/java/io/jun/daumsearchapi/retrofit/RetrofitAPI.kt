package io.jun.daumsearchapi.retrofit

import io.jun.daumsearchapi.model.Image
import io.reactivex.Observable
import retrofit2.http.*

interface RetrofitAPI {

    @Headers("Authorization: KakaoAK e417c4434dff0d5d22c916f2d3e06ec7")
    @GET("/v2/search/image")
    fun requestImage(@Query("query") keyword: String,
                     @Query("page") page: Int,
                     @Query("size") size: Int = 30
    ): Observable<Image>
}