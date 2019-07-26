package com.example.duyustory

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PictureActivity : AppCompatActivity() {

    private lateinit var image_URL : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        image_URL = intent.getStringExtra("Picture_URL")
        Log.d("image_URL", image_URL)
    }
}