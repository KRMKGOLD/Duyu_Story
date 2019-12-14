package com.example.duyustory.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.duyustory.data.Cat
import com.example.duyustory.R
import com.example.duyustory.add.AddActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter
    private var catList = arrayListOf<Cat>()
    private val usersDB = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolbar)

        mainAdapter = MainAdapter(this, catList)

        duyu_recycler.setHasFixedSize(true)
        duyu_recycler.adapter = mainAdapter
        duyu_recycler.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onResume() {
        super.onResume()
        getDataInDB()
    }

    private fun getDataInDB() {
        showProgressBar()

        usersDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempCatList = arrayListOf<Cat>()

                for (snapshot in dataSnapshot.children) {
                    val catData = snapshot.getValue(Cat::class.java)
                    catData?.let { tempCatList.add(0, it) }
                }

                mainAdapter.setListData(tempCatList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        mainAdapter.notifyDataSetChanged()
        hideProgressBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.startAddActivityMenu -> {
                startActivity(Intent(this, AddActivity::class.java))
            }
        }
        return true
    }

    private fun showProgressBar() {
        mainProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        mainProgressBar.visibility = View.GONE
    }
}