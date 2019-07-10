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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity() {

    private val GET_GALLERY_IMAGE = 200
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private lateinit var imageURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val drawable = this@AddActivity.getDrawable(R.drawable.background_rounding) as GradientDrawable
        catImageView.background = drawable
        catImageView.clipToOutline = true

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

                catImageUploadTask.addOnFailureListener {
                    Toast.makeText(this, "업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("exception", it.printStackTrace().toString())
                    addProgressBar.visibility = View.GONE




                }.addOnSuccessListener {
                    Toast.makeText(this, "업로드에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    addProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            imageURI = data.data
            catImageView.setImageURI(imageURI)
        }
    }
}