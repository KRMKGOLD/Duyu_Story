package com.example.duyustory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter
    private var catList = arrayListOf<Cat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        val usersDB = database.getReference("users")

        mainAdapter = MainAdapter(this, catList)

        duyu_recycler.setHasFixedSize(true)
        duyu_recycler.adapter = mainAdapter

        usersDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (tempSnapshot in dataSnapshot.children) {
                    val catData = tempSnapshot.getValue(Cat::class.java)
                    catList.add(catData!!)
                }
                Log.d("catList", catList.toString())

                mainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

        })

//        usersDB.setValue(catList)

    }
}
