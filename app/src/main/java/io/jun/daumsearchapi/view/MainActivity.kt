package io.jun.daumsearchapi.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import io.jun.daumsearchapi.R
import io.jun.daumsearchapi.adapter.ImageAdapter
import io.jun.daumsearchapi.model.Image
import io.jun.daumsearchapi.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var imageAdapter: ImageAdapter

    var page by Delegates.notNull<Int>()
    lateinit var latestKeyword: String
    lateinit var imageInfo: Image

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setView()
        subscribeSearchText()
    }

    private fun subscribeSearchText() =
        search_editText.textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe { charSequence ->
                if(!charSequence.isNullOrBlank()) {
                    page = 1
                    latestKeyword = charSequence.toString()

                    compositeDisposable.clear()
                    compositeDisposable.add(searchImage(latestKeyword, page))
                }
            }

    private fun searchImage(keyword: String, page: Int) =
        ViewModelProvider(this).get(MainViewModel::class.java)
            .searchImage(keyword, page).subscribe({
                if (it.documents.isNullOrEmpty()) {
                    Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show()
                }
                imageInfo = it
                imageAdapter.setImageDocs(it.documents)
            }, { throwable ->
                Toast.makeText(this, throwable.toString(), Toast.LENGTH_LONG).show()
            })

    private fun addImage(keyword: String, page: Int) =
        ViewModelProvider(this).get(MainViewModel::class.java)
            .searchImage(keyword, page).subscribe({
                imageInfo = it
                imageAdapter.addImageDocs(it.documents)
            }, { throwable ->
                Toast.makeText(this, throwable.toString(), Toast.LENGTH_LONG).show()
            })

    private fun setView() {
        imageAdapter = ImageAdapter(this)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = imageAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    if(::imageInfo.isInitialized &&
                        !imageInfo.meta.is_end &&
                        !recyclerView.canScrollVertically(1)) { //스크롤 최하단 부분이라면

                        compositeDisposable.clear()
                        showLoading()
                        compositeDisposable.add(addImage(latestKeyword, ++page))
                    }
                }
            })
        }
    }

    //이미지 추가로 30개 가져올 때 보여줄 로딩효과
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE

        val timer = Observable.timer(650, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { progressBar.visibility = View.GONE }

        compositeDisposable.add(timer)
    }
}