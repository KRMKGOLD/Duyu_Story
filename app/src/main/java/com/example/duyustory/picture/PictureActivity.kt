package com.example.duyustory.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_picture.*
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.duyustory.R

class PictureActivity : AppCompatActivity() {

    private lateinit var imageUrl: String

    private fun onOffFullScreen() {
        val overlay = findViewById<ConstraintLayout>(R.id.pictureLayout)
        overlay.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBarTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        onOffFullScreen()

        imageUrl = intent.getStringExtra("Picture_URL")

        Glide.with(this).load(imageUrl).into(fullScreenImageView)
    }
}