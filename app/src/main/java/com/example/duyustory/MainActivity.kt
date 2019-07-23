package com.example.duyustory

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter
    private var catList = arrayListOf<Cat>()
    private val database = FirebaseDatabase.getInstance()
    private val usersDB = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter(this, catList)

        duyu_recycler.setHasFixedSize(true)
        duyu_recycler.adapter = mainAdapter

        mainProgressBar.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        getDataInDB()
    }

    private fun getDataInDB() {
        usersDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempCatList = arrayListOf<Cat>()

                for (tempSnapshot in dataSnapshot.children) {
                    val catData = tempSnapshot.getValue(Cat::class.java)
                    tempCatList.add(catData!!)
                }
                tempCatList.reverse()
                catList.clear()
                catList.addAll(tempCatList)

                mainProgressBar.visibility = View.GONE
                mainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.startAddActivityMenu -> {
                val startAddActivityIntent = Intent(this, AddActivity::class.java)
                startActivity(startAddActivityIntent)
            }
        }
        return true
    }
}