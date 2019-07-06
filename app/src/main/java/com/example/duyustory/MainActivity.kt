package com.example.duyustory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter : MainAdapter
    private var catList = listOf<Cat>(Cat("a", "a"), Cat("b", "b"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter(this, catList)

        duyu_recycler.setHasFixedSize(true)
        duyu_recycler.adapter = mainAdapter
    }
}
