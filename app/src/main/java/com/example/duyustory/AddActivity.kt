package com.example.duyustory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.GradientDrawable
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val drawable = this@AddActivity.getDrawable(R.drawable.background_rounding) as GradientDrawable
        catImageView.background = drawable
        catImageView.clipToOutline = true
    }
}