package com.example.duyustory

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity() {

    private val GET_GALLERY_IMAGE = 200

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val database = FirebaseDatabase.getInstance()
    private val usersDB = database.getReference("users")

    private lateinit var imageURI: Uri
    private lateinit var catURL : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        doRoundImageView()

        catImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(galleryIntent, GET_GALLERY_IMAGE)
        }

        uploadButton.setOnClickListener {
            if (catImageView.drawable == null || titleEditText.text.toString() == "" || contentEditText.text.toString() == "") {
                Toast.makeText(this, "사진이나 내용을 입력하십시오.", Toast.LENGTH_SHORT).show()
            } else {
                addProgressBar.visibility = View.VISIBLE

                val randomUUDI = "${UUID.randomUUID()}"
                val storageImageRef = storageRef.child("image/$randomUUDI")
                // "${UUID.randomUUID()}.jpg" 은 유니크한 파일이름을 만들기 위함.
                val catImageUploadTask = storageImageRef.putFile(imageURI)

                catImageUploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    if (!it.isSuccessful) {
                        it.exception?.let { exception -> throw exception }
                    }
                    return@Continuation storageImageRef.downloadUrl
                }).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val catDataWithImageURL = Cat(it.result.toString(), titleEditText.text.toString(), contentEditText.text.toString())
                        pushCatDataInDB(catDataWithImageURL)
                    } else {
                        Toast.makeText(this, "Error, DB(1) Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun pushCatDataInDB (cat : Cat) {
        usersDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataBaseError : DatabaseError) {
                Toast.makeText(this@AddActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersDB.push().setValue(cat)
                addProgressBar.visibility = View.GONE
                Toast.makeText(this@AddActivity, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun doRoundImageView() {
        val drawable = this@AddActivity.getDrawable(R.drawable.background_rounding) as GradientDrawable
        catImageView.background = drawable
        catImageView.clipToOutline = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            imageURI = data.data
            catImageView.setImageURI(imageURI)
        }
    }
}