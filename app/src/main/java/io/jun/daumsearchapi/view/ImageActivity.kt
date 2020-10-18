package io.jun.daumsearchapi.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import io.jun.daumsearchapi.R
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        setView()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun setView() {

        Glide.with(this)
            .load(intent.getStringExtra("image_url"))
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(imageView)

        val sitename = intent.getStringExtra("display_sitename")
        val datetime = intent.getStringExtra("datetime")

        sitename_textView.visibility = if (sitename.isNullOrEmpty()) View.GONE else View.VISIBLE
        datetime_textView.visibility = if (datetime.isNullOrEmpty()) View.GONE else View.VISIBLE

        sitename_textView.text = String.format(getString(R.string.sitename), sitename)
        datetime_textView.text = String.format(getString(R.string.datetime), datetime)
    }

    private fun hideSystemUI() {

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}