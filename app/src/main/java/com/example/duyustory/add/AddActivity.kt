package com.example.duyustory.add

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
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
import java.io.ByteArrayOutputStream
import java.util.*
import android.view.View
import com.example.duyustory.data.Cat
import com.example.duyustory.R
import com.example.duyustory.util.AddPictureUtil

class AddActivity : AppCompatActivity() {

    private val GET_GALLERY_IMAGE = 200

    private val storageRef = FirebaseStorage.getInstance().reference
    private val database = FirebaseDatabase.getInstance()
    private val usersDB = database.getReference("users")
    private var imageBitmap: Bitmap? = null
    private val addPictureUtil = AddPictureUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        doRoundImageView()

        catImageView.setOnClickListener {
            startActivityForResult(addPictureUtil.getImageInGallery(), GET_GALLERY_IMAGE)
        }

        uploadButton.setOnClickListener {
            if (catImageView.drawable == null || titleEditText.text.toString() == "" || contentEditText.text.toString() == "") {
                Toast.makeText(this, "사진이나 내용을 입력하십시오.", Toast.LENGTH_SHORT).show()
            } else {
                uploadCatImage()
                addProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)

            val imagePath: Uri? = data.data
            val exif = ExifInterface(addPictureUtil.getRealPathFromURI(this, imagePath!!))
            val exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
            val exifDegree = addPictureUtil.exifOrientationToDegrees(exifOrientation)
            imageBitmap = addPictureUtil.rotate(imageBitmap, exifDegree)

            catImageView.setImageBitmap(imageBitmap)
        }
    }

    private fun uploadCatImage() {
        val randomUUDI = "${UUID.randomUUID()}"
        val storageImageRef = storageRef.child("image/$randomUUDI")
        // "${UUID.randomUUID()}.jpg" 은 유니크한 파일이름을 만들기 위함.

        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val catByteData = baos.toByteArray()

        storageImageRef.putBytes(catByteData).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            if (!it.isSuccessful) it.exception?.let { exception -> throw exception }
            return@Continuation storageImageRef.downloadUrl
        }).addOnCompleteListener {
            if (it.isSuccessful) {
                pushCatDataInDB(
                    Cat(
                        it.result.toString(),
                        titleEditText.text.toString(),
                        contentEditText.text.toString()
                    )
                )
            } else {
                Toast.makeText(this, "Error, DB(1) Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pushCatDataInDB(cat: Cat) {
        usersDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {
                Toast.makeText(this@AddActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersDB.push().setValue(cat)
                addProgressBar.visibility = View.GONE
                Toast.makeText(this@AddActivity, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun doRoundImageView() {
        val drawable = this@AddActivity.getDrawable(R.drawable.background_rounding) as GradientDrawable
        catImageView.background = drawable
        catImageView.clipToOutline = true
    }
}