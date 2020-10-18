package io.jun.daumsearchapi.model

import io.jun.daumsearchapi.retrofit.RetrofitAPI
import io.jun.daumsearchapi.retrofit.RetrofitCreator
import io.reactivex.Observable

class ImageRepository {

    private val service: RetrofitAPI = RetrofitCreator.createRetrofit().create(RetrofitAPI::class.java)

    fun searchImage(keyword: String, page: Int): Observable<Image> = service.requestImage(keyword, page)
}