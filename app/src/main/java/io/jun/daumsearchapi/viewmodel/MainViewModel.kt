package io.jun.daumsearchapi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.jun.daumsearchapi.model.Image
import io.jun.daumsearchapi.model.ImageRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ImageRepository()

    fun searchImage(keyword: String, page: Int): Observable<Image> = repository
            .searchImage(keyword, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}